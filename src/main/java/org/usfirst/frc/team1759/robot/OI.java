package org.usfirst.frc.team1759.robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.buttons.NetworkButton;

// @Author Spencer Moore. Used to set buttons and distinguish between Keyboards and Joytsticks controls

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

	public Joystick leftJoystick;
	public Joystick rightJoystick;

	public static final int HIGH_LAUNCHING = 1;
	public static final int LOW_LAUNCHING = 1;

	public static final int INTAKE_UP = 2;
	public static final int INTAKE_DOWN = 2;
	
	private NetworkTable inputTable;

	public OI() {

		leftJoystick = new Joystick(0);
		rightJoystick = new Joystick(1);

		joysticksAttached = leftJoystick != null && rightJoystick != null;

		if (joysticksAttached) {
			highLaunch = new JoystickButton(rightJoystick, HIGH_LAUNCHING);
			lowLaunch = new JoystickButton(leftJoystick, LOW_LAUNCHING);
			intakeUp = new JoystickButton(rightJoystick, INTAKE_UP);
			intakeDown = new JoystickButton(leftJoystick, INTAKE_DOWN);
			
		} else {
			// set to WASD control
			inputTable = NetworkTableInstance.getDefault().getTable("inputTable");
			highLaunch = new NetworkButton(inputTable, "q");
			lowLaunch = new NetworkButton(inputTable, "e");
			intakeUp = new NetworkButton(inputTable, "f");
			intakeDown = new NetworkButton(inputTable, "r");
			forward = new NetworkButton(inputTable, "w");
			back = new NetworkButton(inputTable, "s");
			left = new NetworkButton(inputTable, "a");
			right = new NetworkButton(inputTable, "d");
			sneak = new NetworkButton(inputTable, "Shift");


			
		}
	}

}
