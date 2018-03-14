/**
 *
 */
package models;

import edu.wpi.first.wpilibj.command.Command;

/**
 * The WPILibJ {@link Command} class is designed so that its most important
 * functions ({@link Command#start() start()}, {@link Command#execute()
 * execute()}, and {@link Command#isFinished() isFinished()}) are protected,
 * and can only be accessed from within the same package.  But our waypoint
 * simulator needs to run these commands, too.
 *
 * The compromise is implementing an interface whose only purpose is to take
 * our own commands' protected functions and make them public again so that we
 * can test our own code.
 *
 * @author uakotaobi
 *
 */
public interface TestableCommandInterface {

    /**
     * This function must call your Command's {@link Command#start() start()}
     * method.
     */
    public void startCommand();

    /**
     * This function must call your Command's
     * {@link Command#execute() execute()} method.
     */
    public void executeCommand();


    /**
     * This function must return the result from calling your Command's
     * {@link Command#isFinished() isFinished()} method.
     */
    public boolean isCommandFinished();

    /**
     * This function will return the real command that TestableCommandInterface is wrapping around
     * 
     */
    public Command getCommand();
}
