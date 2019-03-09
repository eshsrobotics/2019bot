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
	final static double ARM_INCREMENT_INTERVAL_SECONDS = 0.05;
	final static double ELBOW_POWER_INCREMENT_PER_INTERVAL = 0.02;
	final static double ELBOW_POWER_INITIAL_RAMP = 0.2;
	final static double ELBOW_MAX_DOWNWARD_POWER = 0.3;
	final static double EPSILON = 0.05;
	double lastIncrementTimeSeconds = 0;

	// The scale factor here should be the range of degrees that the elbow potentiometer is capable of.
	//
	// When constucting the AnalogPotentiometer with a scale factor of 1.0 and an offset of 0.0, this
	// flaky pot we're testing with gives us values between 0 and 0.27.  That's probably
	// not the correct value, but whatever; we can work around that in software.
	//
	// Update: The potentiometer had a resistance in the M
	private final static double MIN_INPUT_POT_VALUE = 0;
	private final static double MAX_INPUT_POT_VALUE = 1.0; // 0.27;

	// The test potentiometer I have here goes from about 4:00 (120 degrees) to about 8:00 (240 degrees).
	private final static double MIN_OUTPUT_POT_VALUE = 120;
	private final static double MAX_OUTPUT_POT_VALUE = 240;
	private final static double SCALE_FACTOR = (MAX_OUTPUT_POT_VALUE - MIN_OUTPUT_POT_VALUE) / (MAX_INPUT_POT_VALUE - MIN_INPUT_POT_VALUE);
	private final double OFFSET = MIN_OUTPUT_POT_VALUE;	

    public Arm() {
		wrist = new  Spark(RobotMap.WRIST_MOVE);
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
		//myRobot = new DifferentialDrive(left, right);
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

		// At u = 0.5 we want no power, and at u = 0 or 1, we want max power (in opposite directions.)
		double power = 2 * (u - 0.5);		
		//elbow.set(power);

		// System.out.printf("u = (%.1f-%.1f)/(%.1f-%.1f) = %.2f | desired, actual power = %.2f, %.2f ", 
		// 				pot.get(),
		// 				MIN_OUTPUT_POT_VALUE,
		// 				MAX_OUTPUT_POT_VALUE,
		// 				MIN_OUTPUT_POT_VALUE,
		// 				u,
		// 				power, 
		// 				elbow.get());
	}

	public void arm(OI oi) {
		if (oi.joysticksAttached) {
			double h = wrist.get();
			if (oi.leftJoystick.getRawButton(3)) {
				h = 0.3;
			} else if (oi.rightJoystick.getRawButton(3)) {
				h = -0.3; //-1;
			} else {
				h = 0;
			}
			double j = elbow.get();
			if (oi.leftJoystick.getRawButton(4)) {
				// Slow increment every few intervals.
				if (timer.get() - lastIncrementTimeSeconds > ARM_INCREMENT_INTERVAL_SECONDS) {
					lastIncrementTimeSeconds = timer.get();
					if (Math.abs(j) < EPSILON) {
						j = ELBOW_POWER_INITIAL_RAMP;
					} else {
						j += ELBOW_POWER_INCREMENT_PER_INTERVAL;
					}
					if (j > 1) {
						j = 1;
					}
					System.out.printf("Increased elbow power to %.2f\n", j);
				}
			} else if (oi.rightJoystick.getRawButton(4)) {
				// Slow decrement every few intervals.
				if (timer.get() - lastIncrementTimeSeconds > ARM_INCREMENT_INTERVAL_SECONDS) {
					lastIncrementTimeSeconds = timer.get();
					if (Math.abs(j) < EPSILON) {
						j = -ELBOW_POWER_INITIAL_RAMP;
					} else {
						j -= ELBOW_POWER_INCREMENT_PER_INTERVAL;
					}
					// Due to gravity, this does not need to be as rapid as the upward movement.
					if (j < -ELBOW_MAX_DOWNWARD_POWER) {
						j = -ELBOW_MAX_DOWNWARD_POWER;
					}
					System.out.printf("Decreased elbow power to %.2f\n", j);
				}
			} else {
				// Speed decays when there's no set power...but we don't instantly stop (the arm jerks badly.)
				// j = 0;
				if (timer.get() - lastIncrementTimeSeconds > ARM_INCREMENT_INTERVAL_SECONDS) {
					lastIncrementTimeSeconds = timer.get();
					j -= (Math.signum(j) * ELBOW_POWER_INCREMENT_PER_INTERVAL);
					if (Math.abs(j) < EPSILON) {
						j = 0;
					} 
					System.out.printf("Wrist power decaying to %.2f\n", j);
				}
			}
			wrist.set(h);
			elbow.set(j);
		} else {
			/*double left = 0;
			double right = 0;

			if (oi.forward.get()) {
				left = 1;
				right = 1;
			} else if (oi.back.get()) {
				left = -1;
				right = -1;
			} else if (oi.left.get()) {
				left = -1;
				right = 1;
			} else if (oi.right.get()) {
				left = 1;
				right = -1;
			}
			if (oi.sneak.get()) {
				if (oi.left.get() || oi.right.get()) {
					left *= 0.8;
					right *= 0.8;
				} else {
					left *= 0.5;
					right *= 0.5;
				}
			}

			myRobot.tankDrive(- left, - right);			
		*/}
	}
	
	public void arm (/*double leftSpeed, double rightSpeed*/) {
		//myRobot.tankDrive(- leftSpeed, - rightSpeed);
	}
}
