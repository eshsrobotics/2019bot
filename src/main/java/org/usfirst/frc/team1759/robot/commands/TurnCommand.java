package org.usfirst.frc.team1759.robot.commands;

import models.Constants;
import models.Point;
import models.Vector2;

import org.usfirst.frc.team1759.robot.Sensors;
import org.usfirst.frc.team1759.robot.subsystems.TankDrive;

import edu.wpi.first.wpilibj.command.Command;

/***
 * This command's purpose is to rotate the tank drive continuously until the
 * robot is pointing at the desired heading.
 * 
 * @author uakotaobi
 */
public class TurnCommand extends Command {

		/***
		 * The tank drive we're supposed to be turning. 
		 */
        TankDrive tank;
        
        /**
         * The forward-pointing vector that the robot has at the time of start().
         */
        Vector2 initialHeading;
        
        /***
         * This is the heading we ultimately want to be pointing toward.
         */
        Vector2 goalHeading;

        double myAngle;
        double lastRecordedGyroAngle;
        double destAngle;
        double degreesLeft;

        /**
         * This function is used to convert from the degrees that the gyro gives to a Vector2 heading.
         * @param gyroAngleInDegrees The gyro angle to convert.
         * @return A unit vector pointing gyroangle degrees clockwise from (0, 1).
         */
        private Vector2 gyroAngleToVector(double gyroAngleInDegrees) {
                double gyroAngleInRadians = gyroAngleInDegrees * Constants.DEGREES_TO_RADIANS;
                Vector2 myHeading = new Vector2(Math.cos(gyroAngleInRadians),	//X on a unit vector is cos(theta)
                                                Math.sin(gyroAngleInRadians));	//Y on a unit vector is sin(theta)
                return myHeading;
        }

        /**
         * Initializes this command.
         *
         * @param tank The tank drive to turn.
         */

        public TurnCommand(TankDrive tank) {
            tank = new TankDrive();
        }
        
        /***
         * Bring the command to a valid initial state. 
         */
        @Override
        public synchronized void start() {
        	// By completely arbitrary convention, this points up.
        	initialHeading = new Vector2(0, 1); 
        	
            myAngle = Sensors.gyro.getAngle();
            lastRecordedGyroAngle = myAngle;
            Point startingPoint = Constants.ORIGIN;
        };
        
        /***
         * Set the heading that we want to turn toward.
         * @param desiredHeading This vector is relative to the robot's 
         *                        *starting vector*, which is assumed to point
         *                        forward. 
         */
        void setHeading(Vector2 desiredHeading) {
        	
        }
        
        /***
         * This function is called intermittently over and over until 
         * isFinished() returns true.
         * 
         * It does a tiny bit of work to turn us toward our target
         * incrementally.
         */
        @Override
        protected void execute() {

                myAngle                                = Sensors.gyro.getAngle();
                Vector2 vectorForLastRecordedGyroAngle = gyroAngleToVector(lastRecordedGyroAngle);
                Vector2 vectorForCurrentGyroAngle      = gyroAngleToVector(myAngle);
                double amountToTurnInRadians           = vectorForLastRecordedGyroAngle.angleBetween(vectorForCurrentGyroAngle);
                double amountToTurnInDegrees           = amountToTurnInRadians * Constants.RADIANS_TO_DEGREES;

                destAngle = lastRecordedGyroAngle + amountToTurnInDegrees;
                
                // Use our desired destination 

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

                // Save for the next incremental invocation of execute().
                lastRecordedGyroAngle = Sensors.gyro.getAngle();
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
