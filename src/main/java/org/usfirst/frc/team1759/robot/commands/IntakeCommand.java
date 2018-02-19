package org.usfirst.frc.team1759.robot.commands;

import org.usfirst.frc.team1759.robot.subsystems.Arm;
import org.usfirst.frc.team1759.robot.subsystems.Intake;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class IntakeCommand extends CommandGroup {
	
	Intake upperIntake;
	Intake lowerIntake;
	Arm arm;
	
	public IntakeCommand(Intake upperIntake, Intake lowerIntake, Arm arm) {
		addSequential(new MoveArm(arm, false), 1);
		addParallel(new TurnWheels(lowerIntake, true), 1.5);
		addParallel(new TurnWheels(upperIntake, true), 2);
		addSequential(new MoveArm(arm, true), 1);
	}
	
	public IntakeCommand(Intake lowerIntake, Arm arm) {
		addSequential(new MoveArm(arm, false), 1);
		addParallel(new TurnWheels(lowerIntake, true), 1.5);
		addSequential(new MoveArm(arm, true), 1);
	}
}
