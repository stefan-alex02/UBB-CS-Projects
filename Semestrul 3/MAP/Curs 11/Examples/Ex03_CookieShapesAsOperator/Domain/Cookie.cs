using CookieShapesAsOperator.Domain.Shapes;

namespace CookieShapesAsOperator.Domain;

public class Cookie {
    public double Size { get; }
    public bool Baked { get; private set; }
    
    public Cookie(double size) {
        Size = size;
        Baked = false;
    }

    public void Bake() {
        Baked = true;
    }

    public override string ToString() {
        return "\ud83c\udf6a:" + Size;
    }
}