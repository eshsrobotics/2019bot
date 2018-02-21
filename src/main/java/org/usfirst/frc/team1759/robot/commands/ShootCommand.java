package org.usfirst.frc.team1759.robot.commands;

import org.usfirst.frc.team1759.robot.subsystems.Launcher;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Command;

public class ShootCommand extends Command {

	Launcher launcher;
	public ShootCommand(Launcher launcher) {
		this.launcher = launcher;
		launcher.launcher.set(DoubleSolenoid.Value.kForward);
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
	
	public void end() {
		launcher.launcher.set(DoubleSolenoid.Value.kReverse);
	}
	
	

}
