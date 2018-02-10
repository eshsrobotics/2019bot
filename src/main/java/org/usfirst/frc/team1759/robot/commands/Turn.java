package commands;

import models.Constants;
import models.Point;
import models.Vector2;

import org.usfirst.frc.team1759.robot.Sensors;
import org.usfirst.frc.team1759.robot.subsystems.TankDrive;

import edu.wpi.first.wpilibj.command.Command;

public class Turn extends Command {

	TankDrive tank;
	Vector2 myHeading;
	
	double myAngle;
	double destAngle;
	double degreesLeft;
	
	/**
	 * This function is used to convert from the degrees that the gyro gives to a Vector2 heading.
	 * @param gyroangle
	 * @return
	 */
	private Vector2 gyroAngleToVector(double gyroangle) {
		gyroangle = Sensors.gyro.getAngle();
		myHeading = new Vector2(Math.cos(myAngleInRadians),		//X on a unit vector is cos(theta)
					Math.sin(myAngleInRadians));	//Y on a unit vector is sin(theta)
		return myHeading;
	}
	
	/**
	 * This function is used to turn the robot from its heading upon being called to the necessary heading to move to the next point.
	 * 
	 * @param tank
	 * @param sensors
	 * @param heading
	 * 
	 * Given a necessary heading, the robot will take input from the gyroscope to determine how far it needs to turn.
	 * When the robot has a value, it will turn left if the difference in the angles is negative. 
	 * 
	 */
	
	public void start (TankDrive tank, Sensors sensors) {
		tank = new TankDrive();
		myAngle = Sensors.gyro.getAngle();
		Point startingPoint = Constants.ORIGIN;
	}
	
	public void execute(Vector2 heading) {
		
		double myAngleInRadians = myAngle * Constants.DEGREES_TO_RADIANS;
		destAngle = myHeading.angle(heading);	//Unit vectors should expect a magnitude of 1, with heading found as above.
		
		while (myAngle != destAngle) {
			myAngle = Sensors.gyro.getAngle();
			degreesLeft = destAngle - myAngle;	//If degreesLeft is negative, we will want to go left. If degreesLeft is positive, we will want to go left.
			
			if(degreesLeft > 45) {				//If we are more than 45 degrees off, go quickly.
				tank.tankDrive(-1, 1);
			} else if (degreesLeft > 15) {		//If we are less than 45 degrees off, but more than 15 degrees off, go a little slower.
				tank.tankDrive(-.5, .5);
			} else if (degreesLeft > 0) {		//If we are less than 15 degrees off, but more than 0 degrees off, we need precision.
				tank.tankDrive(-.25, -.25);
			} else if (degreesLeft < -45) {		//If we are more than 45 degrees off, go quickly.
				tank.tankDrive(1, -1);
			} else if (degreesLeft < -15) {		//If we are less than 45 degrees off, but more than 15 degrees off, go a little slower.
				tank.tankDrive(.5, -.5);
			} else if (degreesLeft < 0) {		//If we are less than 15 degrees off, but more than 0 degrees off, we need precision.
				tank.tankDrive(.25, -.25);
			} else {							//If none of the above are true, we have reached our destination.
				degreesLeft = 0;
			}
		}
	}
	@Override
	public void setName(String subsystem, String name) {
		
	}

	@Override
	protected boolean isFinished() {
		if (degreesLeft == 0) {
			return true;
		} else {
			return false;
		}
	}

}
