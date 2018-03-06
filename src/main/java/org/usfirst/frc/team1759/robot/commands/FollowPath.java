package org.usfirst.frc.team1759.robot.commands;

import java.util.List;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import models.EncoderInterface;
import models.Node;
import models.TankDriveInterface;
import models.TestableCommandInterface;

public class FollowPath extends CommandGroup implements TestableCommandInterface  {

        public FollowPath(EncoderInterface encoder, TankDriveInterface tankDrive, Node currentNode, List<Node> path, Command callback) {
                boolean firstNode = true;
                for (Node destNode : path) {
                        if (firstNode) {
                                firstNode = false;
                                continue;
                        }
                        GoEncoder go = new GoEncoder(encoder, tankDrive, currentNode.point, destNode.point);
                        TurnCommand turnCommand = new TurnCommand(tankDrive);
                        turnCommand.setHeading(currentNode.point.vectorTo(destNode.point));
                        addSequential(turnCommand);
                        addSequential(go);
                        currentNode = destNode;
                }
                addSequential(callback);

        }

        @Override
        public void setName(String subsystem, String name) {

        }

        /**
         * This is only here to prevent
         * {@link CommandGroup#start() CommandGroup.start()} from being called, as
         * that would invoke the actual scheduler during unit testing, introducing
         * a slew of new runtime dependencies (like libntcore.so.)
         *
         * WPIlibJ's Command hierarchy was not designed with unit testing in mind.
         */
    @Override
    public void start() {

        // Quandary: The empty implementation here is obviously preventing the
        // CommandGroup from running, which means that execute() is a no-op.
        // But if I *do* call super.start(), then the NetworkTables
        // requirements cause depenency hell to break loose.
        //
        // Solution: The fake scheduler will not use FollowPath.  Instead, it
        // will add the list of commands one by one itself, the same way the
        // FollowPath constructor does.
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
    public boolean isCommandRunning() {
        return isRunning();
    }

    @Override
    public boolean isCommandFinished() {
        return isFinished();
    }
}
