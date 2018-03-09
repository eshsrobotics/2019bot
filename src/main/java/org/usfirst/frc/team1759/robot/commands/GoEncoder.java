package org.usfirst.frc.team1759.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import models.Constants;
import models.EncoderInterface;
import models.Point;
import models.TankDriveInterface;
import models.TestableCommandInterface;

public class GoEncoder extends Command implements TestableCommandInterface {

	EncoderInterface encoder;
	TankDriveInterface tank;
	Point current, dest;
	double dist;

	public GoEncoder(EncoderInterface encoder, TankDriveInterface tank, Point current, Point dest) {
		this.encoder = encoder;
		this.tank = tank;
		this.current = current;
		this.dest = dest;
	}

	// 5 > 7 ? print(this doesnt run) : print(this does)
	// speed = something ? value : value
	@Override
    public void execute() {
		encoder.reset();
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

    @Override
    public void startCommand() {
        // No-op because super.start() activates the WPILibJ Scheduler mechanism.
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
