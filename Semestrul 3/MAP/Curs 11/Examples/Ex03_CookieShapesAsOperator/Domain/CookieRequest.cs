using System.Text;
using CookieShapesAsOperator.Domain.Shapes;

namespace CookieShapesAsOperator.Domain;

public class CookieRequest {
    public Type[] RequestedShapes { get; private set; }
    
    public int CookieAmount { get; private set; }

    public CookieRequest(Type[] requestedShapes, int cookieAmount) {
        foreach (var type in requestedShapes) {
            if (!type.GetInterfaces().Contains(typeof(IShape))) {
                throw new ArgumentException("Given type must be an IShape");
            }
        }
        this.RequestedShapes = requestedShapes;
        this.CookieAmount = cookieAmount;
    }

    public bool HasShape(IShape shape) {
        foreach (var requestedShapeType in RequestedShapes) {
            if (requestedShapeType == shape.GetType())
                return true;
        }
        return false;
    }

    public override string ToString() {
        StringBuilder stringBuilder = new StringBuilder();
        
        stringBuilder.Append(CookieAmount + "\ud83c\udf6a, with shapes : ");
        foreach (var shape in RequestedShapes) {
            stringBuilder.Append(shape.Name + " ");
        }
        
        return stringBuilder.ToString();
    }
}