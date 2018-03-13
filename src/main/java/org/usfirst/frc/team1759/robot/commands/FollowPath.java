package org.usfirst.frc.team1759.robot.commands;

import java.util.List;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import models.EncoderInterface;
import models.Node;
import models.TankDriveInterface;
import models.TestableCommandInterface;
import models.Vector2;

public class FollowPath extends CommandGroup implements TestableCommandInterface  {

        public FollowPath(EncoderInterface encoder, Gyro gyro, TankDriveInterface tankDrive, Vector2 initialRobotDirection, Node currentNode, List<Node> path, Command callback) {
                boolean firstNode = true;
                for (Node destNode : path) {
                        if (firstNode) {
                                firstNode = false;
                                continue;
                        }
                        GoEncoder go = new GoEncoder(encoder, tankDrive, currentNode.point, destNode.point);
                        
			TurnCommand turnCommand = new TurnCommand(tankDrive, gyro, initialRobotDirection);
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
}
