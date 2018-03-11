package org.usfirst.frc.team1759.robot.commands;
import edu.wpi.first.wpilibj.command.Command;

/**
 * This is used so that we can text autonomous without something attached as a launcher.
 */
public class FakeEnd extends Command {
	
	public void FakeEnd() {
		
	}

	@Override
	public boolean isFinished() {
		return true;
	}

	@Override
	public void execute() {
	
	}
	
	@Override
	public void start() {

	}
}
