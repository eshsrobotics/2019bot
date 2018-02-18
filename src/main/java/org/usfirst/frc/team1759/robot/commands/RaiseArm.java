package org.usfirst.frc.team1759.commands;

import org.usfirst.frc.team1759.robot.subsystems.Arm;

import edu.wpi.first.wpilibj.command.Command;

public class RaiseArm extends Command {

	Arm arm;
	
	protected void initialize() {
		setTimeout(3);
	}
	public RaiseArm(Arm arm) {
		this.arm = arm;
	}
	
	protected void execute() {
		arm.raise();
	}
	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return false;
	}

}
