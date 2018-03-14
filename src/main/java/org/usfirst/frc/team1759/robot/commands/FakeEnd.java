package org.usfirst.frc.team1759.robot.commands;
import edu.wpi.first.wpilibj.command.Command;
import models.TestableCommandInterface;

/**
 * This is used so that we can text autonomous without something attached as a launcher.
 */
public class FakeEnd extends Command implements TestableCommandInterface {
	
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
	
	@Override
	public void startCommand() {
		start();
	}
	
	@Override
	public void executeCommand() {
		execute();
	}
	
	@Override
	public boolean isCommandFinished() {
		return isFinished();
	}
	
	@Override
	public Command getCommand() {
		return (Command) this;
	}
}
