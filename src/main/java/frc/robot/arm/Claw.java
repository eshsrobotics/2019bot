package frc.robot.arm;

import frc.robot.OI;
import frc.robot.driving.RobotMap;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.command.Subsystem;
/**
 * This subsystem is used to control the arm raising or lowering for 2019bot. 
 * 
 * @author Spencer Moore
 */

public class Claw extends Subsystem{
    private Spark claw;

    public Claw() {
        claw = new Spark(RobotMap.CLAW_MOVE);
	}
	@Override
	public void setName(String subsystem, String name) {

	}

	@Override
	public void initDefaultCommand() {
		//myRobot = new DifferentialDrive(left, right);
	}

	public void claw(OI oi) {
		//if (oi.joysticksAttached) {
           /* while (oi.rightJoystick.getTrigger()) {
				claw.set(1);
			}
			while (oi.leftJoystick.getTrigger()) {
				claw.set(-1);
			}*/
			if (oi.joysticksAttached) {
				double h = 0;
				if (oi.leftJoystick.getTrigger()) {
					h = 0.6;
				} else if (oi.rightJoystick.getTrigger()) {
					h = -0.6;
				} else {
					h = 0;
				}
				claw.set(oi.rightJoystick.getX());
			}
			//myRobot.tankDrive(- oi.leftJoystick.getY(), - oi.rightJoystick.getY());
		//} else {
			/*double left = 0;
			double right = 0;

			if (oi.forward.get()) {
				left = 1;
				right = 1;
			} else if (oi.back.get()) {
				left = -1;
				right = -1;
			} else if (oi.left.get()) {
				left = -1;
				right = 1;
			} else if (oi.right.get()) {
				left = 1;
				right = -1;
			}
			if (oi.sneak.get()) {
				if (oi.left.get() || oi.right.get()) {
					left *= 0.8;
					right *= 0.8;
				} else {
					left *= 0.5;
					right *= 0.5;
				}
			}

			myRobot.tankDrive(- left, - right);
        *///}
	}
	
	public void claw (/*double leftSpeed, double rightSpeed*/) {
		//myRobot.tankDrive(- leftSpeed, - rightSpeed);
	}
}
