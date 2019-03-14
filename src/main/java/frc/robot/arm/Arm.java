package frc.robot.arm;

import frc.robot.OI;
import frc.robot.driving.RobotMap;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.AnalogPotentiometer;

/**
 * This subsystem is used to control the arm raising or lowering for 2019bot.
 *
 * @author Spencer Moore
 */

public class Arm extends Subsystem {
        private SpeedController wrist;
        private SpeedController elbow;
        private AnalogPotentiometer pot;
        private Timer timer;
        final static double ELBOW_INCREMENT_INTERVAL_SECONDS = 0.05;
        final static double ELBOW_POWER_INCREMENT_PER_INTERVAL = 0.02;
        final static double ELBOW_POWER_INITIAL_RAMP = 0.2;
        final static double ELBOW_MAX_DOWNWARD_POWER = 0.3;
        final static double EPSILON = 0.05;
        double lastIncrementTimeSeconds = 0;

        // The scale factor here should be the range of degrees that the elbow
        // potentiometer is capable of.
        //
        // When constucting the AnalogPotentiometer with a scale factor of 1.0 and an
        // offset of 0.0, this
        // flaky pot we're testing with gives us values between 0 and 0.27. That's
        // probably
        // not the correct value, but whatever; we can work around that in software.
        //
        // Update: The potentiometer had a resistance in the M
        private final static double MIN_INPUT_POT_VALUE = 0;
        private final static double MAX_INPUT_POT_VALUE = 1.0; // 0.27;

        // The test potentiometer I have here goes from about 4:00 (120 degrees) to
        // about 8:00 (240 degrees).
        private final static double MIN_OUTPUT_POT_VALUE = 120;
        private final static double MAX_OUTPUT_POT_VALUE = 240;
        private final static double SCALE_FACTOR = (MAX_OUTPUT_POT_VALUE - MIN_OUTPUT_POT_VALUE)
                / (MAX_INPUT_POT_VALUE - MIN_INPUT_POT_VALUE);
        private final double OFFSET = MIN_OUTPUT_POT_VALUE;

        public Arm() {
                wrist = new Spark(RobotMap.WRIST_MOVE);
                elbow = new Spark(RobotMap.ELBOW_MOVE);
                // pot = new AnalogPotentiometer(RobotMap.TEST_POTENTIOMETER, 1.0, 0.0);
                pot = new AnalogPotentiometer(RobotMap.TEST_POTENTIOMETER, SCALE_FACTOR, OFFSET);
                timer = new Timer();
                timer.start();
        }

        @Override
        public void setName(String subsystem, String name) {

        }

        @Override
        public void initDefaultCommand() {
                // myRobot = new DifferentialDrive(left, right);
        }

        public void setElbowMotorSpeedBasedOnElbowPotentiometer() {
                // Use linear interpolation for now.
                //
                // u is our parameter of interpolation -- how far along the range we are.
                //
                // u = (current - min) / (max - min)

                double u = (pot.get() - MIN_OUTPUT_POT_VALUE) / (MAX_OUTPUT_POT_VALUE - MIN_OUTPUT_POT_VALUE);
                if (u < RobotMap.MIN_MOTOR_POWER) {
                        u = 0;
                } else if (u > 1) {
                        u = 1;
                }

                // At u = 0.5 we want no power, and at u = 0 or 1, we want max power (in
                // opposite directions.)
                double power = 2 * (u - 0.5);
                // elbow.set(power);

                // System.out.printf("u = (%.1f-%.1f)/(%.1f-%.1f) = %.2f | desired, actual power
                // = %.2f, %.2f ",
                // pot.get(),
                // MIN_OUTPUT_POT_VALUE,
                // MAX_OUTPUT_POT_VALUE,
                // MIN_OUTPUT_POT_VALUE,
                // u,
                // power,
                // elbow.get());
        }

        /**
         * Provides smoothly-accelerating and decelerating speed changes for the given
         * motor.
         *
         * This function is designed to be general enough to be used for any motor in
         * any subsystem. When you want smooth motion that may or may not be influenced
         * by gravity, this is your one-stop shop.
         *
         * @param motor                   The motor whose speed we are adjusting.
         * @param direction               A value that is only relevant for its sign:
         *                                positive (increment), negative (decrement), or
         *                                0 (decay to baseline.)
         * @param minUpdateIntervaleconds We will update the motor no more frequently
         *                                than this.
         * @param positiveIncrement       If signum(direction) > 0, how much will we
         *                                incremement the motor speed?
         * @param negativeIncrement       If signum(direction) < 0, how much will we
         *                                decrement the motor speed?
         * @param ramp                    When the motor is still, how much should we
         *                                initially increment it before using
         *                                positiveIncrement or negativeIncrement?
         * @param min                     What is the minimum speed for the motor? This
         *                                should be no less than -1.
         * @param max                     What is the maximum speed for the motor? This
         *                                should be no greater than 1.
         */
        private void adjustMotor(SpeedController motor, double direction, double minUpdateIntervalSeconds,
                                 double positiveIncrement, double negativeIncrement, double ramp, double min, double max) {

                if (min > max) {
                        // Swap.
                        double temp = min;
                        min = max;
                        max = temp;
                }

                if (max > 1) {
                        max = 1;
                }

                if (min < -1) {
                        min = -1;
                }

                double speed = motor.get();
                if (direction > 0) {
                        // Slow increment every few intervals.
                        if (timer.get() - lastIncrementTimeSeconds > minUpdateIntervalSeconds) {
                                lastIncrementTimeSeconds = timer.get();
                                if (Math.abs(speed) < EPSILON) {
                                        speed = ramp;
                                } else {
                                        speed += Math.abs(positiveIncrement);
                                }
                                if (speed > max) {
                                        speed = max;
                                }
                                System.out.printf("Increased power to %.2f\n", speed);
                        }
                } else if (direction < 0) {
                        // Slow decrement every few intervals.
                        if (timer.get() - lastIncrementTimeSeconds > minUpdateIntervalSeconds) {
                                lastIncrementTimeSeconds = timer.get();
                                if (Math.abs(speed) < EPSILON) {
                                        speed = -ramp;
                                } else {
                                        speed -= Math.abs(negativeIncrement);
                                }
                                if (speed < min) {
                                        speed = min;
                                }
                                System.out.printf("Decreased power to %.2f\n", speed);
                        }
                } else {
                        // Speed decays when there's no set power...but we don't instantly stop (that might
                        // cause the motor to jerk badly.)
                        if (timer.get() - lastIncrementTimeSeconds > minUpdateIntervalSeconds) {
                                lastIncrementTimeSeconds = timer.get();
                                speed -= (Math.signum(speed) * Math.abs(positiveIncrement));
                                if (Math.abs(speed) < EPSILON) {
                                        speed = 0;
                                }
                                System.out.printf("Power decaying to %.2f\n", speed);
                        }
                }
                motor.set(speed);
        }

        /**
         * Moves the elbow motor up (in the opposite direction of gravity.)
         */
        private void moveElbowUp() {
                adjustMotor(elbow, +1, ELBOW_INCREMENT_INTERVAL_SECONDS,
                            ELBOW_POWER_INCREMENT_PER_INTERVAL, ELBOW_POWER_INCREMENT_PER_INTERVAL, ELBOW_POWER_INITIAL_RAMP,
                            -ELBOW_MAX_DOWNWARD_POWER, 1.0);
        }

        /**
         * Moves the elbow down (in the same direction as gravity.)
         */
        private void moveElbowDown() {
                adjustMotor(elbow, -1, ELBOW_INCREMENT_INTERVAL_SECONDS,
                            ELBOW_POWER_INCREMENT_PER_INTERVAL, ELBOW_POWER_INCREMENT_PER_INTERVAL, ELBOW_POWER_INITIAL_RAMP,
                            -ELBOW_MAX_DOWNWARD_POWER, 1.0);
        }

        /**
         * Whether the elbow is moving up or down, this function brings it to a gradual halt.
         */
        private void stopElbowGradually() {
                adjustMotor(elbow, 0, ELBOW_INCREMENT_INTERVAL_SECONDS,
                            ELBOW_POWER_INCREMENT_PER_INTERVAL, ELBOW_POWER_INCREMENT_PER_INTERVAL, ELBOW_POWER_INITIAL_RAMP,
                            -ELBOW_MAX_DOWNWARD_POWER, 1.0);
        }

        public void arm(OI oi) {
                if (oi.joysticksAttached) {
                        double h = wrist.get();
                        if (oi.leftJoystick.getRawButton(3)) {
                                h = 0.3;
                        } else if (oi.rightJoystick.getRawButton(3)) {
                                // Take full advantage of gravity.
                                h = -1;
                        } else {
                                h = 0;
                        }

                        if (oi.leftJoystick.getRawButton(4)) {
                                moveElbowUp();
                        } else if (oi.rightJoystick.getRawButton(4)) {
                                moveElbowDown();
                        } else {
                                stopElbowGradually();
                        }
                        wrist.set(h);
                } else {
                        /*
                         * double left = 0; double right = 0;
                         *
                         * if (oi.forward.get()) { left = 1; right = 1; } else if (oi.back.get()) { left
                         * = -1; right = -1; } else if (oi.left.get()) { left = -1; right = 1; } else if
                         * (oi.right.get()) { left = 1; right = -1; } if (oi.sneak.get()) { if
                         * (oi.left.get() || oi.right.get()) { left *= 0.8; right *= 0.8; } else { left
                         * *= 0.5; right *= 0.5; } }
                         *
                         * myRobot.tankDrive(- left, - right);
                         */}
        }

        public void arm(/* double leftSpeed, double rightSpeed */) {
                // myRobot.tankDrive(- leftSpeed, - rightSpeed);
        }
}
