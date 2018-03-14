package org.usfirst.frc.team1759.robot.commands;

import edu.wpi.first.wpilibj.Encoder;

import models.Constants;
import models.EncoderInterface;
import models.Point;
import models.TankDriveInterface;
import models.TestableCommandInterface;
import edu.wpi.first.wpilibj.command.Command;

public class GoEncoder extends Command {

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
	
	private boolean started = false;

	public GoEncoder(Encoder encoder, TankDriveInterface tank, Point current, Point dest) {
		this.encoder = encoder;
		this.tank = tank;
		this.current = current;
		this.dest = dest;
	}


	/**
	 * Resets the encoder to prepare for another run.
	 */
	// customStart is here because start is not allowed to be run by wpilibj
	// This code is needed to reset the encoder
	public synchronized void customStart() {
		if (started) return;
        // Note: Calling super.start() activates the WPILibJ Scheduler
		// mechanism, which makes testing unfeasible.  Besides, I don't think
		// we need super.start()'s side effects.
		encoder.reset();
		encoder.setDistancePerPulse(distancePerPulse);
		targetDistance = this.current.dist(this.dest);
		System.out.printf("GoEncoder.customStart(): encoder.getDistance() = %.2f, current = %s, dest=%s\n", encoder.getDistance(), this.current, this.dest.toString());
	}

	// 5 > 7 ? print(this doesnt run) : print(this does)
	// speed = something ? value : value
	@Override
    public void execute() {
		if (!started) {
			customStart();
			started = true;
		}
		double speed = 1.0;
		if (distRemaining() < Constants.WAYPOINT_SLOWDOWN_DISTANCE) {
			double calcSpeed = distRemaining() / Constants.WAYPOINT_SLOWDOWN_DISTANCE;
			speed = calcSpeed < Constants.MINIMUM_SPEED ? Constants.MINIMUM_SPEED : calcSpeed;
		}
		System.out.printf("GoEncoder.execute(): distRemaining=%.2f, encoder.getDistance()=%.2f, speed=%.2f\n",
				distRemaining(),encoder.getDistance(), speed);
		tank.tankDrive(speed, speed);
	}

	@Override
	public void setName(String subsystem, String name) {
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean isFinished() {
		boolean result = (distRemaining() < Constants.WAYPOINT_CAPTURE_DISTANCE);
		if (result == true) {
			System.out.printf("GoEncoder.isFinished(): We're done.\n");
		}
		return result;
	}

	/**
	 * Returns the estimated distance remaining between the robot and the
	 * destination location, in feet.
	 */
	public double distRemaining() {
		return Math.abs(targetDistance - encoder.getDistance());
	}
}
