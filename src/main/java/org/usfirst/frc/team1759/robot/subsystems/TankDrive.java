package org.usfirst.frc.team1759.robot.subsystems;

import org.usfirst.frc.team1759.robot.OI;
import org.usfirst.frc.team1759.robot.RobotMap;

import edu.wpi.first.wpilibj.Spark;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

/**
 * This subsystem is used to control the tank drive for 2018bot. Constructor
 * calls multiple CANTalons, then assigns them into SpeedControllerGroups. Can
 * be instantiated as using a gyro or not using a gyro.
 * 
 * @author Aidan Galbreath
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

	public TankDrive() {

		rightFront = new Spark(RobotMap.RIGHT_FRONT_PORT);
		rightBack = new Spark(RobotMap.RIGHT_BACK_PORT);
		leftFront = new Spark(RobotMap.LEFT_FRONT_PORT);
		leftBack = new Spark(RobotMap.LEFT_BACK_PORT);
		left = new SpeedControllerGroup(leftFront, leftBack);
		right = new SpeedControllerGroup(rightFront, rightBack);
		myRobot = new DifferentialDrive(left, right);
	}
	@Override
	public void setName(String subsystem, String name) {

	}

	@Override
	public void initDefaultCommand() {
		myRobot = new DifferentialDrive(left, right);
	}

	public void tankDrive(OI oi) {
		if (oi.joysticksAttached) {
			myRobot.tankDrive(- oi.leftJoystick.getY(), - oi.rightJoystick.getY());
		} else {
			double left = 0;
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
		}
	}
	
	public void tankDrive (double leftSpeed, double rightSpeed) {
		myRobot.tankDrive(- leftSpeed, - rightSpeed);
	}
}
