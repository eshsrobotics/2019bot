package org.usfirst.frc.team1759.robot.commands;

import org.usfirst.frc.team1759.robot.subsystems.Arm;
import org.usfirst.frc.team1759.robot.subsystems.Intake;

import edu.wpi.first.wpilibj.command.CommandGroup;
import models.TestableCommandInterface;

public class ExpelCommand extends CommandGroup {
	
	Intake lowerIntake;
	
	public ExpelCommand(Intake lowerIntake) {
		addSequential(new TurnWheels(lowerIntake, true), 1.5);
	}
	
}
