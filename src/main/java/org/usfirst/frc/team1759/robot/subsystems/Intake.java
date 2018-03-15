package org.usfirst.frc.team1759.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Intake extends Subsystem {

	SpeedControllerGroup leftIntake;
	SpeedControllerGroup rightIntake;
	private double SPEED_MULT = 0.75;
	
	public Intake(WPI_TalonSRX leftWheel, WPI_TalonSRX rightWheel) {
		leftIntake = new SpeedControllerGroup(leftWheel);
		rightIntake = new SpeedControllerGroup(rightWheel);
	}
	
	public Intake(WPI_TalonSRX leftWheel, WPI_TalonSRX rightWheel, WPI_TalonSRX secondaryLeftWheel, WPI_TalonSRX secondaryRightWheel) {
		leftIntake = new SpeedControllerGroup(leftWheel, secondaryLeftWheel);
		rightIntake = new SpeedControllerGroup(rightWheel, secondaryRightWheel);
	}
	
	public void takeIn(double speed) {
		leftIntake.set(speed);
		rightIntake.set(-speed);
	}
	
	public void takeIn() {
		takeIn(SPEED_MULT);
	}
	
	public void pushOut(double speed) {
		leftIntake.set(-speed);
		rightIntake.set(speed);
	}
	
	public void pushOut() {
		pushOut(SPEED_MULT);
	}
	
	public void stop() {
		leftIntake.set(0);
		rightIntake.set(0);
	}

	@Override
	public void setName(String subsystem, String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub
		
	}
}
