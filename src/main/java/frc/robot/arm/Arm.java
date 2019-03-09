package frc.robot.arm;

import frc.robot.OI;
import frc.robot.driving.RobotMap;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.AnalogPotentiometer;

/**
 * This subsystem is used to control the arm raising or lowering for 2019bot. 
 * 
 * @author Spencer Moore
 */

public class Arm extends Subsystem{
	private SpeedController wrist;
	private SpeedController elbow; 
	private AnalogPotentiometer pot;

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

		System.out.printf("u = (%.1f-%.1f)/(%.1f-%.1f) = %.2f | desired, actual power = %.2f, %.2f ", 
						pot.get(),
						MIN_OUTPUT_POT_VALUE,
						MAX_OUTPUT_POT_VALUE,
						MIN_OUTPUT_POT_VALUE,
						u,
						power, 
						elbow.get());
	}

	public void arm(OI oi) {
		if (oi.joysticksAttached) {
            if (oi.joysticksAttached) {
				double h = 0;
                if (oi.leftJoystick.getRawButton(3)) {
					h = 0.3;
				} else if (oi.rightJoystick.getRawButton(3)) {
					h = -1;
				} else {
					h = 0;
				}
				double j = 0;
                if (oi.leftJoystick.getRawButton(4)) {
					j = 0.5;
				} else if (oi.rightJoystick.getRawButton(4)) {
					j = -0.5;
				} else {
					j = 0;
				}
				wrist.set(h);
				elbow.set(j);
                //myRobot.tankDrive(- oi.leftJoystick.getY(), - oi.rightJoystick.getY());
            }
			//myRobot.tankDrive(- oi.leftJoystick.getY(), - oi.rightJoystick.getY());
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
