package org.usfirst.frc.team1759.robot.commands;

import java.util.List;

import models.EncoderInterface;
import models.Node;
import models.TankDriveInterface;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class FollowPath extends CommandGroup {
	
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

}
