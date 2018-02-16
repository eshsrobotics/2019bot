package org.usfirst.frc.team1759.robot;

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
	public Button totalIntake;
	public Button partialIntake;
	public Button expel;
	public Button forward;
	public Button back;
	public Button left;
	public Button right; 
	public Button sneak;
	public Button climbUp;
	public Button climbDown;

	public Joystick leftJoystick;
	public Joystick rightJoystick;
	
	private NetworkTable inputTable;

	//With the variables below, the high/up option is a button keyed to the right joystick, the 
	//low/down option is a button keyed to the left joystick.
	
	public static final int LAUNCHING_BUTTON_OUT = 1;
	public static final int LAUNCHING_BUTTON_IN = 1;

	public static final int PARTIAL_INTAKE_BUTTON = 2;
	public static final int FULL_INTAKE_BUTTON = 3;
	public static final int EXPEL_INTAKE_BUTTON = 4;
	
	public static final int CLIMBER_UP_BUTTON = 7;
	public static final int CLIMBER_DOWN_BUTTON = 7;

	public OI() {

		leftJoystick = new Joystick(0);
		rightJoystick = new Joystick(1);

		// Can set to false to force keyboard controls
		joysticksAttached = leftJoystick != null && rightJoystick != null;

		if (joysticksAttached) {
			System.out.println("Creating OI with joystick buttons");
			launchOut = new JoystickButton(rightJoystick, LAUNCHING_BUTTON_OUT);
			launchIn = new JoystickButton(leftJoystick, LAUNCHING_BUTTON_IN);
			totalIntake = new JoystickButton(rightJoystick, FULL_INTAKE_BUTTON);
			partialIntake = new JoystickButton(rightJoystick, PARTIAL_INTAKE_BUTTON);
			expel = new JoystickButton(rightJoystick, EXPEL_INTAKE_BUTTON);
			climbUp = new JoystickButton(rightJoystick, CLIMBER_UP_BUTTON);
			climbDown = new JoystickButton(leftJoystick, CLIMBER_DOWN_BUTTON);
		} else {
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
			inputTable = NetworkTableInstance.getDefault().getTable("inputTable");
			NetworkTableInstance.getDefault().setUpdateRate(0.0166);
			launchOut = new NetworkButton(inputTable, "J");
			launchIn = new NetworkButton(inputTable, "K");
			totalIntake = new NetworkButton(inputTable, "O");		//Fix Buttons for Total/Partial/Expel
			partialIntake = new NetworkButton(inputTable, "P");
			expel = new NetworkButton(inputTable, "U");
			climbUp = new NetworkButton(inputTable, "N");
			climbDown = new NetworkButton(inputTable, "M");
			forward = new NetworkButton(inputTable, "W");
			back = new NetworkButton(inputTable, "S");
			left = new NetworkButton(inputTable, "A");
			right = new NetworkButton(inputTable, "D");
			sneak = new NetworkButton(inputTable, "Shift");
		}
	}

}
