package org.usfirst.frc.team1759.commands;

import org.usfirst.frc.team1759.robot.subsystems.Arm;
import org.usfirst.frc.team1759.robot.subsystems.Intake;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class ExpelCommand extends CommandGroup {
	
	Intake lowerIntake;
	Arm arm;
	
	public ExpelCommand(Intake lowerIntake, Arm arm) {
		addSequential(new MoveArm(arm, false), 1);
		addSequential(new TurnWheels(lowerIntake, true), 1.5);
	}
}
