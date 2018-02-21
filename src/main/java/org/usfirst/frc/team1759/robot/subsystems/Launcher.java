package org.usfirst.frc.team1759.robot.subsystems;

import org.usfirst.frc.team1759.robot.OI;
import org.usfirst.frc.team1759.robot.RobotMap;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Launcher extends Subsystem {

	public DoubleSolenoid launcher;

	public Launcher() {
		launcher = new DoubleSolenoid(RobotMap.HIGH_LAUNCH_PORT_OUT, RobotMap.HIGH_LAUNCH_PORT_IN);
	}

	@Override
	public void setName(String subsystem, String name) {

	}

	public void launch(OI oi) {
		if (oi.launchOut.get()) {
			launcher.set(DoubleSolenoid.Value.kForward);
		} else {
			launcher.set(DoubleSolenoid.Value.kReverse);
		} 
	}

	@Override
	protected void initDefaultCommand() {

	}

}
