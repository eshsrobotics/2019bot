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
	
	public Button highLaunch;
	public Button lowLaunch;
	public Button intakeUp;
	public Button intakeDown;
	public Button forward;
	public Button back;
	public Button left;
	public Button right; 
	public Button sneak;
	public Button climbUp;
	public Button climbDown;

	public Joystick leftJoystick;
	public Joystick rightJoystick;

	public static final int HIGH_LAUNCHING_BUTTON = 1;
	public static final int LOW_LAUNCHING_BUTTON = 1;

	public static final int INTAKE_UP_BUTTON = 2;
	public static final int INTAKE_DOWN_BUTTON = 2;
	
	private NetworkTable inputTable;

	public OI() {

		leftJoystick = new Joystick(0);
		rightJoystick = new Joystick(1);

		// Can set to false to force keyboard controls
		joysticksAttached = leftJoystick != null && rightJoystick != null;

		if (joysticksAttached) {
		  System.out.println("Creating OI with joystick buttons");
			highLaunch = new JoystickButton(rightJoystick, HIGH_LAUNCHING_BUTTON);
			lowLaunch = new JoystickButton(leftJoystick, LOW_LAUNCHING_BUTTON);
			intakeUp = new JoystickButton(rightJoystick, INTAKE_UP_BUTTON);
			intakeDown = new JoystickButton(leftJoystick, INTAKE_DOWN_BUTTON);
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
			highLaunch = new NetworkButton(inputTable, "U");
			lowLaunch = new NetworkButton(inputTable, "I");
			intakeUp = new NetworkButton(inputTable, "O");
			intakeDown = new NetworkButton(inputTable, "P");
			climbUp = new NetworkButton(inputTable, "J");
			climbDown = new NetworkButton(inputTable, "K");
			forward = new NetworkButton(inputTable, "W");
			back = new NetworkButton(inputTable, "S");
			left = new NetworkButton(inputTable, "A");
			right = new NetworkButton(inputTable, "D");
			sneak = new NetworkButton(inputTable, "Shift");
		}
	}

}
