using System.Text;
using CookieShapesAsOperator.Business;
using CookieShapesAsOperator.Domain;
using CookieShapesAsOperator.Domain.Shapes;
using CookieShapesAsOperator.Exceptions;

Console.OutputEncoding = Encoding.UTF8;
Console.WriteLine();

Shop shop = new Shop("Sweets shop");

shop.PurchaseCutters(new [] {
    new CookieCutter<IShape>(new Circle(3)),
    new CookieCutter<IShape>(new Circle(2.8)),
    new CookieCutter<IShape>(new Triangle(4, 4, 4)),
    new CookieCutter<IShape>(new Square(6)),
    new CookieCutter<IShape>(new Square(5))
});

shop.SetDefaultBakingCapacity(5);

CookieRequest request1 = new CookieRequest(new [] {
    typeof(Square),
    typeof(Circle)
}, 2);

try {
    var cookies = shop.PlaceOrder(request1);
    
    Console.WriteLine("Received cookies : " + 
                      String.Join(' ', cookies.Select(cookie => cookie.ToString()))
    );
}
catch (UnavailableShapeException e) {
    Console.WriteLine("Shop : Unfortunately, " + e.Message);
}