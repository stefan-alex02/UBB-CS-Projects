namespace CookieShapesAsOperator.Domain.Shapes;

public class Circle : IShape {
    public double Radius { get; }
    
    public Circle(double radius) {
        Radius = radius;
    }

    public override string ToString() {
        return "\u25cb";
    }
}