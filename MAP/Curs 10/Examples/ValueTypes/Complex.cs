namespace ValueTypes;

public class Complex {
    public int Re { get; set; }
    public int Im { get; set; }

    public Complex(int re, int im) => (Re, Im) = (re, im);

    // public MutableComplex(int re, int im) {
    //     (Re, Im) = (re, im);
    // }
    
    // public MutableComplex(int re, int im) {
    //     Re = re;
    //     Im = im;
    // }
}