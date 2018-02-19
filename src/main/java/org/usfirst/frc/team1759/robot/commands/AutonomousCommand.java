package org.usfirst.frc.team1759.robot.commands;

import models.Point;

import org.usfirst.frc.team1759.robot.subsystems.TankDrive;

import wrappers.EncoderWrapper;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutonomousCommand extends CommandGroup {
	
	public AutonomousCommand(Encoder encoder, TankDrive tank, Point start, Point dest) {
		
		GoEncoder move = new GoEncoder(new EncoderWrapper(encoder), tank, start, dest);
	}
}
