package models;

import models.Vector2;

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
}
