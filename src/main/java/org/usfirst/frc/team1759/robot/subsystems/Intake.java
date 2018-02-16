package org.usfirst.frc.team1759.robot.subsystems;

import org.usfirst.frc.team1759.robot.OI;
import org.usfirst.frc.team1759.robot.RobotMap;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * This subsystem is used to control the belts that lift the power cube from the ground to the launching mechanism. This is done via two buttons.
 * While joysticks are connected, it moves forward 
 */
public class Intake extends Subsystem {
	WPI_TalonSRX leftIntake;
	WPI_TalonSRX rightIntake;
	WPI_TalonSRX leftWheels;
	WPI_TalonSRX rightWheels;
	DoubleSolenoid lift;
	DigitalInput limitSwitch;
	private long timeStart;
	private double SPEED_MULT = 0.5;

	public Intake() {
		leftIntake = new WPI_TalonSRX(RobotMap.LEFT_IN_PORT);
		rightIntake = new WPI_TalonSRX(RobotMap.RIGHT_IN_PORT);
		leftWheels = new WPI_TalonSRX(RobotMap.LIFT_LEFT_PORT);
		rightWheels = new WPI_TalonSRX(RobotMap.LIFT_RIGHT_PORT);
		lift = new DoubleSolenoid(RobotMap.INTAKE_PORT_IN, RobotMap.INTAKE_PORT_OUT);
		limitSwitch = new DigitalInput(2);
	}

	@Override
	public void setName(String subsystem, String name) {

	}

	@Override
	protected void initDefaultCommand() {

	}

	public void intake(OI oi) {
		if (limitSwitch.get()) {
			SPEED_MULT = .35;
			System.out.println(limitSwitch.get());
		}
		if (oi.intakeUp.get()) {
			timeStart = System.currentTimeMillis();
			if(System.currentTimeMillis() - timeStart < 2000) {
				lift.set(DoubleSolenoid.Value.kReverse);
				leftWheels.set(SPEED_MULT);
				rightWheels.set(SPEED_MULT);
			} else if (System.currentTimeMillis() - timeStart < 4000) {
				leftIntake.set(SPEED_MULT);
				rightIntake.set(SPEED_MULT);
				leftWheels.set(0);
				rightWheels.set(0);
				lift.set(DoubleSolenoid.Value.kForward);
			} else {
				leftIntake.set(0);
				rightIntake.set(0);
				lift.set(DoubleSolenoid.Value.kOff);
			}
		} else if (oi.intakeDown.get()) {
			if(System.currentTimeMillis() - timeStart < 2000) {
				lift.set(DoubleSolenoid.Value.kReverse);
				leftIntake.set(-1.0 * SPEED_MULT);
				rightIntake.set(-1.0 * SPEED_MULT);
			} else if(System.currentTimeMillis() - timeStart < 4000) {
				lift.set(DoubleSolenoid.Value.kForward);
				leftIntake.set(0);
				rightIntake.set(0);
				leftWheels.set(-1.0 * SPEED_MULT);
				rightWheels.set(-1.0 * SPEED_MULT);
			} else {
				lift.set(DoubleSolenoid.Value.kOff);
			}
		} else {
			leftIntake.set(0);
			rightIntake.set(0);
		}
	}
}
