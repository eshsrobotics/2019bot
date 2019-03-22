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
        private SpeedController wrist1;
        private SpeedController wrist2;
        private SpeedController elbow;
        private AnalogPotentiometer pot;
        static double[] DESIRED_ELBOW_POSITIONS = { 0.27, 0.35, 0.4, 0.44, 0.5, 0.6, 0.7 }; // These are potentiometer values.
        static int currentPosition = 0;

        // PID invariants.
        final static double secondsPerIteration = 0.02; //assumption

        // PID constants for the elbow.
        final static double kI = 0;
        final static double kD = 0;
        final static double kP = 30;

        // PID member variables for the elbow.
        private double integral = 0;
        private double previousError = 0;
        private double desiredPosition = DESIRED_ELBOW_POSITIONS[0];

        // This is used to provide smooth manual movement for the elbow and wrist.
        private Timer timer;

        // Elbow manual movement parameters.
        final static double ELBOW_INCREMENT_INTERVAL_SECONDS = 0.05;
        final static double ELBOW_POWER_INCREMENT_PER_INTERVAL = 0.08;
        final static double ELBOW_SLOW_DOWN_INCREMENT_PER_LEVEL = 0.16;
        final static double ELBOW_POWER_INITIAL_RAMP = 0.2;
        final static double ELBOW_MAX_DOWNWARD_POWER = 0.8;
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
        private final static double MIN_INPUT_POT_VALUE = 0.26;
        private final static double MAX_INPUT_POT_VALUE = 0.72;

        // The test potentiometer I have here goes from about 4:00 (120 degrees) to
        // about 8:00 (240 degrees).
        private final static double MIN_OUTPUT_POT_VALUE = 30; // Measured from vertical
        private final static double MAX_OUTPUT_POT_VALUE = 150;
        private final static double SCALE_FACTOR = (MAX_OUTPUT_POT_VALUE - MIN_OUTPUT_POT_VALUE)
                / (MAX_INPUT_POT_VALUE - MIN_INPUT_POT_VALUE);
        private final double OFFSET = MIN_OUTPUT_POT_VALUE;

        public Arm() {
                wrist1 = new Spark(RobotMap.WRIST_MOVE_ONE);
                wrist2 = new Spark(RobotMap.WRIST_MOVE_TWO);
                elbow = new Spark(RobotMap.ELBOW_MOVE);
                pot = new AnalogPotentiometer(RobotMap.TEST_POTENTIOMETER, 1.0, 0.0);
                //pot = new AnalogPotentiometer(RobotMap.TEST_POTENTIOMETER, SCALE_FACTOR, OFFSET);
                timer = new Timer();
                timer.start();
                resetPID(0.5);
        }

        @Override
        public void setName(String subsystem, String name) {

        }

        @Override
        public void initDefaultCommand() {
                // myRobot = new DifferentialDrive(left, right);
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
                                //System.out.printf("Decreased power to %.2f\n", speed);
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
                              //  System.out.printf("Power decaying to %.2f\n", speed);
                        }
                }
                motor.set(speed);
        }
        /**
         * This function is meant to be using during manual control; PID control will smooth the motor
         * response on its own.
         */

        public void resetPID(double desiredPosition) {
                integral = desiredPosition;
                previousError = desiredPosition;
                this.desiredPosition = desiredPosition;
        }
        /**
         * Sets out PID goal position to the given index in the DESIRED_ELOBW_POSITIONS array.
         *          * 
         * Note that this does not actually cause PID movement!  You must hold down a button to override
         * the manual controls and effect PID movement to the desired position.
         * 
         * @param position The index of the desired position in the hard coded array of encoder values.
         */
        private void setDesiredPosition(int position) { 
                if (position >= DESIRED_ELBOW_POSITIONS.length) {
                        position = DESIRED_ELBOW_POSITIONS.length - 1;
                } else if (position < 0) {
                        position = 0;
                } 
                System.out.println("\nGoing to " + DESIRED_ELBOW_POSITIONS[position] + "\n");
                resetPID(DESIRED_ELBOW_POSITIONS[position]);
        }

        /**
         * Goes up to the previous position in the DESIRED_ELBOW_POSITIONS array, in effect causing the arm to move to its
         * previous designated position.
         */
        private void goUpOnePosition() {
                currentPosition ++;
                if (currentPosition >= DESIRED_ELBOW_POSITIONS.length) {
                        currentPosition = DESIRED_ELBOW_POSITIONS.length - 1;
                }
                setDesiredPosition(currentPosition);
        }

        /**
         * Goes down to the next position in the DESIRED_ELBOW_POSITIONS array, in effect causing the arm
         * to move to its next designated position.
         */
        private void goDownOnePosition() {
                currentPosition --;
                if (currentPosition < 0) {
                        currentPosition = 0;
                } 
                setDesiredPosition(currentPosition);
        }

        /**
         * Moves the elbow motor up (in the opposite direction of gravity.)
         * 
         * This is only used during manual control.
         */
        private void moveElbowUp() {
                adjustMotor(elbow, +1, ELBOW_INCREMENT_INTERVAL_SECONDS,
                            ELBOW_POWER_INCREMENT_PER_INTERVAL, 0.2, ELBOW_POWER_INITIAL_RAMP,
                            -ELBOW_MAX_DOWNWARD_POWER, 1.0);
        }

        /**
         * Moves the elbow down (in the same direction as gravity.)
         * 
         * This is only used during manual control.
         */
        private void moveElbowDown() {
                adjustMotor(elbow, -1, ELBOW_INCREMENT_INTERVAL_SECONDS,
                            ELBOW_SLOW_DOWN_INCREMENT_PER_LEVEL, 0.2, ELBOW_POWER_INITIAL_RAMP,
                            -ELBOW_MAX_DOWNWARD_POWER, 1.0);
        }

        /**
         * Whether the elbow is moving up or down, this function brings it to a gradual halt.
         * 
         * This is only used during manual control (specifically, when the throttle is released.)
         */
        private void stopElbowGradually() {
                adjustMotor(elbow, 0, ELBOW_INCREMENT_INTERVAL_SECONDS,
                            ELBOW_SLOW_DOWN_INCREMENT_PER_LEVEL, ELBOW_SLOW_DOWN_INCREMENT_PER_LEVEL, ELBOW_POWER_INITIAL_RAMP,
                            -ELBOW_MAX_DOWNWARD_POWER, 1.0);
        }

        public void arm(OI oi) {
                if (oi.joysticksAttached) {
                        double h = wrist1.get();
                        if (oi.rightJoystick.getRawButton(6)) {
                                h = 0.8;
                        } else if (oi.rightJoystick.getRawButton(4)) {
                                // Take full advantage of gravity.
                                h = -0.35;
                        } else {
                                h = 0;
                        }

                        if (oi.leftJoystick.getRawButton(5)) {
                                moveElbowUp();
                        } else if (oi.leftJoystick.getRawButton(3)) {
                                moveElbowDown();
                        } else {
                                stopElbowGradually();
                        }
                        wrist1.set(h);
                        wrist2.set(-h);

                        
                        
                        /*if (oi.rightJoystick.getRawButtonPressed(3)) {
                                goUpOnePosition();
                        }
                        if (oi.leftJoystick.getRawButtonPressed(4)) {
                                goDownOnePosition();    
                        }
                        if (oi.rightJoystick.getRawButton(3) || oi.leftJoystick.getRawButton(4)) {
                                double error = desiredPosition - this.pot.get();
                                this.integral += error*secondsPerIteration;
                                double derivative = (error-this.previousError)/secondsPerIteration;
                                double desiredPower = (kP*error) + (kI*this.integral) + (kD * derivative);
                                if (desiredPower > 1) {
                                        desiredPower = 1;
                                }
                                if (desiredPower < -1) {
                                        desiredPower = -1;
                                }
                                elbow.set(desiredPower);
                                this.previousError = error;
                        }
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
                         }*/ }
        }

        public void arm(/* double leftSpeed, double rightSpeed */) {
                // myRobot.tankDrive(- leftSpeed, - rightSpeed);
        }
}
