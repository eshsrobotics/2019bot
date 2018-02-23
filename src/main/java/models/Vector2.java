package models;

/**
 * A simple class for waypoint vector math.
 */
public class Vector2 {

        public double x;
        public double y;

        /**
         * Constructs a vector with the given coordinates.
         */
        public Vector2(double x, double y) {
                 this.x = x;
                 this.y = y;
        }

        /**
         * Copies the given vector into this one.
         */
        public Vector2(Vector2 vector) {
                this.x = vector.x;
                this.y = vector.y;
        }

        /**
         * Initializes this vector from the given point.
         */
        public Vector2(Point p) {
                // This can be interpreted as the vector pointing from origin to p.
                this.x = p.x;
                this.y = p.y;
        }

        /**
         * Returns the sum of this vector and another.
         */
        public Vector2 add(Vector2 vector) {
                return new Vector2(x + vector.x, y + vector.y);
        }

        /**
         * Returns the difference between this vector and another.
         */
        public Vector2 sub(Vector2 vector) {
                return new Vector2(x - vector.x, y - vector.y);
        }

        /**
         * Returns a vector equal to this vector scaled by the given factor.
         */
        public Vector2 mult(double scalar) {
                return new Vector2(x * scalar, y * scalar);
        }

        /**
         * Returns a vector equal to this vector scaled by the multiplicative inverse of the given factor.
         */
        public Vector2 div(double scalar) {
                return new Vector2(x / scalar, y / scalar);
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
                return this.div(length());
        }

        /***
         * Returns the angle, in radians, between this vector and another.
         */
        public double angleBetween(Vector2 other) {
                // a • b = |a| |b| cos θ
                return Math.acos(this.dot(other) / (this.length() * other.length()));
        }

        /***
         * Rotates this vector counterclockwise around the origin by the given
         * angle.
         * @param theta The rotation angle in radians.
         */
        public void rotate(double theta) {
            double rx = x * Math.cos(theta) - y * Math.sin(theta);
            double ry = x * Math.sin(theta) + y * Math.cos(theta);
            x = rx;
            y = ry;
        }

        /***
         * Returns a vector that represents the value this vector would have if
         * it were rotated by the given angle around the origin.
         *
         * @param theta The rotation angle in radians.
         * @return The rotated vector.
         */
        public Vector2 rotatedBy(double theta) {
            Vector2 v = new Vector2(this);
            v.rotate(theta);
            return v;
        }

        /***
         * Converts this vector to a string.
         */
        @Override
        public String toString() {
            return String.format("Vector(%.3f, %.3f)", x, y);
        }
}
