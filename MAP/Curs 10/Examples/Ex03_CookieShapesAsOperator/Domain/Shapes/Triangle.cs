namespace CookieShapesAsOperator.Domain.Shapes;

public class Triangle : IShape {
    public readonly bool IsEquilateral;
    public double Side1 { get; }
    public double Side2 { get; }
    public double Side3 { get; }
    
    public Triangle(double side1, double side2, double side3) {
        Side1 = side1;
        Side2 = side2;
        Side3 = side3;

        IsEquilateral = Side1.Equals(Side2) && Side2.Equals(Side3);
    }

    public override string ToString() {
        return "\u25b3";
    }
}