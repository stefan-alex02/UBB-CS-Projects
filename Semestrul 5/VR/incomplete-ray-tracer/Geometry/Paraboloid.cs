using System;
using System.Linq;
using rt.MathUtil;

namespace rt;

public class Paraboloid : Geometry {
    public Vector Center { get; }
    public Tuple<double, double> CurveDistances { get; }

    public Paraboloid(Vector center, Tuple<double, double> curveDistances, Material material, Color color) : 
        base(material, color) {
        Center = center;
        CurveDistances = curveDistances;
    }

    public Paraboloid(Vector center, Tuple<double, double> curveDistances, Color color) : base(color) {
        Center = center;
        CurveDistances = curveDistances;
    }

    public override Intersection GetIntersection(Line line, double minDist, double maxDist) {
        // Shifting line origin by the ellipsoid's center
        Vector shiftedOrigin = line.X0 - Center;

        // Getting coefficients of quadratic equation in t
        double a = Math.Pow(line.Dx.X, 2) / CurveDistances.Item1 +
                   Math.Pow(line.Dx.Y, 2) / CurveDistances.Item2;

        double b = (line.Dx.X * shiftedOrigin.X / CurveDistances.Item1 +
                    line.Dx.Y * shiftedOrigin.Y / CurveDistances.Item2 -
                    line.Dx.Z) * 2.0;

        double c = Math.Pow(shiftedOrigin.X, 2) / CurveDistances.Item1 +
                   Math.Pow(shiftedOrigin.Y, 2) / CurveDistances.Item2 - 
                   2 * shiftedOrigin.Z;

        // Getting solution of quadratic equation
        Solution solution = new QuadraticEquation(a, b, c).Solve();

        if (solution.IsEmpty) {
            return Intersection.NONE;
        }

        // Taking the closest intersection point
        double? t = solution.SmallestPositiveSolutionOrNull();

        // Distance between camera center and intersection point
        double dist = (line.CoordinateToPosition(t ?? 0) - line.X0).Length();
            
        // TODO: normal line must be calculated
        // TODO: what do 'valid' and 'visible' fields do ?
        return new Intersection(true,
            t is not null && dist >= minDist && dist <= maxDist,
            this,
            line,
            t ?? 0,
            null,
            Material,
            Color);
    }
}