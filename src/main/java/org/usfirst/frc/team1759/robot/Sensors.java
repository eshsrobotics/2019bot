package org.usfirst.frc.team1759.robot;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;

/**
 * 
 * @author Aidan Galbreath
 * 
 * This class is used to keep track of all sensors on this year's robot. Like OI and RobotMap,
 * this class is used to abstract
 *
 */
public class Sensors {

	BuiltInAccelerometer accelerometer;
	ADXRS450_Gyro gyro;
	
	public Sensors() {
		accelerometer = new BuiltInAccelerometer();
		gyro = new ADXRS450_Gyro();
	}
	
}
