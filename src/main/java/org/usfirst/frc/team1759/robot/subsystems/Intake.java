package org.usfirst.frc.team1759.robot.subsystems;

import org.usfirst.frc.team1759.robot.OI;
import org.usfirst.frc.team1759.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Subsystem;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

/**
 * This subsystem is used to control the belts that lift the power cube from the ground to the launching mechanism. This is done via two buttons.
 * While joysticks are connected, it moves forward 
 */
public class Intake extends Subsystem {
	WPI_TalonSRX leftIntake;
	WPI_TalonSRX rightIntake;
	private final double SPEED_MULT = 0.5;

	public Intake() {
		leftIntake = new WPI_TalonSRX(RobotMap.LEFT_IN_PORT);
		rightIntake = new WPI_TalonSRX(RobotMap.RIGHT_IN_PORT);
	}

	@Override
	public void setName(String subsystem, String name) {

	}

	@Override
	protected void initDefaultCommand() {

	}

	public void intake(OI oi) {
		if (oi.intakeUp.get()) {
			leftIntake.set(SPEED_MULT);
			rightIntake.set(SPEED_MULT);
		} else if (oi.intakeDown.get()) {
			leftIntake.set(-1.0 * SPEED_MULT);
			rightIntake.set(-1.0 * SPEED_MULT);
		} else {
			leftIntake.set(0);
			rightIntake.set(0);
		}
	}
}
