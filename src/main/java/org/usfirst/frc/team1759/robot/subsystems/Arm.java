package org.usfirst.frc.team1759.robot.subsystems;

import org.usfirst.frc.team1759.robot.RobotMap;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Arm extends Subsystem {

	DoubleSolenoid arm;
	
	public Arm(DoubleSolenoid arm) {
		this.arm = arm;
	}
	
	public void raise() {
		arm.set(DoubleSolenoid.Value.kReverse);
	}
	public void lower() {
		arm.set(DoubleSolenoid.Value.kForward);
	}
	public void stop() {
		arm.set(DoubleSolenoid.Value.kOff);
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
