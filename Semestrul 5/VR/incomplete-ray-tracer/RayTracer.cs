using System;

namespace rt
{
    class RayTracer
    {
        private Geometry[] geometries;
        private Light[] lights;

        public RayTracer(Geometry[] geometries, Light[] lights)
        {
            this.geometries = geometries;
            this.lights = lights;
        }

        private double ImageToViewPlane(int n, int imgSize, double viewPlaneSize)
        {
            return -n * viewPlaneSize / imgSize + viewPlaneSize / 2;
        }

        private Intersection FindFirstIntersection(Line ray, double minDist, double maxDist)
        {
            var intersection = Intersection.NONE;

            foreach (var geometry in geometries)
            {
                var intr = geometry.GetIntersection(ray, minDist, maxDist);

                if (!intr.Valid || !intr.Visible) continue;

                if (!intersection.Valid || !intersection.Visible)
                {
                    intersection = intr;
                }
                else if (intr.T < intersection.T)
                {
                    intersection = intr;
                }
            }

            return intersection;
        }

        private bool IsLit(Vector point, Light light)
        {
            // TODO: ADD CODE HERE
            return true;
        }

        public void Render(Camera camera, int width, int height, string filename)
        {
            var background = new Color(0.2, 0.2, 0.2, 1.0);

            var image = new Image(width, height);
            
            camera.Normalize();
            
            // Using a view Parallel vector for calculating positional vector of points in the view plane
            Vector parallel = camera.Direction ^ camera.Up;
            parallel.Normalize();

            for (var i = 0; i < width; i++)
            {
                for (var j = 0; j < height; j++)
                {
                    // Calculating cartesian distances of current point of row j and column i,
                    // relative to the middle point 

                    double a = ImageToViewPlane(j, height, camera.ViewPlaneHeight);
                    double b = ImageToViewPlane(i, width, camera.ViewPlaneWidth);

                    // Console.WriteLine(a + " " + b);

                    Vector x1 = camera.Position +
                                camera.Direction * camera.ViewPlaneDistance + 
                                camera.Up * a +
                                parallel * b;

                    Line line = new Line(camera.Position, x1);

                    Intersection intersection = FindFirstIntersection(line,
                        camera.FrontPlaneDistance,
                        camera.BackPlaneDistance);

                    // Console.WriteLine(intersection.T);
                    
                    image.SetPixel(i, j, 
                        intersection != Intersection.NONE && 
                        intersection.Valid && 
                        intersection.Visible ? 
                            intersection.Color : background);
                }
            }

            image.Store(filename);
        }
    }
}