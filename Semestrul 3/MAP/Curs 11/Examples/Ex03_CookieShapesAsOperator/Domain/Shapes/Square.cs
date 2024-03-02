namespace CookieShapesAsOperator.Domain.Shapes;

public class Square : IShape{
    public double Side { get; }
    
    public Square(double side) {
        Side = side;
    }

    public override string ToString() {
        return "\u25a1";
    }
}