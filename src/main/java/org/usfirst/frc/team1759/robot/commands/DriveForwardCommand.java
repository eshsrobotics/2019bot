package org.usfirst.frc.team1759.robot.commands;

import edu.wpi.first.wpilibj.command.Command;

import org.usfirst.frc.team1759.robot.subsystems.TankDrive;

public class DriveForwardCommand extends Command {
	TankDrive tankDrive;
	
	public DriveForwardCommand() {
		requires(tankDrive);
	}
	
	public void drive(TankDrive tankDrive, double seconds) {
		tankDrive.autoDrive(1.0, 1.0);
		setTimeout(seconds);
	}
	
	@Override
	public void setName(String subsystem, String name) {
		
	}

	@Override
	protected boolean isFinished() {
		return false;
	}

}
