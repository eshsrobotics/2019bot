/**
 *
 */
package tests;

import models.Constants;
import models.Point;
import models.TankDriveInterface;

/**
 * A FakeRobotModel is, simply put, a fake TankDriveInterface combined with a
 * fake EncoderInterface.  The idea is that as the tank drive "drives", the
 * encoders will record and return plausible data.
 * @author uakotaobi
 *
 */
public class FakeRobotModel {

    /***
     * Translates a linear velocity, in feet per second, to a sensible
     * rotational velocity in radians per second.
     *
     * This constant is arbitrary and designed just to make the simulation look
     * decent.  The maximum linear rotation speed is 2.0 (representing full
     * throttle in opposite directions for the tank drive), and I wanted that
     * to correspond to 90 degrees per second.
     *
     * If you want the robot to rotate faster, change the 90 to something
     * higher.
     */
    private static final double MAX_TANK_SPEED = 1.0;
    private static final double MIN_TANK_SPEED = -1.0;
    public static final double LINEAR_VELOCITY_TO_RADIANS =
            90 * Constants.DEGREES_TO_RADIANS / (MAX_TANK_SPEED - MIN_TANK_SPEED);

    /**
     * The coordinates of the fake robot, in feet.
     */
    private Point virtualPosition;

    /**
     * Creates a fake robot that sits at the origin.
     */
    public FakeRobotModel() {
        virtualPosition = new Point(0, 0);
    }

    /***
     * Simulated the driving of the fake robot.
     * @author uakotaobi
     *
     */
    public class FakeTankDrive implements TankDriveInterface {

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
            // Phrased another way: I apply 50% power (0.5) to the right wheel and 100%
            // power (1.0) to the left wheel.  What is the robot's resulting velocity
            // vector?  How will it turn?  What direction will it face 0.02 seconds,
            // 0.5 seconds, 1 second from now?
            //
            // My strategy is simple: Model the robot as a center point with a
            // line segment of fixed length which is centered upon it, perpendicular to
            // the robot's direction of forward motion, V.
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
            // Call the center O and the endpoints of the line segment L and R.  Now we
            // can power the tank drive with speeds l and r by treating these speeds as
            // angular velocities.
            //
            //   When l + r = 0, the center of rotation is O (the average of L and R.)
            //   When l = 0, the center of rotation is L.
            //   When r = 0, the center of rotation is R.
            //
            // Thus the center of rotation can be expressed as a function of the
            // scalars l and r and vector from O to L:
            //
            //                (r + l)
            //         /  O + ------- * (L - O), when r ≠ l
            //        /       (r - l)
            //   C = (
            //        \
            //         \  O,                     when r = l
            //
            // The angle of rotation, θ, is r - l, scaled appropriately to
            // translate the linear velocities into acceptable rotational
            // velocities.  Calculate A' and B' by rotating A and B θ degrees
            // around point C.
            //
            // As for the final velocity V', its magnitude is clearly:
            //
            //   0 when l = -r (the robot pivots in place),
            //   0 when l = 0 (the robot pivots about R),
            //   0 when r = 0 (the robot pivots about L), and
            //   Maximal when l = r (full forward or backward throttle).
            //
            // Thus the magnitude can also be expressed as a function of the scalars
            // r and l:
            //
            //   m = r * l * (r + l)
            //
            // The direction is simply the vector from L to R rotated counterclockwise
            // by 90 degrees (or, equivalently, the vector from R to L rotated
            // counterclockwise by 90 degrees), leading to a final velocity of:
            //
            //   V' = m * Vector2((R - L).y, -(R - L).x)
            //
            // If V's magnitude is greater than the maximum speed, we clip it; if it is
            // less than the minimum epsilon threshold, we zero it.
            //
            // -----
            //
            // Armed with this information, we can now answer my own question
            // from the start of this comment block:
            //
            //   r = 0.5
            //   l = 1.0
            //   C = O + (1.5/-0.5) * (L - O) = O - 3(L-O) [= O + 3(R-O)]
            //   (Note that C lies outside of line segment LR.)
            //   θ = (r - l) * LINEAR_VELOCITY_TO_RADIANS = 0.075
        }

    }
}
