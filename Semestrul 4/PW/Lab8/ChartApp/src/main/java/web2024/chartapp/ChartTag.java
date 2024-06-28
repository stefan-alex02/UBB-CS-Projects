package web2024.chartapp;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorModel;
import java.awt.image.RescaleOp;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

public class ChartTag extends SimpleTagSupport {
    private List<Double> oxValues;
    private List<Double> oyValues;
    private String oxLabel;
    private String oyLabel;
    private Double oxMin;
    private Double oxMax;
    private Double oyMin;
    private Double oyMax;
    private String color;
    private String imageUrl;
    private int width = 600;
    private int height = 400;
    private final int margin = 50;

    private final int errorMargin = 10;

    public void setOxValues(String oxValues) {
        this.oxValues = parseValues(oxValues);
    }

    public void setOyValues(String oyValues) {
        this.oyValues = parseValues(oyValues);
    }

    public void setOxLabel(String oxLabel) {
        this.oxLabel = oxLabel;
    }

    public void setOyLabel(String oyLabel) {
        this.oyLabel = oyLabel;
    }

    public void setOxMin(Double oxMin) {
        this.oxMin = oxMin;
    }

    public void setOxMax(Double oxMax) {
        this.oxMax = oxMax;
    }

    public void setOyMin(Double oyMin) {
        this.oyMin = oyMin;
    }

    public void setOyMax(Double oyMax) {
        this.oyMax = oyMax;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    private List<Double> parseValues(String values) {
        List<String> stringValues = new ArrayList<>(Arrays.asList(values.split(",")));
        List<Double> doubleValues = new ArrayList<>();

        for (String stringValue : stringValues) {
            doubleValues.add(Double.parseDouble(stringValue));
        }

        return doubleValues;
    }

    @Override
    public void doTag() throws JspException, IOException {
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bufferedImage.createGraphics();

        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);

        g2d.setColor(Color.BLACK);
        g2d.drawLine(margin, height - margin, width - margin, height - margin);
        g2d.drawLine(margin, height - margin, margin, margin);

        if (oxMin != null) {
            oxValues.add(0, oxMin);
        }
        else {
            oxMin = oxValues.stream().min(Double::compare).orElse(0.0);
        }

        if (oxMax != null) {
            oxValues.add(oxMax);
        }
        else {
            oxMax = oxValues.stream().max(Double::compare).orElse(0.0);
        }

        if (oyMin != null) {
            oyValues.add(0, oyMin);
        }
        else {
            oyMin = oyValues.stream().min(Double::compare).orElse(0.0);
        }

        if (oyMax != null) {
            oyValues.add(oyMax);
        }
        else {
            oyMax = oyValues.stream().max(Double::compare).orElse(0.0);
        }

        for (int i = 0; i < oxValues.size(); i++) {
            int x = margin + (int) (i * (width - 2 * margin) / (double) oxValues.size());
            g2d.drawLine(x, height - margin - 5, x, height - margin + 5);
            if (i < oxValues.size()) {
                g2d.drawString(String.format("%.2f", oxValues.get(i)), x - 10, height - margin + 20);
            }
        }

        for (int i = 0; i < oyValues.size(); i++) {
            int y = height - margin - (int) (i * (height - 2 * margin) / (double) oyValues.size());
            g2d.drawLine(margin - 5, y, margin + 5, y);
            if (i < oyValues.size()) {
                g2d.drawString(String.format("%.2f", oyValues.get(i)), margin - 40, y + 5);
            }
        }

        if (oxLabel != null) {
            g2d.drawString(oxLabel, 275, 380);
        }

        if (oyLabel != null) {
            g2d.drawString(oyLabel, 10, 200);
        }

        Color lineColor = (color != null) ? Color.decode(color) : Color.BLUE;
        g2d.setColor(lineColor);

        try {
            URL url = new URL(imageUrl);
            Image image = ImageIO.read(url);

            int x1 = margin + errorMargin;
            int y1 = height - margin - errorMargin;
            int x2 = width - margin - errorMargin;
            int y2 = margin + errorMargin;
            double scaleX = (x2 - x1) / (double) image.getWidth(null);
            double scaleY = Math.abs((y2 - y1) / (double) image.getHeight(null));
            int newX = x1;
            int newY = y1 - (int) (image.getHeight(null) * scaleY);

            BufferedImageOp colorizeOp = new BufferedImageOp() {
                @Override
                public BufferedImage filter(BufferedImage src, BufferedImage dest) {
                    int width = src.getWidth();
                    int height = src.getHeight();

                    if (dest == null) {
                        dest = createCompatibleDestImage(src, null);
                    }

                    int[] srcRgb = new int[src.getColorModel().getNumComponents()];
                    int[] destRgb = new int[srcRgb.length];

                    for (int x = 0; x < width; x++) {
                        for (int y = 0; y < height; y++) {
                            src.getRaster().getPixel(x, y, srcRgb);

                            // Calculate the luminance of the pixel
                            double luminance = 0.2126 * srcRgb[0] + 0.7152 * (srcRgb.length > 1 ? srcRgb[1] : srcRgb[0]) + 0.0722 * (srcRgb.length > 2 ? srcRgb[2] : srcRgb[0]);

                            // Colorize darker pixels based on the target color and luminance
                            if (luminance < 128) {
                                destRgb[0] = (int) (lineColor.getRed() * (1 - luminance / 255));
                                destRgb[1] = (int) (lineColor.getGreen() * (1 - luminance / 255));
                                destRgb[2] = (int) (lineColor.getBlue() * (1 - luminance / 255));
                            } else {
                                System.arraycopy(srcRgb, 0, destRgb, 0, srcRgb.length);
                            }

                            dest.getRaster().setPixel(x, y, destRgb);
                        }
                    }

                    return dest;
                }


                @Override
                public Rectangle2D getBounds2D(BufferedImage src) {
                    return new Rectangle(0, 0, src.getWidth(), src.getHeight());
                }

                @Override
                public Point2D getPoint2D(Point2D srcPt, Point2D dstPt) {
                    return dstPt == null ? srcPt : new Point2D.Double(dstPt.getX(), dstPt.getY());
                }

                @Override
                public RenderingHints getRenderingHints() {
                    return null;
                }

                @Override
                public BufferedImage createCompatibleDestImage(BufferedImage src, ColorModel destCM) {
                    if (destCM == null) {
                        destCM = src.getColorModel();
                    }
                    return new BufferedImage(destCM, src.copyData(null), src.isAlphaPremultiplied(), null);
                }
            };

            BufferedImage coloredImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = coloredImage.createGraphics();
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, coloredImage.getWidth(), coloredImage.getHeight());
            g.drawImage(image, 0, 0, null);
            g.dispose();
            coloredImage = colorizeOp.filter(coloredImage, null);

            g2d.drawImage(coloredImage, newX, newY, (int) (image.getWidth(null) * scaleX), (int) (image.getHeight(null) * scaleY), null);


            g2d.drawImage(coloredImage, newX, newY, (int) (image.getWidth(null) * scaleX), (int) (image.getHeight(null) * scaleY), null);
        } catch (IOException e) {
            e.printStackTrace();
        }


        g2d.dispose();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", outputStream);
        byte[] imageBytes = outputStream.toByteArray();
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);

        JspWriter out = getJspContext().getOut();
        out.print("<img src=\"data:image/png;base64, " + base64Image + "\" alt=\"Chart\" />");
    }
}
