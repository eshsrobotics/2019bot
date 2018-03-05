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
        private final FakeEncoder encoder;
        private final FakeGyro gyro;

        /***
         * The encoder drift we use if not given one during construction.
         */
        private static final double DEFAULT_ENCODER_DRIFT_PER_FOOT = 0.001;

        /**
         * Returns the fake tank drive used to store this virtual robot's
         * position, direction, and velocity.
         */
        public FakeTankDrive getDrive() {
                return drive;
        }

        /**
         * Returns the fake encoder used to track total distance traveled.
         */
        public FakeEncoder getEncoder() {
            return encoder;
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
         *
         * A FakeTankDrive created this way will not use an encoder or gyro.
         */
        public FakeRobotModel(Point position, double width, Vector2 direction) {
            encoder = null;
            gyro = null;
            drive = new FakeTankDrive(position, width, encoder, gyro);
            drive.setDirection(direction.length() < Constants.EPSILON ? new Vector2(0, 1) : direction);
        }

        /**
         * Creates a fake robot at the given position, with the given width
         * between the (virtual) tank treads and pointing in the given
         * direction.
         *
         * @param position  The initial position of the robot (and thus the
         *                  drive and encoder.)
         * @param width     The distance between the virtual tank treads of the
         *                  drive, in feet.
         * @param direction The initial direction unit vector.
         * @param maxEncoderDeviancePerFoot The maximum amount by which the
         *                                  encoder is allowed to misreport
         *                                  its distance per foot traveled.
         * @param maxGyroDeviancePerDegree  The maximum amount by which the
         *                                  gyro is allowed to misreport
         *                                  its angle per degree rotated.
         */
        public FakeRobotModel(Point position, double width, Vector2 direction,
                              double maxEncoderDeviancePerFoot,
                              double maxGyroDeviancePerDegree) {
            encoder = new FakeEncoder(position, maxEncoderDeviancePerFoot);
            gyro = new FakeGyro(0, maxGyroDeviancePerDegree);
            drive = new FakeTankDrive(position, width, encoder, gyro);
            drive.setDirection(direction.length() < Constants.EPSILON ? new Vector2(0, 1) : direction);
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
                 * Translates a linear velocity, in unit-less left- and
                 * right-speeds, to a sensible instantaneous rotational
                 * velocity in radians.
                 *
                 * This constant is arbitrary and designed just to make the
                 * simulation look decent.  The maximum linear rotation speed
                 * is 2.0 (representing full throttle in opposite directions
                 * for the tank drive), and I wanted that to correspond to a
                 * rotation by 90 degrees.
                 *
                 * This does mean that rotating by (+1, -1) will spin you
                 * around very, VERY quickly -- 5 times per second if
                 * tankDrive() is called at a reasonable framerate of 20 Hz!
                 * If you want the robot to rotate more slowly, change the 90
                 * to something lower.
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
                 * Every time tankDrive() is called, our position can change.
                 * We use this object to (somewhat inaccurately) keep track of
                 * distance traveled.
                 *
                 * It's okay if this value is null; in that case, we just won't
                 * use it.
                 */
                private FakeEncoder encoder;

                /**
                 * Every time tankDrive() is called, our angle can change.
                 * We use this object to (somewhat inaccurately) keep track of
                 * degrees rotated.
                 *
                 * It's okay if this value is null; in that case, we just won't
                 * use it.
                 */
                private FakeGyro gyro;

                /**
                 * Returns the position of the drive (in feet.)
                 */
                public Point getPosition() {
                        return new Point(position); // Prevents "getPosition().x += 100".
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
                        return new Vector2(direction); // Prevents "getDirection().y = 9999".
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
                 * Initializes this tank drive in 2D space without an encoder
                 * or gyro.
                 *
                 * @param position    The position of the center of the robot,
                 *                    in feet.
                 * @param width       The width of the disembodied line
                 *                    segment, in feet.
                 * @param fakeEncoder The fake encoder that we will report our
                 *                    position to.
                 */
                public FakeTankDrive(Point position, double width) {
                        this(position, width, null, null);
                }

                /**
                 * Initializes this tank drive in 2D space.
                 *
                 * @param position    The position of the center of the robot,
                 *                    in feet.
                 * @param width       The width of the disembodied line
                 *                    segment, in feet.
                 * @param fakeEncoder The fake encoder that we will report our
                 *                    position to.  This can be null.
                 * @param fakeGyro    The fake gyro that we will report our
                 *                    angle to.  This can be null.
                 */
                public FakeTankDrive(Point position, double width, FakeEncoder fakeEncoder, FakeGyro fakeGyro) {
                        this.position = position;
                        this.width = width;
                        this.direction = new Vector2(0, 1);
                        this.speed = 0;
                        this.encoder = fakeEncoder;
                        if (encoder != null) {
                            encoder.reset();
                        }
                        this.gyro = fakeGyro;
                        if (gyro != null) {
                            gyro.reset();
                        }
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

                        double oldAngleInRadians = Math.atan2(this.direction.y, this.direction.x);

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
                        // System.out.printf("[L =%s, R =%s]\n", L, R);

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
                        // System.out.printf("[l =%.3f, r =%.3f]\n", l, r);
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
                        // System.out.printf("[L'=%s, R'=%s]\n", newL, newR);

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
                        // rotated clockwise by 90 degrees), leading to a final
                        // velocity of:
                        //
                        //   V' = m * Vector2(-(R' - L').y, (R' - L').x)
                        //
                        // If V's magnitude is greater than the
                        // maximum speed, we clip it; if it is less
                        // than the minimum epsilon threshold, we zero
                        // it.

                        Vector2 newSegmentVector = newL.vectorTo(newR);
                        Vector2 velocity = new Vector2(-newSegmentVector.y, newSegmentVector.x);
                        //System.out.printf("[C=%s, newSegmentVector=%s, velocity=%s]\n", C, newSegmentVector, velocity);
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
                        Point newCenterAfterRotation = new Point((newL.x + newR.x)/2,
                                                                 (newL.y + newR.y)/2);
                        this.position = newCenterAfterRotation.add(this.direction.mult(this.speed));
                        if (encoder != null) {
                            encoder.updatePosition(this.position);
                        }
                        if (gyro != null) {
                            double newAngleInRadians = Math.atan2(this.direction.y, this.direction.x);
                            double degreesRotated = (newAngleInRadians - oldAngleInRadians) * Constants.RADIANS_TO_DEGREES;
                            gyro.updateAngle(degreesRotated);
                        }
                }

                /**
                 * Returns a string representation of this object.
                 */
                @Override
                public String toString() {
                    String encoderInformation = "";
                    if (encoder != null) {
                        encoderInformation = String.format(", distance=%.6f", encoder.getDistance());
                    }

                    String gyroInformation = "";
                    if (gyro != null) {
                        gyroInformation = String.format(", angle=%.6f", gyro.getAngle());
                    }

                    return String.format("FakeTankDrive(position=%s, direction=%s, speed=%f%s%s",
                                         position,
                                         direction,
                                         speed,
                                         encoderInformation,
                                         gyroInformation);
                }
        }
}
