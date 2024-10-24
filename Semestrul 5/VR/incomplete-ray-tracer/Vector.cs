﻿using System;

namespace rt {
    public class Vector {
        public static Vector I = new Vector(1, 0, 0);
        public static Vector J = new Vector(0, 1, 0);
        public static Vector K = new Vector(0, 0, 1);
        public static Vector Ones = new Vector(1, 1, 1);
        public static Vector Zero = new();

        public double X { get; set; }
        public double Y { get; set; }
        public double Z { get; set; }

        public Vector() {
            X = 0;
            Y = 0;
            Z = 0;
        }

        public Vector(double x, double y, double z) {
            X = x;
            Y = y;
            Z = z;
        }

        public Vector(Vector v) {
            X = v.X;
            Y = v.Y;
            Z = v.Z;
        }

        /// <summary>
        /// Vector addition
        /// </summary>
        /// <param name="a">first vector</param>
        /// <param name="b">second vector</param>
        /// <returns>the sum of the 2 vectors</returns>
        public static Vector operator +(Vector a, Vector b) {
            return new Vector(a.X + b.X, a.Y + b.Y, a.Z + b.Z);
        }

        /// <summary>
        /// Vector subtraction
        /// </summary>
        /// <param name="a">first vector</param>
        /// <param name="b">second vector</param>
        /// <returns>the subtraction result vector</returns>
        public static Vector operator -(Vector a, Vector b) {
            return new Vector(a.X - b.X, a.Y - b.Y, a.Z - b.Z);
        }

        /// <summary>
        /// Vector dot product
        /// </summary>
        /// <param name="v">first vector</param>
        /// <param name="b">second vector</param>
        /// <returns>the dot product</returns>
        public static double operator *(Vector v, Vector b) {
            return v.X * b.X + v.Y * b.Y + v.Z * b.Z;
        }

        /// <summary>
        /// Vector cross product
        /// </summary>
        /// <param name="a">first vector</param>
        /// <param name="b">second vector</param>
        /// <returns>the cross product</returns>
        public static Vector operator ^(Vector a, Vector b) {
            return new Vector(a.Y * b.Z - a.Z * b.Y, a.Z * b.X - a.X * b.Z, a.X * b.Y - a.Y * b.X);
        }

        /// <summary>
        /// Vector scalar multiplication
        /// </summary>
        /// <param name="v">a vector</param>
        /// <param name="k">a scalar (real number)</param>
        /// <returns>the scalar multiplication</returns>
        public static Vector operator *(Vector v, double k) {
            return new Vector(v.X * k, v.Y * k, v.Z * k);
        }

        /// <summary>
        /// Vector scalar division
        /// </summary>
        /// <param name="v">a vector</param>
        /// <param name="k">a scalar (real number)</param>
        /// <returns>the scalar division</returns>
        public static Vector operator /(Vector v, double k) {
            return new Vector(v.X / k, v.Y / k, v.Z / k);
        }

        public void Multiply(Vector k) {
            X *= k.X;
            Y *= k.Y;
            Z *= k.Z;
        }

        public void Divide(Vector k) {
            X /= k.X;
            Y /= k.Y;
            Z /= k.Z;
        }

        public double Length2() {
            return X * X + Y * Y + Z * Z;
        }

        public double Length() {
            return Math.Sqrt(Length2());
        }

        /// <summary>
        /// Performs a normalization of the vector (vector becomes a unit vector)
        /// </summary>
        /// <returns>the vector that was normalized</returns>
        public Vector Normalize() {
            var norm = Length();
            if (norm > 0.0) {
                X /= norm;
                Y /= norm;
                Z /= norm;
            }

            return this;
        }
    }
}