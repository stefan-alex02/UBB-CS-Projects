using System;


namespace rt
{
    public class Ellipsoid : Geometry
    {
        private Vector Center { get; }
        private Vector SemiAxesLength { get; }
        private double Radius { get; }
        
        
        public Ellipsoid(Vector center, Vector semiAxesLength, double radius, Material material, Color color) : base(material, color)
        {
            Center = center;
            SemiAxesLength = semiAxesLength;
            Radius = radius;
        }

        public Ellipsoid(Vector center, Vector semiAxesLength, double radius, Color color) : base(color)
        {
            Center = center;
            SemiAxesLength = semiAxesLength;
            Radius = radius;
        }

        public override Intersection GetIntersection(Line line, double minDist, double maxDist) {
            // Creating a new line origin shifted by the ellipsoid's center
            // (since ellipsoid center is considered to be the origin point reference)
            Vector shiftedOrigin = line.X0 - Center;
            
            // Getting coefficients of quadratic equation in t
            double a = Math.Pow(line.Dx.X / SemiAxesLength.X, 2) +
                       Math.Pow(line.Dx.Y / SemiAxesLength.Y, 2) +
                       Math.Pow(line.Dx.Z / SemiAxesLength.Z, 2);
            
            double b = (line.Dx.X * shiftedOrigin.X / Math.Pow(SemiAxesLength.X, 2) +
                        line.Dx.Y * shiftedOrigin.Y / Math.Pow(SemiAxesLength.Y, 2) +
                        line.Dx.Z * shiftedOrigin.Z / Math.Pow(SemiAxesLength.Z, 2)) * 2.0;
            
            double c = Math.Pow(shiftedOrigin.X / SemiAxesLength.X, 2) +
                       Math.Pow(shiftedOrigin.Y / SemiAxesLength.Y, 2) +
                       Math.Pow(shiftedOrigin.Z / SemiAxesLength.Z, 2) - Math.Pow(Radius, 2);

            // Calculating delta of quadratic equation
            double delta = Math.Pow(b, 2) - 4.0 * a * c;

            // Deciding result based on delta's (discriminant's) value
            if (delta >= 0) {
                double t;
                if (delta == 0) {
                    t = -b / (2.0 * a);
                }
                else {
                    double sqrtDelta = Math.Sqrt(delta);
                    t = Math.Min((-b - sqrtDelta) / (2.0 * a), (-b + sqrtDelta) / (2.0 * a));
                }
                
                // Distance between camera center and intersection point
                double dist = (line.CoordinateToPosition(t) - line.X0).Length();
                
                // TODO: normal line must be calculated
                // TODO: what do 'valid' and 'visible' fields do ?
                return new Intersection(true, 
                    t > 0 && dist >= minDist && dist <= maxDist, 
                    this, 
                    line, 
                    t, 
                    null, 
                    Material, 
                    Color);
            }
            return Intersection.NONE;
        }
    }
}
