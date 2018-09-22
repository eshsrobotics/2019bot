package org.usfirst.frc.team1759.robot.subsystems;

import org.usfirst.frc.team1759.robot.OI;
import org.usfirst.frc.team1759.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.DoubleSolenoid;

public class Arm extends Subsystem {
	
	DoubleSolenoid arm;
	
	public Arm() {
		arm = new DoubleSolenoid(RobotMap.LAUNCHER_SOLENOID_OUT, RobotMap.LAUNCHER_SOLENOID_IN);
	}
	
	public void raiseArm() {
		arm.set(DoubleSolenoid.Value.kForward);
	}
}