package org.usfirst.frc.team1759.robot.commands;

import models.Constants;
import models.Vector2;

import org.usfirst.frc.team1759.robot.Sensors;
import org.usfirst.frc.team1759.robot.subsystems.TankDrive;
import edu.wpi.first.wpilibj.command.Command;

/***
 * This command's purpose is to rotate the tank drive continuously until the
 * robot is pointing at the desired heading.
 * 
 * This should not be running at the same time as Go(), as they both utilize the TankDrive.
 *
 * @author uakotaobi
 */
public class TurnCommand extends Command {

        /***
         * The tank drive we're supposed to be turning.
         */
        TankDrive tank;

        /**
         * The forward-pointing vector that the robot has at the time of start().
         */
        Vector2 initialHeading;

        /***
         * The gyro angle (in radians) that the robot measured at the time of start().
         */
        double initialGyroAngle;

        /***
         * This is the heading we ultimately want to be pointing toward.
         */
        Vector2 goalHeading;

        /***
         * Once the angle between our current bearing and the goalHeading is
         * less than this, we can stop turning.
         */
        final double epsilon = 0.01;

        /**
         * Initializes this command.
         *
         * @param tank The tank drive to turn.
         */

        public TurnCommand(TankDrive tank) {
                tank = new TankDrive();
                goalHeading = initialHeading; // By default, we don't turn.
        }
        
        /***
         * Returns the gyro angle, in degrees, between the initial heading at
         * start() and the given heading.
         *
         * That's a little different than merely calculating the /angle/
         * between the two vectors--it also takes the initial gyro angle into
         * account.
         */
        private double vectorToGyroAngle(Vector2 heading) {
                Vector2 normalizedHeading = heading.normalized();
                double gyroAngleInRadians = initialGyroAngle - Math.acos(initialHeading.dot(normalizedHeading));
                return gyroAngleInRadians * Constants.RADIANS_TO_DEGREES;
        }

        /***
         * Bring the command to a valid initial state.
         *
         *  Almost everything we calculate in this command is relative to the
         *  initial heading and initial gyro angle.
         */
        @Override
        public synchronized void start() {

                initialGyroAngle = Sensors.gyro.getAngle() * Constants.DEGREES_TO_RADIANS;
                initialHeading = new Vector2(0, 1); // By completely arbitrary convention, this points up.
        };

        /***
         * Set the heading that we want to turn toward.
         * @param desiredHeading This vector is relative to the robot's
         *                        *starting vector*, which is assumed to point
         *                        forward.
         */
        public void setHeading(Vector2 desiredHeading) {
                goalHeading = desiredHeading.normalized();
        }

        /***
         * This function is called intermittently over and over until
         * isFinished() returns true.
         *
         * It does a tiny bit of work to turn us toward our target
         * incrementally.
         */
        @Override
        protected void execute() {

                double currentGyroAngleInDegrees = Sensors.gyro.getAngle();
                double desiredGyroAngleInDegrees = vectorToGyroAngle(goalHeading);
                double degreesToTurn = desiredGyroAngleInDegrees - currentGyroAngleInDegrees;

                // Turn at the appropriate magnitude.
                //
                // You're probably wondering wondering what I mean!
                //
                // Suppose you have min, max, and current, which lies somewhere
                // between.  The linear interpolation formula to get the
                // parameter of interpolation, u, representing the percentage
                // of distance current is between min and max, is:
                //
                //        (current - min)
                //    u = ---------------
                //          (max - min)
                //
                //    Note that:
                //    * u = 0 when current is at min.
                //    * u = 1 when current is at max.
                //
                // Now consider our robot's turning angle:
                //   * current is degreesToTurn.
                //   * max is the maxTurningAngleDegrees.
                //   * min is the minTurningAngleDegrees.
                //
                // Let's calculate the "magnitude of our turn", which is 1 at
                // the max turning angle and 0 at the minimum.

                final double minTurningAngleDegrees = epsilon; // We can't more slowly than this.
                final double maxTurningAngleDegrees = 10.0;    // We don't want to turn faster than this.

                if (Math.abs(degreesToTurn) > maxTurningAngleDegrees) {
                        // Not allowed to turn faster than this.
                        degreesToTurn = Math.signum(degreesToTurn) * maxTurningAngleDegrees;
                }

                // 0 <= turningMagnitude <= 1.
                // As we get closer and closer to our desired goal, we should
                // turn more and more slowly.
                //
                // Warning: In the end, this may be *too* slow.  We
                // may need to increase epsilon and/or the
                // minTurningAngleDegrees.

                double turningMagnitude = (degreesToTurn - minTurningAngleDegrees) / (maxTurningAngleDegrees - minTurningAngleDegrees);

                if (degreesToTurn < epsilon) {
                        // Turn left.
                        tank.tankDrive(-turningMagnitude, turningMagnitude);
                } else if (degreesToTurn > epsilon) {
                        // Turn right.
                        tank.tankDrive(turningMagnitude, -turningMagnitude);
                }
        }

        @Override
        public void setName(String subsystem, String name) {

        }

        /***
         * Are we done turning?  Did we hit the vector we set out to hit?
         */
        @Override
        protected boolean isFinished() {

                double angleDifference = Math.acos(initialHeading.dot(goalHeading));
                if (Math.abs(angleDifference) < epsilon) {
                        return true;
                } else {
                        return false;
                }
        }
}
