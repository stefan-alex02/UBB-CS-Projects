package ir.map.g221.domain;

public record ComplexNumber(double re, double im) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ComplexNumber that = (ComplexNumber) o;
        return Double.compare(re, that.re) == 0 && Double.compare(im, that.im) == 0;
    }

    @Override
    public String toString() {
        String reSign, reString, imSign, imString;
        if (re == 0) {
            reSign = "";
            reString = "";
        } else {
            reSign = (re >= 0 ? "" : "- ");
            reString = ((re % 1) == 0 ?
                    Integer.toString((int) Math.abs(re)) :
                    Double.toString(Math.abs(re)));
        }

        if (im == 0) {
            imSign = "";
            imString = "";
        } else {
            if (re == 0) {
                imSign = (im >= 0 ? "" : "- ");
            } else {
                imSign = (im >= 0 ? " + " : " - ");
            }
            if (im == 1 || im == -1) {
                imString = "i";
            } else if ((im % 1) == 0) {
                imString = ((int) Math.abs(im)) + " * i";
            } else {
                imString = (Math.abs(im)) + " * i";
            }
        }

        return reSign + reString + imSign + imString;
    }
}
