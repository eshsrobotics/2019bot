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
	public Button intakeOut;
	public Button intakeIn;
	public Button armOut;
	public Button armIn;
	public Button forward;
	public Button back;
	public Button left;
	public Button right; 
	public Button sneak;
	public Button climbUp;
	public Button climbDown;
	public Button halfPower;
	public Button precision; 

	public Joystick leftJoystick;
	public Joystick rightJoystick;
	
	private NetworkTable inputTable;

	//With the variables below, the high/up option is a button keyed to the right joystick, the 
	//low/down option is a button keyed to the left joystick.
	
	//public static final int LAUNCHING_BUTTON_OUT = 1;
	//public static final int LAUNCHING_BUTTON_IN = 1;

	public static final int INTAKE_IN_BUTTON = 1;
	public static final int INTAKE_OUT_BUTTON = 1;
	
	public static final int HALF_POWER_BUTTON = 2;	
	
	public static final int ARM_OUT_BUTTON = 4;
	public static final int ARM_IN_BUTTON = 6;
	
	public static final int SNEAK = 4;
	public static final int PRECISION = 5;

	public OI() {

		leftJoystick = new Joystick(0);
		rightJoystick = new Joystick(1);

		// Can set to false to force keyboard controls
		joysticksAttached = leftJoystick != null && rightJoystick != null;
		
		inputTable = NetworkTableInstance.getDefault().getTable("inputTable");
		NetworkTableInstance.getDefault().setUpdateRate(0.0166);
		armIn = new NetworkButton(inputTable, "E");
		armOut = new NetworkButton(inputTable, "Q");
		intakeIn = new NetworkButton(inputTable, "Right Mouse");
		intakeOut = new NetworkButton(inputTable, "Left Mouse");
		forward = new NetworkButton(inputTable, "W");
		back = new NetworkButton(inputTable, "S");
		left = new NetworkButton(inputTable, "A");
		right = new NetworkButton(inputTable, "D");
		sneak = new NetworkButton(inputTable, "Shift");
		halfPower = new NetworkButton(inputTable, "Tab");
		precision = new NetworkButton(inputTable, "Ctrl");
	
	
		if (joysticksAttached) {
			System.out.println("Creating OI with joystick buttons");
			armOut =    new SharedButton (new JoystickButton(rightJoystick, ARM_OUT_BUTTON), armOut);
			armIn =     new SharedButton (new JoystickButton(rightJoystick, ARM_IN_BUTTON), armIn);
			intakeIn =  new SharedButton (new JoystickButton(rightJoystick, INTAKE_IN_BUTTON), intakeIn);
			intakeOut = new SharedButton (new JoystickButton(leftJoystick, INTAKE_OUT_BUTTON), intakeOut);
			halfPower = new SharedButton (new JoystickButton(rightJoystick, HALF_POWER_BUTTON), halfPower);
			sneak = new SharedButton (new JoystickButton(leftJoystick, SNEAK), sneak);
			precision = new SharedButton (new JoystickButton(leftJoystick, PRECISION), precision);
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
			
		}
	}
	private class SharedButton extends Button {
		
		Button network;
		Button joystick;
		
		public SharedButton(Button network, Button joystick) {
			this.network = network;
			this.joystick = joystick;
		}
		
		@Override
		public boolean get() {
			return network.get() || joystick.get();
		}
	}
}
