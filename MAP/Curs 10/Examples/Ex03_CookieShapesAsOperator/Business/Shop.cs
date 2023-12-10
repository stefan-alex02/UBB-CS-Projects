using CookieShapesAsOperator.Domain;
using CookieShapesAsOperator.Domain.Shapes;
using CookieShapesAsOperator.Exceptions;

namespace CookieShapesAsOperator.Business;

public class Shop {
    public String Name { get; }
    public Kitchen Kitchen {get;}

    public Shop(string name) {
        Name = name;
        Kitchen = new Kitchen();

        Console.WriteLine("New shop opened : " + name);
    }

    public void SetDefaultBakingCapacity(double doughAmount) {
        Kitchen.DefaultDoughAmount = doughAmount;
    }

    public void PurchaseCutters(IEnumerable<CookieCutter<IShape>> cookieCutters) {
        Console.WriteLine("Purchasing new cookie cutters...");
        Kitchen.AddCookieCutters(cookieCutters);
    }

    public List<Cookie> PlaceOrder(CookieRequest request) {
        Console.WriteLine("Shop : Received order : " + request);
        
        foreach (var shape in request.RequestedShapes) {
            if (Kitchen.CookieCutters.All(cutter => cutter.Shape.GetType() != shape)) {
                throw new UnavailableShapeException(shape.Name + " shape is not available for cookies");
            }
        }
        
        return Kitchen.CreateCookies(request);
    }
}