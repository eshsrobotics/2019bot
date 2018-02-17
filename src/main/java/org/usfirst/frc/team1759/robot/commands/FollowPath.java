package org.usfirst.frc.team1759.robot.commands;

import java.util.List;

import models.Node;

import org.usfirst.frc.team1759.robot.subsystems.TankDrive;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class FollowPath extends CommandGroup {
	
	public FollowPath(TankDrive tankDrive, List<Node> path) {
		for (Node node : path) {
			Go go = new Go(tankDrive);
			go.setDest(node.point);
			TurnCommand turnCommand = new TurnCommand(tankDrive);
			turnCommand.setHeading(go.setHeading());
			addSequential(turnCommand);
			addSequential(go);
		}
	}

	@Override
	public void setName(String subsystem, String name) {
		
	}

}
