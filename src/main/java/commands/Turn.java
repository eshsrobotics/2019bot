package commands;

import models.Vector2;

import org.usfirst.frc.team1759.robot.Sensors;
import org.usfirst.frc.team1759.robot.subsystems.TankDrive;

import edu.wpi.first.wpilibj.command.Command;

public class Turn extends Command {

	Vector2 myPosition;
	double myAngle;
	double destAngle;
	double degreesLeft;
	
	public void execute(TankDrive tank, Sensors sensors, Vector2 dest) {
		
		myPosition = new Vector2(0, 0);			//What do I use for the paramaters? Is this declared from a global myPosition Vector?
		myAngle = Sensors.gyro.getAngle();
		destAngle = myPosition.angle(dest);
		
		while (myAngle != destAngle) {
			myAngle = Sensors.gyro.getAngle();
			
			degreesLeft = destAngle - myAngle;	//If degreesLeft is negative, we will want to go left. If degreesLeft is positive, we will want to go left.
			
			if(degreesLeft > 45) {				//If we are more than 45 degrees off, go quickly.
				
			} else if (degreesLeft > 15) {		//If we are less than 45 degrees off, but more than 15 degrees off, go a little slower.
				
			} else if (degreesLeft > 0) {		//If we are less than 15 degrees off, but more than 0 degrees off, we need precision.
				
			} else if (degreesLeft < -45) {		//If we are more than 45 degrees off, go quickly.
				
			} else if (degreesLeft < -15) {		//If we are less than 45 degrees off, but more than 15 degrees off, go a little slower.
				
			} else if (degreesLeft < 0) {		//If we are less than 15 degrees off, but more than 0 degrees off, we need precision.
				
			} else {							//If none of the above are true, we have reached our destination.
				
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
