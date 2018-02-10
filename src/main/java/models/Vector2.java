package models;

import java.lang.Math;

/**
 * A simple class for waypoint vector math.
 */
public class Vector2 {

        public double x;
        public double y;

        /**
         * Copies the given vector into this one.
         */
        public Vector2(Vector2 vector) {
                this.x = vector.x;
                this.y = vector.y;
        }

        /**
         * Constructs a vector with the given coordinates.
         */
        public Vector2(double x, double y) {
                 this.x = x;
                 this.y = y;
        }

        /**
         * Returns the sum of this vector and another.
         */
        public Vector2 add(Vector2 vector) {
                return new Vector2(x + vector.x, y + vector.y);
        }

        /**
         * Returns a vector equal to this vector scaled by the given factor.
         */
        public Vector2 mult(double scalar) {
                return new Vector2(x * scalar, y * scalar);
        }

        /***
         * Returns the dot product of this vector and given vector.
         */
        public double dot(Vector2 dest) {
                return x * dest.x + y * dest.y;
        }

        /***
         * Returns the length of this vector.
         */
        public double length() {
                return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
        }

        /***
         * Returns the normalized version of this vector.
         *
         * Normalizing a vector divides it by its length, guaranteeing
         * a vector of length 1 for all vectors except the zero
         * vector.
         */
        public Vector2 normalized() {
                return this.mult(1.0/length());
        }

        /***
         * Returns the angle, in radians, between this vector and another.
         */
        public double angle(Vector2 other) {
                // a • b = |a| |b| cos θ
                return Math.acos(this.length() * other.length()) / this.dot(other);
        }

}
