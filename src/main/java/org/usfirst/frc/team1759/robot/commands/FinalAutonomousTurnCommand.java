/**
 *
 */
package org.usfirst.frc.team1759.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import models.Constants;
import models.TankDriveInterface;
import models.Vector2;

/**
 * A command run at the end of the chain of {@link TurnCommand TurnCommands
 * and {@link GoEncoder} commands we use to navigate the field during
 * autonomous mode.
 *
 * Our final waypoint is always a position just to the north or south of a
 * switch, so this command rotates the robot so it faces that switch directly.
 *
 * @author uakotaobi
 *
 */
public class FinalAutonomousTurnCommand extends Command {

    private TurnCommand turnCommand;
    private Gyro gyro;
    private Vector2 initialDirection;
    private boolean faceLeft;

    /**
     * Initializes this object with all of the information it needs to do its
     * job.
     *
     * @param tankDrive The {@link TankDriveInterface} we'll use to turn the
     *                  robot.
     * @param initialDirection The direction the robot had at the time it
     *                         started autonomous (that is, when
     *                         {@link Gyro.getAngle() its gyro angle} was 0.)
     * @param faceLeft When the robot starts autonomous, it is always pointed
     *                 toward the center of the game field.  The vector 90
     *                 degrees counterclockwise from the robot's
     *                 initialDirection is the "left side of the field" (from
     *                 the drivers' perspective more so than the robot's.)
     *
     *                  When the robot has arrived at its destination, it is
     *                  either just to the left of a switch on the left, or
     *                  just to the right of a switch on the right.  (The same
     *                  would apply for the scale in the center, but we do not
     *                  target the scales during autonomous mode.)  In order to
     *                  shoot, the robot must turn so it faces the opposite side
     *                  of the field from where it currently stands.
     *
     *                  That is where faceLeft comes in.  If it is true, we
     *                  will rotate the robot until its orientation is toward
     *                  the left side of the field; if it is false, we will
     *                  rotate until the robot faces the right side.
     */
    public FinalAutonomousTurnCommand(TankDriveInterface tankDrive, Vector2 initialDirection, boolean faceLeft) {
        this.turnCommand = new TurnCommand(tankDrive);
        this.initialDirection = initialDirection;
        this.faceLeft = faceLeft;
    }

    /**
     * Calculates the direction we need to have and sets our internal
     * {@link TurnCommand} to bring us there.
     */
    @Override
    public synchronized void start() {

        double desiredAngleInRadians = (faceLeft ? -90 : 90) * Constants.DEGREES_TO_RADIANS;
        Vector2 desiredDirection = initialDirection.rotatedBy(desiredAngleInRadians);
        turnCommand.setHeading(desiredDirection);
        turnCommand.start();
    }

    /* (non-Javadoc)
     * @see edu.wpi.first.wpilibj.command.Command#execute()
     */
    @Override
    protected void execute() {
        turnCommand.execute();
    }

    /* (non-Javadoc)
     * @see edu.wpi.first.wpilibj.command.Command#isFinished()
     */
    @Override
    protected boolean isFinished() {
        return turnCommand.isFinished();
    }
}
