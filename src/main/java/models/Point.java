package models;

public class Point {

        public double x;
        public double y;

        public Point(double x,  double y) {
                this.x = x;
                this.y = y;
        }

        /**
         * Copies the given point into this one.
         */
        public Point(Point p) {
                this.x = p.x;
                this.y = p.y;
        }

        /**
         * Initializes this point from the given vector.
         */
        public Point(Vector2 v) {
                // This can be interpreted as the translation from the origin by v.
                this.x = v.x;
                this.y = v.y;
        }

        public Point add(Vector2 vector) {
                return new Point(x + vector.x, y + vector.y);
        }

        public Point sub(Vector2 vector) {
                return new Point(x - vector.x, y - vector.y);
        }

        /**
         * Returns the vector that, when added to this, yields dest.
         */
        public Vector2 vectorTo(Point dest) {
                // The vector from P1 to P2 is P2 - P1.
                return new Vector2(dest).sub(new Vector2(this));
        }

        /**
         * Returns the distance between this point and another.
         */
        public double dist(Point other) {
                return vectorTo(other).length();
        }

        /***
         * Rotates this point by the given angle around the given point.
         * @param center The center of rotation.
         * @param theta The rotation angle in radians.
         */
        public void rotateAround(Point center, double theta) {
            Point p = new Point(this);
            Vector2 v = new Vector2(center);
            p = p.add(v.mult(-1));                        // Translate.
            Vector2 vr = new Vector2(p).rotatedBy(theta); // Rotate.
            p = new Point(vr).add(v);                     // Translate back.
            x = p.x;
            y = p.y;
        }

        /***
         * Returns the value this point would have if it were rotated by the
         * given angle around the given point.
         * @param center The center of rotation.
         * @param theta The rotation angle in radians.
         * @return The rotated point.
         */
        public Point rotatedAround(Point center, double theta) {
            Point p = this;
            p.rotateAround(center, theta);
            return p;
        }

        /***
         * Converts this point to a string.
         */
        @Override
        public String toString() {
            return String.format("Point(%.3f, %.3f)", x, y);
        }
}
