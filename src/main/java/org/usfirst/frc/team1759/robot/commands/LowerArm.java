package org.usfirst.frc.team1759.robot.commands;

import org.usfirst.frc.team1759.robot.subsystems.Arm;

import edu.wpi.first.wpilibj.command.Command;

public class LowerArm extends Command{

	Arm arm;
	
	protected void initialize() {
		setTimeout(3);
	}
	
	public LowerArm(Arm arm) {
		this.arm = arm;
	}
	
	protected void execute() {
		arm.lower();
	}
	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return false;
	}

}
