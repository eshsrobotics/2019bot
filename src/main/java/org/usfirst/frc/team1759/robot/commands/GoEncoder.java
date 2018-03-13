package org.usfirst.frc.team1759.robot.commands;

import edu.wpi.first.wpilibj.Encoder;

import models.Constants;
import models.EncoderInterface;
import models.Point;
import models.TankDriveInterface;
import models.TestableCommandInterface;
import edu.wpi.first.wpilibj.command.Command;

public class GoEncoder extends Command implements TestableCommandInterface {

	Encoder encoder;
	TankDriveInterface tank;
	Point current, dest;

	/**
	 * Our expected distance from the starting point to the destination, in
	 * feet.
	 */
	double targetDistance;
	// There are 1440 pulses per revolution, Diameter is .5 feet.
	double distancePerPulse = Math.PI * .5 / 1440;

	public GoEncoder(Encoder encoder, TankDriveInterface tank, Point current, Point dest) {
		this.encoder = encoder;
		this.tank = tank;
		this.current = current;
		this.dest = dest;
	}


	/**
	 * Resets the encoder to prepare for another run.
	 */
	@Override
	public synchronized void start() {
        // Note: Calling super.start() activates the WPILibJ Scheduler
		// mechanism, which makes testing unfeasible.  Besides, I don't think
		// we need super.start()'s side effects.
		encoder.reset();
		encoder.setDistancePerPulse(distancePerPulse);
		targetDistance = this.current.dist(this.dest);
	}

	// 5 > 7 ? print(this doesnt run) : print(this does)
	// speed = something ? value : value
	@Override
    public void execute() {
		double speed = 1.0;
		if (distRemaining() < Constants.WAYPOINT_SLOWDOWN_DISTANCE) {
			double calcSpeed = distRemaining() / Constants.WAYPOINT_SLOWDOWN_DISTANCE;
			speed = calcSpeed < Constants.MINIMUM_SPEED ? Constants.MINIMUM_SPEED : calcSpeed;
		}
		System.out.printf("GoEncoder.execute(): distRemaining=%.3f, speed=%.3f     \n",
				distRemaining(), speed);
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

	/**
	 * Returns the estimated distance remaining between the robot and the
	 * destination location, in feet.
	 */
	public double distRemaining() {
		return Math.abs(targetDistance - encoder.getDistance());
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
