package org.usfirst.frc.team1759.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 * 
 * THIS IS NOT FOR BUTTONS.
 * 
 * @author Aidan Galbreath and Spencer Moore
 */
public class RobotMap {
	
	// Drive controller ports
	public static int LEFT_FRONT_PORT = 6;
	public static int LEFT_BACK_PORT = 0;
	public static int RIGHT_FRONT_PORT = 7;
	public static int RIGHT_BACK_PORT = 2;
	
	public static int LAUNCHER_SOLENOID_OUT = 6;
	public static int LAUNCHER_SOLENOID_IN = 7;
	
	private RobotMap() {
		
	}
}