package web2024.calendarapp;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.DynamicAttributes;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;

public class CalendarTag extends SimpleTagSupport implements DynamicAttributes {
    private int an;
    private int luna;
    private int zi = -1;
    private String culoare;
    private String cssClass;

    public void setAn(int an) {
        this.an = an;
    }

    public void setLuna(int luna) {
        this.luna = luna;
    }

    public void setZi(int zi) {
        this.zi = zi;
    }

    public void setCuloare(String culoare) {
        this.culoare = culoare;
    }

    @Override
    public void setDynamicAttribute(String uri, String localName, Object value) throws JspException {
        if ("class".equals(localName)) {
            cssClass = (String) value;
        }
    }

    @Override
    public void doTag() throws JspException, IOException {
        JspWriter out = getJspContext().getOut();

        YearMonth yearMonth = YearMonth.of(an, luna);
        LocalDate firstDayOfMonth = yearMonth.atDay(1);
        int daysInMonth = yearMonth.lengthOfMonth();
        int firstDayOfWeek = firstDayOfMonth.getDayOfWeek().getValue();

        out.write("<div class='" + (cssClass != null ? cssClass : "") + "'>");
        out.write("<table>");
        out.write("<thead>");
        out.write("<tr>");

        for (String dayName : new String[]{"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"}) {
            out.write("<th>" + dayName + "</th>");
        }

        out.write("</tr>");
        out.write("</thead>");
        out.write("<tbody>");
        out.write("<tr>");

        for (int i = 1; i < firstDayOfWeek; i++) {
            out.write("<td></td>");
        }

        for (int day = 1; day <= daysInMonth; day++) {
            if ((day + firstDayOfWeek - 2) % 7 == 0) {
                out.write("</tr><tr>");
            }
            if (day == zi && culoare != null) {
                out.write("<td style='background-color:" + culoare + "'>" + day + "</td>");
            } else {
                out.write("<td>" + day + "</td>");
            }
        }

        out.write("</tr>");
        out.write("</tbody>");
        out.write("</table>");
        out.write("</div>");
    }
}
