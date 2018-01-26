package org.usfirst.frc.team1759.robot.subsystems;

import org.usfirst.frc.team1759.robot.OI;
import org.usfirst.frc.team1759.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.Solenoid;

public class Launcher extends Subsystem {

	Solenoid launcher;
	Solenoid lowLauncher;

	public Launcher() {
		launcher = new Solenoid(RobotMap.HIGH_LAUNCH);
		lowLauncher = new Solenoid(RobotMap.LOW_LAUNCH);
	}

	@Override
	public void setName(String subsystem, String name) {

	}

	public void launch(OI oi) {
		if (oi.highLaunch.get()) {
			launcher.set(true);
		} else {
			launcher.set(false);
		}
		if (oi.lowLaunch.get()) {
			lowLauncher.set(true);
		} else {
			lowLauncher.set(false);
		}
	}

	@Override
	protected void initDefaultCommand() {

	}

}
