package org.usfirst.frc.team1759.robot.subsystems;

import org.usfirst.frc.team1759.robot.OI;
import org.usfirst.frc.team1759.robot.RobotMap;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Climber extends Subsystem {
	
	Spark leftClimb;
	Spark rightClimb;
	
	public Climber() {
		leftClimb = new Spark(RobotMap.CLIMBER_PORT_1);
		rightClimb = new Spark(RobotMap.CLIMBER_PORT_2);
	}
	
	public void climb(OI oi) {
		if (oi.climbUp.get()) {
			leftClimb.set(1.0);
			rightClimb.set(1.0);
		} else if (oi.climbDown.get()) {
			leftClimb.set(-1.0);
			rightClimb.set(-1.0);
		} else {
			leftClimb.set(0);
			rightClimb.set(0);
		}
	}
	@Override
	public void setName(String subsystem, String name) {

		
	}

	@Override
	protected void initDefaultCommand() {

		
	}

}
