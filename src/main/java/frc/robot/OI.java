package frc.robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.buttons.NetworkButton;

/**
 * @Author Spencer Moore, Andrew McClees, and Aidan Galbreath. 
 * 
 * Used to set buttons and distinguish between Keyboards and Joytsticks controls. THIS IS NOT FOR PORT
 * ASSIGNMENTS
 * 
 */

public class OI {
	public boolean joysticksAttached;
	
	public Button launchOut;
	public Button launchIn;
	public Button intakeOutJoystick;
	public Button intakeInJoystick;
	public Button intakeOutNetwork;
	public Button intakeInNetwork;
	public Button armOut;
	public Button armIn;
	public Button forward;
	public Button back;
	public Button left;
	public Button right; 
	public Button sneak;
	public Button climbUp;
	public Button climbDown;

	public Button elbowUpButton;
	public Button elbowDownButton;

	public Joystick leftJoystick;
	public Joystick rightJoystick;
	
	private NetworkTable inputTable;

	//With the variables below, the high/up option is a button keyed to the right joystick, the 
	//low/down option is a button keyed to the left joystick.
	
	public static final int LAUNCHING_BUTTON_OUT = 1;
	public static final int LAUNCHING_BUTTON_IN = 1;

	public static final int INTAKE_IN_BUTTON = 3;
	public static final int INTAKE_OUT_BUTTON = 3;
	
	public static final int ARM_OUT_BUTTON = 4;
	public static final int ARM_IN_BUTTON = 4;
	
	public static final int CLIMBER_UP_BUTTON = 7;
	public static final int CLIMBER_DOWN_BUTTON = 7;

	public OI() {

		leftJoystick = new Joystick(0);
		rightJoystick = new Joystick(1);
		
		// NetworkTables should always function, whether joysticks are attached or not.
		inputTable = NetworkTableInstance.getDefault().getTable("inputTable");
		NetworkTableInstance.getDefault().setUpdateRate(0.0166);
		elbowUpButton = new NetworkButton(inputTable, "Up");
		elbowDownButton = new NetworkButton(inputTable, "Down");
		climbUp = new NetworkButton(inputTable, "Left");
		climbDown = new NetworkButton(inputTable, "Right");
		launchOut = new NetworkButton(inputTable, "C");
		launchIn = new NetworkButton(inputTable, "F");

		// As long as either joystick is attahed, you can use the joystick controls.
		//
		// The keyboard controls are always available either way.
		joysticksAttached = leftJoystick != null && rightJoystick != null;
		
		/*if (joysticksAttached) {
			System.out.println("Creating OI with joystick buttons");
			launchOut = new JoystickButton(rightJoystick, LAUNCHING_BUTTON_OUT);
			launchIn = new JoystickButton(leftJoystick, LAUNCHING_BUTTON_IN);
			armOut = new JoystickButton(rightJoystick, ARM_OUT_BUTTON);
			armIn = new JoystickButton(rightJoystick, ARM_IN_BUTTON);
			climbUp = new JoystickButton(rightJoystick, CLIMBER_UP_BUTTON);
			climbDown = new JoystickButton(leftJoystick, CLIMBER_DOWN_BUTTON);
		} else {*/
			/*
			 * Network Button key strings (for NetworkButton constructor):
			 * - Alphanumeric: Uppercase of the key (i.e. A, B, C, ... or 0, 1, 2, ...)
			 * - F Keys: F1, F2, ...
			 * - Shift: "Shift"
			 * - Control: "Ctrl"
			 * - Alt: "Alt"
			 * - Windows: "Windows"
			 * - Enter: "Enter"
			 * - Escape: "Esc"
			 * - -: "Minus"
			 * - =: "Equals"
			 * - [: "Open Bracket"
			 * - ]: "Close Bracket"
			 * - /: "Slash"
			 * - ': "Quote"
			 * - ;: "Semicolon"
			 * - Caps Lock - "Caps Lock"
			 * - `: "Back Quote"
			 * 
			 * For the mouse right and left buttons, use "Right Mouse" and "Left Mouse"
			 */
		  	System.out.println("Creating OI with network buttons");
			launchOut = new NetworkButton(inputTable, "C");
			launchIn = new NetworkButton(inputTable, "F");
			armIn = new NetworkButton(inputTable, "Up");
			armOut = new NetworkButton(inputTable, "Down");
			forward = new NetworkButton(inputTable, "W");
			back = new NetworkButton(inputTable, "S");
			left = new NetworkButton(inputTable, "A");
			right = new NetworkButton(inputTable, "D");
			sneak = new NetworkButton(inputTable, "Shift");
		//}
	}

}
