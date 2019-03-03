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
	private SpeedController arm;
	private SpeedController elbow; 
	private AnalogPotentiometer pot;

	// The scale factor here should be the range of degrees that the elbow potentiometer is capable of.
	//
	// The test potentiometer I have here goes from about 2:00 to about 10:00, so the range is 
	// 8 hours (240 degrees) and the offset is 2 hours (60 degrees).
	private final double SCALE_FACTOR = 240.0;
	private final double OFFSET = 60.0;

    public Arm() {
		arm = new  Spark(RobotMap.ARM_MOVE);
		elbow = new Spark(RobotMap.ELBOW_MOVE);
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
		// u = (current - min) / (max - min)
		double u = (pot.get() - OFFSET) / SCALE_FACTOR;

		// At u = 0.5 we want max power and at 0 = 0 or 1, we want no power.
		double power = 1 - 2 * Math.abs(u - 0.5);
		elbow.set(power);
		System.out.printf("Pot: %.2f / Elbow power: %.2f ", pot.get(), elbow.get());
	}

	public void arm(OI oi) {
		if (oi.joysticksAttached) {
            if (oi.joysticksAttached) {
                int h = 0;
                if (oi.leftJoystick.getTrigger()) {
                    h = 1;
                } else {
                    h = 0;
                }
                arm.set(h);
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
