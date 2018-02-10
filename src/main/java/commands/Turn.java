package commands;

import org.usfirst.frc.team1759.robot.Sensors;
import org.usfirst.frc.team1759.robot.subsystems.TankDrive;

import edu.wpi.first.wpilibj.command.Command;

public class Turn extends Command {

	public Turn(TankDrive tank, Sensors sensors, double dest) {
		
	}
	@Override
	public void setName(String subsystem, String name) {
		
	}

	@Override
	protected boolean isFinished() {
		return false;
	}

}
