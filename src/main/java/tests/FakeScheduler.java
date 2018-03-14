package tests;

import java.util.ArrayList;
import java.util.ListIterator;

import models.TestableCommandInterface;
import edu.wpi.first.wpilibj.command.Command;
/**
 *
 * From a programming perspective, this should function very similarly to how
 * the Scheduler class, as found at
 * http://first.wpi.edu/FRC/roborio/release/docs/java/edu/wpi/first/wpilibj/command/Scheduler.html,
 * does, but will respond based solely off of our input.
 *
 * @author Aidan Galbreath
 *
 */
public class FakeScheduler {

        /**
         * The only constructed instance of the {@link FakeScheduler} object.
         */
        static private FakeScheduler theInstance = null;

        /**
         * Singletons require private constructors.
         */
        private FakeScheduler() {
            commandList = new ArrayList<TestableCommandInterface>();
            commandIterator = null;
        }

        /**
         * This iterator allows us to advance through the list of commands we
         * need to run.
         */
        private ListIterator<TestableCommandInterface> commandIterator;

        /**
         * This is the array list of commands. When the
         * {@link FakeScheduler#run() run()} method is initiated, the scheduler
         * will begin running the list of commands sequentially.
         */
        private ArrayList<TestableCommandInterface> commandList;

        private TestableCommandInterface currentCommand;

        /**
         * Adds the given command to the command list. This operates on a first in, first out thought process. Commands should be added in the order
         * you wish them to be executed.
         * @param command
         */
        public void add(TestableCommandInterface command) {
                commandList.add(command);
        }

        /**
         * Adds the given to command to the command list at the given
         * index. This is used if you wish to add commands out of order. This
         * is not the preferred method of adding commands, but may be used if
         * needed.
         *
         * @param index
         * @param command
         */
        public void add(int index, TestableCommandInterface command) {
                commandList.add(index, command);
        }

        /**
         * The scheduler is a singleton,  This function retrieves the one and
         * only instance of this class.
         */
        static public FakeScheduler getInstance() {
            if (theInstance == null) {
                theInstance = new FakeScheduler();
            }
            return theInstance;
        }


        /**
         * Allows the added {@link Command commands} to run anew.
         *
         * Note that until {@link FakeScheduler#disable() disable()} is called,
         * attempts to add new {@link Command Commands} to the scheduler will
         * throw exceptions (since an iterator will be active on the
         * scheduler's content.)
         */
        public void enable() {
            commandIterator = commandList.listIterator();
            currentCommand = null;
        }

        /**
         * Disables execution so that new commands can be added.
         */
        public void disable() {
            commandIterator = null;
            currentCommand = null;
        }

        /**
         * Execute the current (sequential) command in our list incrementally.
         */
        public void run() {
        	if (commandIterator == null) {
        		return;
        	}

            if (currentCommand == null || currentCommand.isCommandFinished()) {

                if (!commandIterator.hasNext()) {
                    // We've reached the end of the command list.
                    disable();
                    return;
                }

                currentCommand = commandIterator.next();
                currentCommand.startCommand();
            } else {
                currentCommand.executeCommand();
            }

            // System.out.printf("Current command: %s  \n", currentCommand.getName());
        }
}
