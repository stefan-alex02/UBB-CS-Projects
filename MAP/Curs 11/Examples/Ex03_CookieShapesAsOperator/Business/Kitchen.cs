using System.Text;
using CookieShapesAsOperator.Domain;
using CookieShapesAsOperator.Domain.Shapes;
using CookieShapesAsOperator.Exceptions;

namespace CookieShapesAsOperator.Business;

public class Kitchen {
    private Queue<PackagedDough> preparedDough;
    private readonly Random randomizer = new Random();
    
    public List<CookieCutter<IShape>> CookieCutters { get; private set; }
    public double DefaultDoughAmount { get; set; }

    public Kitchen() {
        this.CookieCutters = new List<CookieCutter<IShape>>();
        this.preparedDough = new Queue<PackagedDough>();
    }

    public void AddCookieCutters(IEnumerable<CookieCutter<IShape>> cookieCutters) {
        this.CookieCutters.AddRange(cookieCutters);
    }

    public void DisplayDough() {
        StringBuilder stringBuilder = new StringBuilder();
        foreach (var dough in preparedDough) {
            stringBuilder.Append("(" + dough + ") ");
        }

        Console.WriteLine("Kitchen : Dough packages : " + stringBuilder);
    }

    public void PrepareDough() {
        var newDough = new PackagedDough(DefaultDoughAmount);
        preparedDough.Enqueue(newDough);
        
        Console.WriteLine("Kitchen : New default dough prepared : " + newDough);
        // DisplayDough();
    }
    
    public void PrepareRandomDough(double minimumAmount) {
        var newDough = new PackagedDough(minimumAmount + randomizer.NextDouble() * DefaultDoughAmount);
        preparedDough.Enqueue(newDough);
        
        Console.WriteLine("Kitchen : New dough with random amount prepared : " + newDough);
        // DisplayDough();
    }

    public void BakeCookies(List<Cookie> cookies) => cookies.ForEach(cookie => cookie.Bake()); // Expression body
    
    public List<Cookie> CreateCookies(CookieRequest cookieRequest) {
        List<Cookie> cookies = new List<Cookie>();
        
        // A short demonstration of the use of Linq methods :
        var suitableCutters = CookieCutters
            .Where(cutter => cookieRequest.HasShape(cutter.Shape));
        
        for (int i = 0; i < cookieRequest.CookieAmount; i++) {
            CookieCutter<IShape> chosenCutter =
                suitableCutters.ElementAt(
                    randomizer.Next(suitableCutters.Count())
                );

            Console.WriteLine("Kitchen : Selected cutter with shape : " + chosenCutter);
            
            cookies.Add(CreateCookie(chosenCutter));
            
            DisplayDough();
        }

        BakeCookies(cookies);
        Console.WriteLine("Cookies were baked.");

        return cookies;
    }

    private Cookie CreateCookie(CookieCutter<IShape> cutter) {
        // If there is no dough available, make one :
        if (preparedDough.Count == 0) {
            PrepareDough();
        }
        
        while (true) {
            try {
                Cookie cookie = cutter.Use(preparedDough.Peek());

                return cookie;
            }
            catch (InsufficientDoughException e) {
                // Random chance to prepared a dough with at least the specified amount or a default one
                if (randomizer.Next(0, 3) == 0) {
                    PrepareDough();
                }
                else {
                    PrepareRandomDough(e.NeededAmount);
                }
                
                if (preparedDough.Peek().Disposed) {
                    var disposedPackage = preparedDough.Dequeue();
                    // Console.WriteLine("Kitchen : Disposed empty dough package : " + disposedPackage + ".");
                }
            }
        }
    }
}