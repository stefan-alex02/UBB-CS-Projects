using CookieShapesAsOperator.Domain.Shapes;
using CookieShapesAsOperator.Exceptions;

namespace CookieShapesAsOperator.Domain;

public class CookieCutter<TS> where TS : IShape {
    private double leftoverDough;
    public TS Shape { get; private set; }

    public CookieCutter(TS shape) {
        Shape = shape;
        leftoverDough = 0;

        Console.WriteLine("New cookie cutter : " + this.ToString());
    }

    /// <summary>
    /// Calculates the size (volume) of the shape. We consider that all shapes
    /// have a height of 1 (cm).
    /// </summary>
    /// <returns></returns>
    private double CalculateSize() {
        // A demonstration of Pattern-Matching :
        switch (Shape) {
            case Circle c:
                return Math.PI * Math.Pow(c.Radius, 2);
            
            case Triangle { IsEquilateral: true } t:
            // case Triangle t when t.IsEquilateral:                 // equivalent to the above line
            // else if ( Shape is Triangle t && t.IsEquilateral )    // equivalent (semantically) to the above lines
            // else if ( Shape is Triangle { IsEquilateral: true } ) // equivalent (semantically) to the above lines
            
                return Math.Pow(t.Side1, 2) * Math.Sqrt(3) / 4;
            
            case Triangle t:
                // Calculate triangle surface using Heron's formula :
                double p = (t.Side1 + t.Side2 + t.Side3) / 2;
                return Math.Sqrt(p * (p - t.Side1) * (p - t.Side2) * (p - t.Side3));
            
            case Square s:
                return Math.Pow(s.Side, 2);
            
            default: ;
                return 0.0;
        }
    }

    public Cookie Use(PackagedDough packagedDough) {
        double cutterSize = this.CalculateSize();
        
        double neededAmount = cutterSize - leftoverDough;
        
        try {
            double amountObtained = packagedDough.Consume(neededAmount); // Trying to take as much dough as we need

            // If the obtained dough is not enough, keep it as leftover and throw exception :
            if (amountObtained + leftoverDough < cutterSize) {
                leftoverDough += amountObtained;

                Console.WriteLine("Cookie cutter : could only get " + amountObtained + "\ud83c\udf5e. " +
                                  "Total leftover : " + leftoverDough + "\ud83c\udf5e. ");

                throw new InsufficientDoughException("Not enough dough", 
                    cutterSize - leftoverDough);
            }

            // Creating the cookie :
            leftoverDough = 0;
            
            Cookie newCookie = new Cookie(cutterSize);
            Console.WriteLine("Cookie cutter : New cookie created " + newCookie + ". Leftover is now 0\ud83c\udf5e.");
            
            return newCookie;
        }
        catch (ObjectDisposedException) {
            // The package is already empty and disposed, throw exception :
            
            Console.WriteLine("Cookie cutter : given dough is already disposed : " + packagedDough +
                              ". Current leftover : " + leftoverDough + "\ud83c\udf5e. ");
            
            throw new InsufficientDoughException("Packaged dough is empty", 
                cutterSize);
        }
    }

    public override string ToString() {
        return "\ud83e\udd63(" + Shape + ":" + CalculateSize() + ")";
    }
}