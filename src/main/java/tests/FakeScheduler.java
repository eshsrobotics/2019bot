package tests;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.command.Command;
/**
 * 
 * From a programming perspective, this should function very similarly to how the Scheduler class, as found at 
 * http://first.wpi.edu/FRC/roborio/release/docs/java/edu/wpi/first/wpilibj/command/Scheduler.html, does, but will respond
 * based solely off of our input.
 * @author Aidan Galbreath
 *
 */
public class FakeScheduler {
	/*
	 * This is the command that is currently being run.
	 */
	Command currentCommand;
	/*
	 * This is the array list of commands. When the .run() method is initiated, the scheduler will begin running each command sequentially.
	 */
	ArrayList<Command> commandList;
	
	/**
	 * Adds the given command to the command list. This operates on a first in, first out thought process. Commands should be added in the order
	 * you wish them to be executed.
	 * @param command
	 */
	public void add(Command command) {
		commandList.add(command);
	}
	
	/**
	 * Adds the given to command to the command list at the given index. This is used if you wish to add commands out of order. This is not the
	 * prefereed method of adding commands, but may be used if needed.
	 * @param index
	 * @param command
	 */
	public void add(int index, Command command) {
		commandList.add(index, command);
	}
	
	public void run() {
		for (int i = 0; i < commandList.size(); i++) {
				currentCommand.start();
				currentCommand = commandList.get(i + 1);
		}
	}
}
