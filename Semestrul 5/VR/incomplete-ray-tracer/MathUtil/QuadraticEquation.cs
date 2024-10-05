using System;

namespace rt.MathUtil;

/// <summary>
/// A class for managing quadratic equation of type Ax^2 + Bx + C = 0
/// </summary>
public class QuadraticEquation {
    public double A;
    public double B;
    public double C;

    public QuadraticEquation(double a, double b, double c) {
        A = a;
        B = b;
        C = c;
    }

    public Solution Solve() {
        // Calculating delta of quadratic equation
        double delta = Math.Pow(B, 2) - 4.0 * A * C;

        // Deciding result based on delta's (discriminant's) value
        switch (delta) {
            case > 0: {
                double sqrtDelta = Math.Sqrt(delta);
                return new Solution(new[] {
                    (-B - sqrtDelta) / (2.0 * A),
                    (-B + sqrtDelta) / (2.0 * A)
                });
            }
            case 0:
                return Solution.Unique(-B / (2.0 * A));
            default:
                return Solution.Empty;
        }
    }
}