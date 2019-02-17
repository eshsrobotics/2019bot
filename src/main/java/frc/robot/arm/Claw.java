package frc.robot.arm;

import frc.robot.OI;
import frc.robot.driving.RobotMap;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.DigitalInput;

/**
 * This subsystem is used to control the arm raising or lowering for 2019bot. 
 * 
 * @author Spencer Moore
 */

public class Claw extends Subsystem {
    private Spark claw;
	private DigitalInput clawCloseLimitSwitch;

    public Claw() {
		claw = new Spark(RobotMap.CLAW_MOVE);
		clawCloseLimitSwitch = new DigitalInput(RobotMap.CLAW_CLOSE_LIMIT_SWITCH_1);
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

				double joystickValue = oi.rightJoystick.getX();
				double joystickDirection = Math.signum(joystickValue);
				if (joystickDirection < 0) {
					// User wants to close the claw.
					if (canClawClose()) {
						claw.set(joystickValue);
					}
				} else {
					// We don't have canClawOpen() right now.
					claw.set(joystickValue);
				}
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
	public boolean isClosing() {
		boolean isClawClosing = (Math.signum(claw.get()) < 0);
		return isClawClosing;
	}
	public void stopClosing() {
		if (this.isClosing()) {
			// This should only be executed once as the claw closes.
			System.out.printf("[%d] stopClosing: claw.get() == %.4f", System.currentTimeMillis(), claw.get());
			stop();
		}
	}
	public void stop() {
		claw.set(0);
	}
	public boolean canClawClose() {
		boolean clawCloseLimitSwitchHit = clawCloseLimitSwitch.get();
		return clawCloseLimitSwitchHit;
	}
}
