namespace CookieShapesAsOperator.Domain.Shapes;

public class Star : IShape {
    
    public double Radius { get; }
    
    public Star(double radius) {
        Radius = radius;
    }

    public override string ToString() {
        return "\u2b50";
    }
}