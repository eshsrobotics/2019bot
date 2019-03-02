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
	
	// Drive controller ports
	public static int LEFT_FRONT_PORT = 2;
	public static int LEFT_BACK_PORT = 3;
	public static int RIGHT_FRONT_PORT = 0;
	public static int RIGHT_BACK_PORT = 1;

	//arm ports
	public static int ARM_MOVE = 13;
	public static int CLAW_MOVE = 4;

	// Claw sensor ports
	public static int CLAW_CLOSE_LIMIT_SWITCH_1 = 1;
	public static int CLAW_OPEN_LIMIT_SWITCH_2 = 2;

	public RobotMap() {
	}
}