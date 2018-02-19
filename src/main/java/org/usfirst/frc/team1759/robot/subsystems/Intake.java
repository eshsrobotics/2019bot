package org.usfirst.frc.team1759.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Intake extends Subsystem {

	SpeedControllerGroup intake;
	private double SPEED_MULT = 0.5;
	
	public Intake(WPI_TalonSRX leftWheel, WPI_TalonSRX rightWheel) {
		intake = new SpeedControllerGroup(leftWheel, rightWheel);
	}
	
	public void takeIn(double speed) {
		intake.set(speed);
	}
	
	public void takeIn() {
		intake.set(SPEED_MULT);
	}
	
	public void pushOut(double speed) {
		intake.set(-1.0 * speed);
	}
	
	public void pushOut() {
		intake.set(-1.0 * SPEED_MULT);
	}
	
	public void stop() {
		intake.set(0);
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
