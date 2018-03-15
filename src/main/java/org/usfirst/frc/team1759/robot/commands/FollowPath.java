package org.usfirst.frc.team1759.robot.commands;

import java.util.List;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.Encoder;
import models.Node;
import models.TankDriveInterface;
import models.TestableCommandInterface;
import models.Vector2;

public class FollowPath extends CommandGroup  {
	
		private TestableCommandInterface finalCommand; 
		
        public FollowPath(Encoder encoder, Gyro gyro, TankDriveInterface tankDrive, Vector2 initialRobotDirection, Node currentNode, List<Node> path, Command callback) {
			boolean firstNode = true;
			for (Node destNode : path) {
				if(firstNode) {
					firstNode = false;
					continue;
				}
				GoEncoder goEncoder = new GoEncoder(encoder, tankDrive, currentNode.point, destNode.point);
					
				TurnCommand turnCommand = new TurnCommand(tankDrive, gyro, initialRobotDirection);
					turnCommand.setHeading(currentNode.point.vectorTo(destNode.point));
					addSequential(turnCommand);
					addSequential(goEncoder);
					currentNode = destNode;
			}
			addSequential(callback);
			//finalCommand = callback;
        }

        @Override
        public void setName(String subsystem, String name) {

        }

		public boolean isFinished() {
			// Turns out that a command that hasn't started returns finished.
			// For now, we let teleopPeriodic() kill this command.
			return false;
			// if(finalCommand.isCommandFinished()) {
				// System.out.printf("Final command has finished. \n");
				// return true
			// } else {
				// return false;
			// }
		}
}
