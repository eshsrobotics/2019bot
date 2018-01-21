package org.usfirst.frc.team1759.robot.subsystems;

import org.usfirst.frc.team1759.robot.OI;
import org.usfirst.frc.team1759.robot.RobotMap;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;


/**
 * This subsystem is used to control the tank drive for 2018bot. Constructor calls multiple CANTalons,
 * then assigns them into SpeedControllerGroups. Can be instantiated as using a gyro or not using a gyro.
 * 
 * @author Aidan Galbreath
 */

public class TankDrive extends Subsystem {
	
	DifferentialDrive myRobot;
	WPI_TalonSRX leftFront;
	WPI_TalonSRX leftBack;
	WPI_TalonSRX rightFront;
	WPI_TalonSRX rightBack;
	SpeedControllerGroup left;
	SpeedControllerGroup right;
	RobotMap robotMap;
	
	public TankDrive() {
		rightFront = new WPI_TalonSRX(0);
		rightBack = new WPI_TalonSRX(1);
		leftFront = new WPI_TalonSRX(5);
		leftBack = new WPI_TalonSRX(6);
		left = new SpeedControllerGroup(leftFront, leftBack);
		right = new SpeedControllerGroup(rightFront, rightBack);
	}

	@Override
	public void setName(String subsystem, String name) {
		
	}

	@Override
	public void initDefaultCommand() {
		myRobot = new DifferentialDrive (left, right);
	}
	
	public void tankDrive(OI oi) {
		if (oi.joysticksAttached) {
			myRobot.tankDrive(oi.leftJoystick.getY(), oi.rightJoystick.getY());
		} else {
			double left = 0;
			double right = 0;
			
			if(oi.forward.get()) {
				left = 1;
				right = 1;
			} else if (oi.back.get()) {
				left = -1;
				right = -1;
			} else if (oi.left.get()) {
				left = -1;
				right = 1;
			} else if (oi.right.get()) {
				left =1;
				right = -1;
			}
			if (oi.sneak.get()) {
				left *= 0.5;
				right *= 0.5;
			}
			
			myRobot.tankDrive(left, right);
		}
	}
}
