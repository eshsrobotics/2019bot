package org.usfirst.frc.team1759.robot.commands;

import models.Constants;
import models.Point;

import org.usfirst.frc.team1759.robot.subsystems.TankDrive;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.command.Command;

public class GoEncoder extends Command {

	Encoder encoder;
	TankDrive tank;
	Point current, dest;
	double dist;
	
	public GoEncoder(Encoder encoder, TankDrive tank, Point current, Point dest) {
		this.encoder = encoder;
		this.tank = tank;
		this.current = current;
		this.dest = dest;
	}
	
	// 5 > 7 ? print(this doesnt run) : print(this does)
	// speed = something ? value : value
	public void execute() {
		double speed = 1.0;
		if (distRemaining() < Constants.WAYPOINT_SLOWDOWN_DISTANCE) {
			double calcSpeed = distRemaining() / Constants.WAYPOINT_SLOWDOWN_DISTANCE;
			speed = calcSpeed < Constants.MINIMUM_SPEED ? Constants.MINIMUM_SPEED : calcSpeed; 
		}
		tank.tankDrive(speed, speed);
	}
	
	@Override
	public void setName(String subsystem, String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean isFinished() {
		return distRemaining() < Constants.WAYPOINT_CAPTURE_DISTANCE;
	}
	
	public double distRemaining() { 
		return Math.abs(dist - encoder.getDistance());
	}

}
