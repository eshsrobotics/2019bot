package frc.robot.driving;

import frc.robot.OI;
import frc.robot.Sneak;
import frc.robot.driving.RobotMap;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

/**
 * This subsystem is used to control the tank drive for 2019bot.
 * 
 * @author Spencer Moore
 */

public class TankDrive extends Subsystem {

	DifferentialDrive myRobot;
	SpeedController leftFront;
	SpeedController leftBack;
	SpeedController leftMid;
	SpeedController rightFront;
	SpeedController rightBack;
	SpeedController rightMid;
	SpeedControllerGroup left;
	SpeedControllerGroup right;
	boolean sneaking = false;
	double speedMod = 0.8;
	Sneak sneak;

	public TankDrive(Sneak sneak) {

		rightFront = new Spark(RobotMap.RIGHT_FRONT_PORT);
		rightBack = new Spark(RobotMap.RIGHT_BACK_PORT);
		leftFront = new Spark(RobotMap.LEFT_FRONT_PORT);
		leftBack = new Spark(RobotMap.LEFT_BACK_PORT);
		left = new SpeedControllerGroup(leftFront, leftBack);
		right = new SpeedControllerGroup(rightFront, rightBack);
		myRobot = new DifferentialDrive(left, right);
		this.sneak = sneak;
	}

	@Override
	public void setName(String subsystem, String name) {

	}

	@Override
	public void initDefaultCommand() {
		myRobot = new DifferentialDrive(left, right);
	}

	public void tankDrive(OI oi) {
		double left = 0, right = 0;

		// Joystick controls; optional.
		if (oi.joysticksAttached) {
			left = -oi.rightJoystick.getZ();
			right = oi.leftJoystick.getZ();
			// myRobot.tankDrive(oi.rightJoystick.getZ() * sneak.get(), -oi.leftJoystick.getZ() * sneak.get());
		} 
		
		// Keyboard controls; also optional.
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

		double sneakMultiplier = sneak.get();
		if (sneak.enabled()) {
			if (oi.left.get() || oi.right.get()) {
				sneakMultiplier = RobotMap.DRIVE_TURNING_SNEAK_VALUE;
			} else {
				sneakMultiplier = RobotMap.DRIVE_FORWARD_SNEAK_VALUE;
			}
		}

		myRobot.tankDrive(-left * sneakMultiplier, -right * sneakMultiplier);
	}

	public void tankDrive(double leftSpeed, double rightSpeed) {
		myRobot.tankDrive(-leftSpeed, -rightSpeed);
	}
}
