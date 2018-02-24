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
	
	// For launcher solenoid
	public static int HIGH_LAUNCH_PORT_OUT = 0;
	public static int HIGH_LAUNCH_PORT_IN = 1;
	
	// For intake solenoid
	public static int ARM_PORT_OUT = 3;
	public static int ARM_PORT_IN = 2;
	
	// Upper intake motors
	public static int UPPER_LEFT_INTAKE= 3;
	public static int UPPER_RIGHT_INTAKE = 1;
	
	// Lower intake motors
	public static int LOWER_LEFT_INTAKE = 4;
	public static int LOWER_RIGHT_INTAKE = 5;
	
	// Climber ports
	public static int CLIMBER_PORT_1 = 10; 		//Spark, not Talon
	public static int CLIMBER_PORT_2 = 11;		//Spark, not Talon
	
	

	
//	public static final int CLIMBER_UP_PORT = 10; //will be assigned to sparks in contrast to everything else being talons
//	public static final int CLIMBER_DOWN_PORT = 11;// ^same
//	
//	public static final int INTAKE_PORT_1 = 4; // Should be 7	
//	public static final int INTAKE_PORT_2 = 7; // Should be 8
//	
//	public static final int RESERVE_PORT_1 = 8;
//	public static final int RESERVE_PORT_2 = 9; //neither are currently used
	
	private RobotMap() {
		
	}
}