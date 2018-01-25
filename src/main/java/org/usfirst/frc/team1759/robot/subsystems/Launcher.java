package org.usfirst.frc.team1759.robot.subsystems;

import org.usfirst.frc.team1759.robot.OI;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.Solenoid;

public class Launcher extends Subsystem {

	Solenoid launcher;
	
	public Launcher() {
		launcher = new Solenoid(0); 

	}
	@Override
	public void setName(String subsystem, String name) {
		// TODO Auto-generated method stub
		
		
	} 
	public void launch(OI oi) {
		if (oi.joysticksAttached) {
			if (oi.rightJoystick.getTrigger()) {
				launcher.set(true);
			} else {
				launcher.set(false);
			}
		} else {
			if(oi.highLaunch.get()) {
				launcher.set(true);
			} else {
				launcher.set(false); 
			}
		}
	}

	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub
		
	}


}
