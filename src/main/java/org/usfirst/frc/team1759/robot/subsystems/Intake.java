package org.usfirst.frc.team1759.robot.subsystems;

import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Intake extends Subsystem {

	SpeedControllerGroup leftIntake;
	SpeedControllerGroup rightIntake;
	
	//Direction is a multiplier to set the current direction for different intakes
	private double direction = 1.0;
	private final double SPEED_MULT = 0.50;
	
	public Intake(SpeedController leftWheel, SpeedController rightWheel) {
		leftIntake = new SpeedControllerGroup(leftWheel);
		rightIntake = new SpeedControllerGroup(rightWheel);
	}
	
	public Intake(SpeedController leftWheel, SpeedController rightWheel, SpeedController secondaryLeftWheel, SpeedController secondaryRightWheel) {
		//direction = -1;	
		leftIntake = new SpeedControllerGroup(leftWheel, secondaryLeftWheel);
		rightIntake = new SpeedControllerGroup(rightWheel, secondaryRightWheel);
	}
	
	public void takeIn(double speed) {
		leftIntake.set(speed * direction);
		rightIntake.set(-speed * direction);
	}
	
	public void takeIn() {
		takeIn(SPEED_MULT);
	}
	
	public void pushOut(double speed) {
		leftIntake.set(-speed * direction);
		rightIntake.set(speed * direction);
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
