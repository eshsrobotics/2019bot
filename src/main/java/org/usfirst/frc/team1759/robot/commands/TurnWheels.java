package org.usfirst.frc.team1759.robot.commands;

import org.usfirst.frc.team1759.robot.subsystems.Intake;

import edu.wpi.first.wpilibj.command.Command;

public class TurnWheels extends Command {

	Intake intake;
	boolean reversed;
	
	public TurnWheels(Intake intake, boolean reversed) {
		this.intake = intake;
		this.reversed = reversed;
	}
	
	public void execute() {
		if(reversed) {
			intake.pushOut();
		} else {
			intake.takeIn();
		}
	}
	
	public void end() {
		intake.stop();
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
