package org.usfirst.frc.team1759.robot.commands;

import models.Constants;
import models.Point;
import models.Vector2;

import org.usfirst.frc.team1759.robot.Sensors;
import org.usfirst.frc.team1759.robot.subsystems.TankDrive;

import edu.wpi.first.wpilibj.command.Command;

public class Go extends Command {
	
	/**
	 * This point is the point that the robot is at when called upon; initially has a value of initialPoint.
	 */
	Point currentPoint;
	/**
	 * This is the point that the robot is at when the command begins. By default, this is set to the origin.
	 */
	Point initialPoint;
	/**
	 * This is the destination point. This is set by the function setDestination(). By default, this is the origin.
	 */
	Point dest;
	/**
	 * This is the heading to reach the destination point. This is calculated by the function setHeading().
	 */
	Vector2 heading;
	/**
	 * This vector is generated from the initial and destination points. It is then added to the initial point to generate the currentPoint.
	 */
	Vector2 changeVector;
	Sensors sense;
	TankDrive tank;
	
	double distance;
	
	double accelX;
	double accelY;
	double initAccelX = 0;
	double initAccelY = 0;
	double prevAccelX;
	double prevAccelY;
	
	double positionX;
	double positionY;
	double initPositionX = 0;
	double initPositionY = 0;
	double prevPositionX;
	double prevPositionY;
	
	double veloX;
	double veloY;
	double initVeloX = 0;
	double initVeloY = 0;
	double prevVeloX;
	double prevVeloY;
	
	double timeInit;
	double timeNow;
	double prevTime;
	long timeInMilli;
	long timeInitInMilli;
	
	static final double range = 0.2;

	/**
	 * This initializes the Go command.
	 * @param tank
	 */
	public Go(TankDrive tank) {
		tank = new TankDrive();
		initialPoint = Constants.ORIGIN;
		currentPoint = initialPoint;
		sense = new Sensors();
		timeInitInMilli = System.currentTimeMillis();
		timeInit = timeInitInMilli * Constants.MILLI_TO_SEC;
		prevAccelX = initAccelX;
		prevAccelY = initAccelY;
		prevPositionX = initPositionX;
		prevPositionY = initPositionY;
		prevTime = timeInit;
		prevVeloX = initVeloX;
		prevVeloY = initVeloY;
	}
	
	/**
	 * This function sets the destination point.
	 * @param dest
	 * @return
	 */
	public Point setDest(Point dest) {
		dest = new Point(dest.x, dest.y);
		return dest;
	}
	
	/**
	 * This function finds the vector that stretches between two points: The current point and the destination point.
	 * @return
	 */
	public Vector2 setHeading() {
		heading = currentPoint.vectorTo(dest);
		return heading;
	}
	
	/**
	 * This function finds the length of the vector between the start and final point.
	 * @return
	 */
	public void setDistance() {
		distance = heading.length();
	}
	
	/**
	 * This function is derived from the kinematics formula, attempting to create a way to determine position with a non-constant acceleration.
	 * This position is based off of two formulas: One for X and one for Y. Each is calculated the same way, but with Y substituted for X in the second.
	 * 
	 * Position = .5 * acceleration * time^2 + (previous velocity * time) + previous position
	 * Previous Velocity = Previous acceleration * time + Initial Velocity
	 * 
	 * Therefore,
	 * 
	 * Position = .5 * acceleration * time^2 + (((previous acceleration  * time) + initial velocity) * time) + previous position
	 * @return
	 */
	public Point updateCurrentPoint() {
		positionX = (.5 * accelX * timeNow * timeNow) + (((prevAccelX * prevTime) + prevVeloX) * timeNow) + prevPositionX;
		positionY = (.5 * accelY * timeNow * timeNow) + (((prevAccelY * prevTime) + prevVeloY) * timeNow) + prevPositionY;
		changeVector = new Vector2(positionX, positionY);
		currentPoint = currentPoint.add(changeVector);
		return currentPoint;
	}
	
	/**
	 * This functions looks at the accelerometer and the current time. This data is used in the above kinematics function.
	 * @param sense
	 */
	public void getSensorValues(Sensors sense) {
		accelX = sense.accelerometer.getX();
		accelY = sense.accelerometer.getY();
		timeInMilli = System.currentTimeMillis() - timeInitInMilli;
		timeNow = timeInMilli * Constants.MILLI_TO_SEC;
	}
	
	@Override
	protected void execute() {
		setDistance();
		if(Math.abs(distance) > range) {
			getSensorValues(sense);
			updateCurrentPoint();
			if (distance > range) {
				tank.tankDrive(1.0, 1.0);
			} else if (distance < range) {
				tank.tankDrive(-1.0, -1.0);
			}
		}
	}

	@Override
	public void setName(String subsystem, String name) {
		
	}

	@Override
	protected boolean isFinished() {
		if (Math.abs(distance) < range) {
			return true;
		} else {
		return false;
		}
	}

}
