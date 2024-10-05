using System;

namespace rt {
    class RayTracer {
        private Geometry[] geometries;
        private Light[] lights;

        public RayTracer(Geometry[] geometries, Light[] lights) {
            this.geometries = geometries;
            this.lights = lights;
        }

        private double ImageToViewPlane(int n, int imgSize, double viewPlaneSize) {
            return -n * viewPlaneSize / imgSize + viewPlaneSize / 2;
        }

        private Intersection FindFirstIntersection(Line ray, double minDist, double maxDist) {
            var intersection = Intersection.NONE;

            foreach (var geometry in geometries) {
                // var intr = geometry.GetIntersection(ray, minDist, maxDist);
                var intr = geometry.GetIntersection(ray, minDist, maxDist);

                TryAddGridlines(intr, Color.WHITE, 10, Vector.Ones * 0.5);

                if (!intr.Valid || !intr.Visible) continue;

                if (!intersection.Valid || !intersection.Visible) {
                    intersection = intr;
                }
                else if (intr.T < intersection.T) {
                    intersection = intr;
                }
            }

            return intersection;
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="intersection"></param>
        /// <param name="gridColor"></param>
        /// <param name="gridDistance"></param>
        /// <param name="gridThicknessByAxes">grid line thicknesses, customized to each axis.
        /// Also, if any value is 0, then no grids relative to that axis will be shown</param>
        /// <returns></returns>
        /// <exception cref="Exception"></exception>
        private bool TryAddGridlines(Intersection intersection, Color gridColor, double gridDistance, 
            Vector gridThicknessByAxes) {
            if (gridThicknessByAxes.X < 0 || gridThicknessByAxes.Y < 0 || gridThicknessByAxes.Z < 0) {
                throw new Exception("Invalid thickness");
            }

            if (intersection == Intersection.NONE ||
                (!(Math.Abs(intersection.Position.X - (int)(intersection.Position.X / gridDistance) * gridDistance) <
                   gridThicknessByAxes.X / 2) &&
                 !(Math.Abs(intersection.Position.Y - (int)(intersection.Position.Y / gridDistance) * gridDistance) <
                   gridThicknessByAxes.Y / 2) &&
                 !(Math.Abs(intersection.Position.Z - (int)(intersection.Position.Z / gridDistance) * gridDistance) <
                   gridThicknessByAxes.Z / 2))) {
                return false;
            }
            
            intersection.Color = gridColor;
            return true;
        }

        private bool IsLit(Vector point, Light light) {
            // TODO: ADD CODE HERE
            return true;
        }

        public void Render(Camera camera, int width, int height, string filename) {
            var background = Color.GRAY;

            var image = new Image(width, height);

            camera.Normalize();

            // Using a view Parallel vector for calculating positional vector of points in the view plane
            Vector parallel = camera.Direction ^ camera.Up;
            parallel.Normalize();

            for (var i = 0; i < width; i++) {
                for (var j = 0; j < height; j++) {
                    // Calculating vector position of point in image corresponding view plane
                    double a = ImageToViewPlane(j, height, camera.ViewPlaneHeight);
                    double b = ImageToViewPlane(i, width, camera.ViewPlaneWidth);

                    Vector x1 = camera.Position +
                                camera.Direction * camera.ViewPlaneDistance +
                                camera.Up * a +
                                parallel * b;

                    Line line = new Line(camera.Position, x1);

                    Intersection intersection = FindFirstIntersection(line,
                        camera.FrontPlaneDistance,
                        camera.BackPlaneDistance);

                    image.SetPixel(i, j,
                        intersection != Intersection.NONE &&
                        intersection.Valid &&
                        intersection.Visible
                            ? intersection.Color
                            : background);
                }
            }

            image.Store(filename);
        }
    }
}