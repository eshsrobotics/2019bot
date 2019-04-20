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
	private static double clawMotion = 0.0;
    private Spark claw;
	private DigitalInput clawCloseLimitSwitch;

    public Claw() {
		claw = new Spark(RobotMap.CLAW_MOVE);
		clawCloseLimitSwitch = new DigitalInput(RobotMap.CLAW_CLOSE_LIMIT_SWITCH_1);
	}
	@Override
	public void setName(String subsystem, String name) { }

	@Override
	public void initDefaultCommand() { }

	public void claw(OI oi) {
		if ((oi.joysticksAttached && oi.rightJoystick.getTrigger()) || oi.launchIn.get()) {
			clawMotion = 0.8;
		} else if ((oi.joysticksAttached && oi.leftJoystick.getTrigger()) || oi.launchOut.get()) {
			clawMotion = -0.8;
		} else {
				clawMotion = 0;
		}
		claw.set(clawMotion);
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
