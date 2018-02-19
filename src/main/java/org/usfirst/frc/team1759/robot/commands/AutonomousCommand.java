package org.usfirst.frc.team1759.robot.commands;

import models.Point;

import org.usfirst.frc.team1759.robot.subsystems.TankDrive;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutonomousCommand extends CommandGroup {
	
	public AutonomousCommand(Encoder encoder, TankDrive tank, Point start, Point dest) {
		
		GoEncoder move = new GoEncoder(encoder, tank, start, dest);
	}
}
