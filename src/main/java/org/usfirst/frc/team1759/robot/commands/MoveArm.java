package org.usfirst.frc.team1759.robot.commands;

import org.usfirst.frc.team1759.robot.subsystems.Arm;

import edu.wpi.first.wpilibj.command.Command;

public class MoveArm extends Command {

	Arm arm;
	boolean up;

	public MoveArm(Arm arm, boolean up) {
		this.arm = arm;
		this.up = up;
	}

	public void execute() {
		if (up) {
			arm.raise();
		} else {
			arm.lower();
		}
	}

	public void end() {
		arm.stop();
	}

	@Override
	public void setName(String subsystem, String name) {
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return false;
	}

}
