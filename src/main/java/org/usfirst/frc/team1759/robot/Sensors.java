package org.usfirst.frc.team1759.robot;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.interfaces.Accelerometer;
import edu.wpi.first.wpilibj.interfaces.Gyro;

public class Sensors {
	public static Accelerometer accelerometer;
	public static Gyro gyro;
	
	public Sensors() {
		accelerometer = new BuiltInAccelerometer(Accelerometer.Range.k2G);
		gyro = new ADXRS450_Gyro();
	}
}
