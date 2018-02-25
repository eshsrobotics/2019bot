/**
 *
 */
package tests;

import models.Constants;
import models.Point;
import models.TankDriveInterface;
import models.Vector2;

/**
 * A FakeRobotModel is, simply put, a fake TankDriveInterface combined with a
 * fake EncoderInterface.  The idea is that as the tank drive "drives", the
 * encoders will record and return plausible data.
 * @author uakotaobi
 *
 */
public class FakeRobotModel {

        private final FakeTankDrive drive;

        /***
         * Returns the fake tank drive used to store this virtual robot's
         * position, direction, and velocity.
         */
        public FakeTankDrive getDrive() {
        	return drive;
        }

        /**
         * Creates a fake robot that sits at the origin, points up, and is 18
         * inches wide.
         */
        public FakeRobotModel() {
                this(Constants.ORIGIN, 1.5, new Vector2(1, 0));
        }

        /**
         * Creates a fake robot at the given position, with the given width
         * between the (virtual) tank treads and pointing in the given
         * direction.
         */
        public FakeRobotModel(Point position, double width, Vector2 direction) {
                drive = new FakeTankDrive(position, width);
                drive.setDirection(direction);
        }

        /***
         * The drive used to simulate the driving of the fake robot.  It is
         * responsible for tracking the robot's position and direction.
         *
         * The fake robot is modeled as a disembodied line segment with a
         * perpendicular unit vector to fix its forward direction.
         *
         * @author uakotaobi
         */
        public class FakeTankDrive implements TankDriveInterface {

                /**
                 * The maximum speed that we can impart to the left
                 * and right wheels during tankDrive().
                 */
                private static final double MAX_TANK_LINEAR_SPEED = 1.0;

                /**
                 * The minimum speed that we can impart to the left
                 * and right wheels during tankDrive().
                 */
                private static final double MIN_TANK_LINEAR_SPEED = -1.0;

                /***
                 * Translates a linear velocity, in feet per second,
                 * to a sensible rotational velocity in radians per
                 * second.
                 *
                 * This constant is arbitrary and designed just to
                 * make the simulation look decent.  The maximum
                 * linear rotation speed is 2.0 (representing full
                 * throttle in opposite directions for the tank
                 * drive), and I wanted that to correspond to 90
                 * degrees per second.
                 *
                 * If you want the robot to rotate faster, change the
                 * 90 to something higher.
                 */
                private static final double LINEAR_VELOCITY_TO_RADIANS =
                        90 * Constants.DEGREES_TO_RADIANS / (MAX_TANK_LINEAR_SPEED - MIN_TANK_LINEAR_SPEED);

                /**
                 * The separation between the virtual tank treads.
                 */
                private double width;

                /**
                 * The center of the disembodied line segment.
                 */
                private Point position;

                /**
                 * The direction in which the drive points.  This is guaranteed
                 * to be a unit vector.
                 */
                private Vector2 direction;

                /**
                 * The drive's forward velocity vector is always this scalar
                 * value times the direction vector.
                 */
                private double speed;

                /**
                 * Returns the position of the drive (in feet.)
                 */
                public Point getPosition() {
                        return position;
                }

                /**
                 * Sets the position of the drive.
                 *
                 * @param newPosition The drive's new position, with dimensions in feet.
                 */
                public void setPosition(Point newPosition) {
                        this.position = newPosition;
                }

                /**
                 * Sets the direction in which the drive points.
                 *
                 * @param newDirection The drive's new direction.  It will be converted
                 *                      to a unit vector if it is not already one. A
                 *                      zero vector will be ignored.
                 */
                public void setDirection(Vector2 newDirection) {
                        if (newDirection.length() > Constants.EPSILON) {
                                this.direction = newDirection.normalized();
                        }
                }

                /**
                 * Returns the robot's direction vector.  Its length is always 1.
                 *
                 * The robot's instantaneous velocity is always
                 * equal to getDirection() * getSpeed().
                 */
                public Vector2 getDirection() {
                        return direction;
                }

                /**
                 * Returns the drive's speed.  If it is 0, the robot will be
                 * stationary.  It will never be less than 0.
                 *
                 * The robot's instantaneous velocity is always
                 * equal to getDirection() * getSpeed().
                 *
                 * To set this value, you must call the tankDrive() function.
                 */
                public double getSpeed() {
                        return speed;
                }

                /**
                 * Initializes this tank drive in 2D space.
                 *
                 * @param position The position of the center of the robot, in feet.
                 * @param width: The width of the disembodied line segment, in feet.
                 */
                public FakeTankDrive(Point position, double width) {
                        this.position = position;
                        this.width = width;
                        this.direction = new Vector2(0, 1);
                        this.speed = 0;
                }

                /***
                 * Drives the robot forward by the given speeds (for the left and right
                 * sets of virtual wheels.)
                 *
                 * This isn't a physics simulation, so the math is unsophisticated.  I
                 * just hope it's realistic enough for testing.
                 */
                @Override
                public void tankDrive(double leftSpeed, double rightSpeed) {


                        // How do you simulate a tank drive?
                        //
                        // Phrased another way: I apply 50% power
                        // (0.5) to the right wheel and 100% power
                        // (1.0) to the left wheel.  What is the
                        // robot's resulting velocity vector?  How
                        // will it turn?  What direction will it face
                        // 0.02 seconds, 0.5 seconds, 1 second from
                        // now?
                        //
                        // My strategy is simple: Model the robot as a
                        // center point with a line segment of fixed
                        // length which is centered upon it,
                        // perpendicular to the robot's direction of
                        // forward motion, V.
                        //
                        //                  ◥ V
                        //                 /
                        //                /
                        //  L            /
                        //    *._       /
                        //       `-._  /
                        //           `*._
                        //           O   `-._
                        //                   `* R
                        //
                        // Call the center O and the endpoints of the
                        // line segment L and R.

                        Point O = position;
                        Vector2 left = new Vector2(-direction.y, direction.x);
                        Vector2 right = new Vector2(direction.y, -direction.x);
                        Point L = O.add(left.mult(width/2));
                        Point R = O.add(right.mult(width/2));

                        // Now we can power the tank drive with speeds
                        // l and r by treating these speeds as angular
                        // velocities.
                        //
                        //   When l + r = 0, the center of rotation is O (the average of L and R.)
                        //   When l = 0, the center of rotation is L.
                        //   When r = 0, the center of rotation is R.
                        //
                        // Thus the center of rotation can be
                        // expressed as a function of the scalars l
                        // and r and vector from O to L:
                        //
                        //                (r + l)
                        //         /  O + ------- * (L - O), when r ≠ l
                        //        /       (r - l)
                        //   C = (
                        //        \
                        //         \  O,                     when r = l
                        //

                		double r = Math.max(-1, Math.min(1, rightSpeed));
                    	double l = Math.max(-1, Math.min(1, leftSpeed));
                        Point C = O;
                        if (Math.abs(r - l) > Constants.EPSILON) {
                                C = O.add(O.vectorTo(L).mult((r + l)/(r - l)));
                        }

                        // The angle of rotation, θ, is r - l, scaled
                        // appropriately to translate the linear
                        // velocities into acceptable rotational
                        // velocities.

                        double theta = (r - l) * LINEAR_VELOCITY_TO_RADIANS;

                        // Calculate R' and L' by rotating R and L θ degrees
                        // around point C.

                        Point newR = R.rotatedAround(C, theta);
                        Point newL = L.rotatedAround(C, theta);

                        // As for the final velocity V', its magnitude
                        // is clearly:
                        //
                        //   0 when l = -r (the robot pivots in place),
                        //   0 when l = 0 (the robot pivots about R),
                        //   0 when r = 0 (the robot pivots about L), and
                        //   Maximal when l = r (full forward or backward throttle).
                        //
                        // Thus the magnitude can also be expressed as
                        // a function of the scalars r and l:
                        //
                        //   m = r * l * (r + l)

                        double m = r * l * (r + l);

                        // The direction is simply the vector from L'
                        // to R' rotated counterclockwise by 90 degrees
                        // (or, equivalently, the vector from R' to L'
                        // rotated counterclockwise by 90 degrees),
                        // leading to a final velocity of:
                        //
                        //   V' = m * Vector2((R' - L').y, -(R' - L').x)
                        //
                        // If V's magnitude is greater than the
                        // maximum speed, we clip it; if it is less
                        // than the minimum epsilon threshold, we zero
                        // it.

                        Vector2 newSegmentVector = newL.vectorTo(newR);
                        Vector2 velocity = new Vector2(newSegmentVector.y, -newSegmentVector.x);

                        this.speed = Math.min(m, MAX_TANK_LINEAR_SPEED);
                        this.setDirection(velocity);

                        // -----
                        // Armed with this information, we can now answer my own question
                        // from the start of this comment block:
                        //
                        //   r = 0.5
                        //   l = 1.0
                        //   C = O + (1.5/-0.5) * (L - O) = O - 3(L-O) [= O + 3(R-O)]
                        //   (Note that C lies outside of line segment LR.)
                     //   θ = (r - l) * LINEAR_VELOCITY_TO_RADIANS = -0.3925
                        //   m = 0.5 * 1.0 * (1.0 + 0.5) = 0.75
                        //
                        // The remaining quantities are points and vectors that depend on
                        // the original values of R and L.  But the robot is clearly
                        // rotating clockwise at around 22.5 degrees per second as it
                        // moves forward.
                        //
                        // Speaking of which, move forward.
                        this.position = this.position.add(this.direction.mult(this.speed));
                }

                /**
                 * Returns a string representation of this object.
                 */
                @Override
				public String toString() {
                	return String.format("FakeTankDrive(position=%s,  direction=%s, speed=%f", position.toString(), direction.toString(), speed);
                }
        }
}
