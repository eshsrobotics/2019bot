package frc.robot.driving;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 * 
 * THIS IS NOT FOR BUTTONS.
 * 
 * @author Spencer Moore
 */
public class RobotMap {

	// CIM motors tend not to respond when given less than this level of power in either direction.
	public static double MIN_MOTOR_POWER = 0.15;

	// Drive motor controller ports (PWM)
	public static int LEFT_FRONT_PORT = 2;
	public static int LEFT_BACK_PORT = 3;
	public static int RIGHT_FRONT_PORT = 0;
	public static int RIGHT_BACK_PORT = 5;

	// Arm motor controller ports (PWM)
	public static int WRIST_MOVE_ONE = 1;
	public static int WRIST_MOVE_TWO = 7;
	public static int CLAW_MOVE = 4;
	public static int ELBOW_MOVE = 8; 

	// Sensor ports (digital I/O)
	public static int CLAW_CLOSE_LIMIT_SWITCH_1 = 1;

	// Sensor ports (analog)
	public static int TEST_POTENTIOMETER = 0;

	// Sneak values (force multipliers)
	public static double DRIVE_SNEAK_VALUE = 0.6;
	public static double WRIST_SNEAK_VALUE = 0.8;

	public RobotMap() {
	}
}