package frc.robot.arm;

import frc.robot.OI;
import frc.robot.Sneak;
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

        // The official heights of (the centers of) our targets are:
        //
        //     19" (loadstation hatch)
        //     19" (rocket hatch, level 1; habitat hatch)
        //     28" (rocket ball,  level 1)
        //     47" (rocket hatch, level 2)
        //     56" (rocket ball,  level 2)
        //     75" (rocket hatch, level 3)
        //     84" (rocket ball,  level 3)
        //
        // Sources: Game manual, pg. 17/125, section 4.3
        //          Game manual, pg. 21/125, section 4.4
        //
        // TODO: Measure elbow/wrist potentiometer positions that correspond
        // to these heights.  (The numbers that you see right now are made-up.)
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

        // Used to slow down the wrist.
        private Sneak sneak;

        // Wrist manual movement parameters.
        final static double INCREMENT_INTERVAL_SECONDS = 0.05;
        final static double WRIST_POWER_INCREMENT_PER_INTERVAL = 0.16;
        final static double WRIST_SLOW_DOWN_INCREMENT_PER_LEVEL = 0.40;
        final static double WRIST_INITIAL_POWER_RAMP_DOWN = 0.3;
        final static double WRIST_INITIAL_POWER_RAMP_UP = 0.6;
        final static double WRIST_MAX_DOWNWARD_POWER = 0.8;
         static double wristMotion = 0.0;

        // Elbow manual movement parameters.
        final static double ELBOW_INCREMENT_INTERVAL_SECONDS = 0.05;
        final static double ELBOW_POWER_INCREMENT_PER_INTERVAL = 0.08;
        final static double ELBOW_SLOW_DOWN_INCREMENT_PER_LEVEL = 0.16;
        final static double ELBOW_POWER_INITIAL_RAMP = 0.2;
        final static double ELBOW_MAX_DOWNWARD_POWER = 0.8;
        final static double EPSILON = 0.05;
        double lastIncrementTimeSeconds = 0;

        static final double ELBOW_GRADUAL_DECAY = 0.9275;
        static final double ELBOW_GRADUAL_ACCELERATION = 0.4;

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

        public Arm(Sneak sneak) {
                
                this.sneak = sneak;
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
                                // System.out.printf("Power decaying to %.2f\n", speed);
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
                adjustMotor(elbow, +1, INCREMENT_INTERVAL_SECONDS,
                            ELBOW_POWER_INCREMENT_PER_INTERVAL, 0.2, 
                            ELBOW_POWER_INITIAL_RAMP,
                            -ELBOW_MAX_DOWNWARD_POWER, 1.0);
        }

        private void moveWristUp() {
                adjustMotor(wrist1, 1, INCREMENT_INTERVAL_SECONDS, 
                        WRIST_POWER_INCREMENT_PER_INTERVAL, WRIST_POWER_INCREMENT_PER_INTERVAL, 
                        WRIST_INITIAL_POWER_RAMP_UP, 
                        -WRIST_MAX_DOWNWARD_POWER, 1.0);
                wrist2.set(-wrist1.get());
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

        private void moveWristDown() {
                adjustMotor(wrist1, -1, INCREMENT_INTERVAL_SECONDS, 
                        WRIST_POWER_INCREMENT_PER_INTERVAL, WRIST_POWER_INCREMENT_PER_INTERVAL, 
                        WRIST_INITIAL_POWER_RAMP_DOWN, 
                        -WRIST_MAX_DOWNWARD_POWER, 1.0);
                wrist2.set(-wrist1.get());
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

        private void stopWristGradually() {        
                adjustMotor(wrist1, 0, INCREMENT_INTERVAL_SECONDS, 
                        WRIST_POWER_INCREMENT_PER_INTERVAL, WRIST_POWER_INCREMENT_PER_INTERVAL, 
                        WRIST_INITIAL_POWER_RAMP_DOWN, 
                        -WRIST_MAX_DOWNWARD_POWER, 1.0);
                wrist2.set(-wrist1.get());       
        }

        /***
         * Instead of just incrementing a motor by a desired value directly, take the average
         * of the incremented value and the delta and set the motor speed to that.
         * 
         * By calling this with a fixed frequency, we can achieve both gradual acceleration
         * and gradual deceleration.
         * 
         * @param speedController The motor to change.
         * @param delta The amount to change the speedController's current speed by.
         * @param decay By what percetage should we dimish per call?  This should be between 0 (instant) and 1 (never slows at all.)
         */
        void gradualChange(SpeedController speedController, double delta, double decay) {
                speedController.set((speedController.get() + delta) * decay * sneak.get());
                System.out.printf("Speed Controller's current value is: %.2f\n", speedController.get());
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
                        wrist1.set(h * sneak.get());
                        wrist2.set(-h * sneak.get());

                        if (oi.rightJoystick.getRawButton(5) || oi.climbUp.get()){
                                moveWristUp();
                        } else if (oi.rightJoystick.getRawButton(3) || oi.climbDown.get()) {
                                // Take full advantage of gravity.
                                moveWristDown();
                        } else {
                                stopWristGradually();
                        }

                        if (oi.leftJoystick.getRawButton(5) || oi.elbowUpButton.get()) {
                                //elbow.set(0.8);
                                gradualChange(elbow, ELBOW_GRADUAL_ACCELERATION, ELBOW_GRADUAL_DECAY);
                        } else if (oi.leftJoystick.getRawButton(3) || oi.elbowDownButton.get()) {
                                //elbow.set(-0.8);
                                gradualChange(elbow, -ELBOW_GRADUAL_ACCELERATION, ELBOW_GRADUAL_DECAY);
                        } else {
                                //elbow.set(0);
                                gradualChange(elbow, 0, ELBOW_GRADUAL_DECAY);
                        }

                        if (oi.leftJoystick.getRawButton(4)) {
                                moveElbowDown();
                        }
                } else {
                        
                        if (oi.climbUp.get()) {
                                wristMotion = 0.5;
                        } else if (oi.climbDown.get()) {
                                wristMotion = -0.5;
                        } else {
                                wristMotion = 0;
                        }
                        wrist1.set(wristMotion);
                        wrist2.set(-wristMotion);

                        if(oi.elbowUpButton.get()) {
                                gradualChange(elbow, ELBOW_GRADUAL_ACCELERATION, ELBOW_GRADUAL_DECAY);
                        } else if (oi.elbowDownButton.get()) {
                                gradualChange(elbow, -ELBOW_GRADUAL_ACCELERATION, ELBOW_GRADUAL_DECAY);
                        } else {
                                gradualChange(elbow, 0, ELBOW_GRADUAL_DECAY);
                        }
                }
        }

        public void arm() {
        }
}
