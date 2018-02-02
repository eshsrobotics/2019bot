package org.usfirst.frc.team1759.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 * 
 * THIS IS NOT FOR BUTTONS.
 * 
 * @author Aidan Galbreath
 */
public class RobotMap {
	public static int LEFT_FRONT_PORT = 0;
	public static int LEFT_MID_PORT = 3;
	public static int LEFT_BACK_PORT = 1;
	public static int RIGHT_FRONT_PORT = 5;
	public static int RIGHT_MID_PORT = 8;
	public static int RIGHT_BACK_PORT = 6;
	
	public static int HIGH_LAUNCH_PORT = 0;
	public static int LOW_LAUNCH_PORT = 1;
	
	public static int LEFT_IN_PORT = 2;
	public static int RIGHT_IN_PORT = 7;
	
	private RobotMap() {
		
	}
}