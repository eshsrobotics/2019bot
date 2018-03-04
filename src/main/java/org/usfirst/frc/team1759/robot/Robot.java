/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team1759.robot;

import models.Graph;

import org.usfirst.frc.team1759.robot.commands.ExpelCommand;
import org.usfirst.frc.team1759.robot.commands.FollowPath;
import org.usfirst.frc.team1759.robot.commands.ShootCommand;
import org.usfirst.frc.team1759.robot.subsystems.Arm;
import org.usfirst.frc.team1759.robot.subsystems.Climber;
import org.usfirst.frc.team1759.robot.subsystems.Intake;
import org.usfirst.frc.team1759.robot.subsystems.Launcher;
import org.usfirst.frc.team1759.robot.subsystems.TankDrive;

import wrappers.EncoderWrapper;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;

/**
 * This is a demo program showing the use of the RobotDrive class, specifically
 * it contains the code necessary to operate a robot with tank drive.
 */
public class Robot extends IterativeRobot {
	private TankDrive tank;
	private Launcher launcher;
	private Intake upperIntake;
	private Intake lowerIntake;
	private Climber climber;
	private Arm arm;
	private OI oi;
	private MatchData matchData;
	private Encoder encoder;

	/**
	 * Used to set the threshold for the throttle. If the throttle is greater than positive threshold, it is up. If it is less
	 * than negative threshold, it is down. If it is between positive and negative threshold, it remains in it's current state.
	 */
	private static final double THROTTLE_THRESHOLD = 1 / 3.0;


	@Override
	public void robotInit() {
		// Streams usb camera video feed straight to Dashboard.
		CameraServer.getInstance().startAutomaticCapture();
		// Initialize drive.
		oi = new OI();
		tank = new TankDrive();
		upperIntake = new Intake(new WPI_TalonSRX(RobotMap.UPPER_LEFT_INTAKE),
				new WPI_TalonSRX(RobotMap.UPPER_RIGHT_INTAKE));
		lowerIntake = new Intake(new WPI_TalonSRX(RobotMap.LOWER_LEFT_INTAKE),
				new WPI_TalonSRX(RobotMap.LOWER_RIGHT_INTAKE));
		arm = new Arm(new DoubleSolenoid(RobotMap.ARM_PORT_IN,
				RobotMap.ARM_PORT_OUT));
		launcher = new Launcher();
		climber = new Climber();
		// Parse match data for use later on
		matchData = new MatchData(DriverStation.getInstance());
		encoder = new Encoder(0, 1);		//TODO: Determine pulses per revolution and distance per revolution in order to set distance per pulse
		//currentPosition = new Vector2(0, 0);
	}

	@Override
	public void disabledInit() {

	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void autonomousInit() {
		Graph graph = new Graph(matchData);
		Command endCommand = matchData.getTarget() == MatchData.Target.SCALE ? new ShootCommand(launcher) : new ExpelCommand(lowerIntake);
		FollowPath followPath = new FollowPath(new EncoderWrapper(encoder),
				tank,
				graph.getStartingNode(),
				graph.findShortestPath(graph.getStartingNode(), graph.getTargetNode()),
				endCommand);
		followPath.start();
	}

	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {

	}

	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
		tank.tankDrive(oi);
		launcher.launch(oi);
		climber.climb(oi);
		if(oi.intakeInJoystick.get() || oi.intakeInNetwork.get()) {
			lowerIntake.takeIn(1.0);
			upperIntake.takeIn(1.0);
		} else if(oi.intakeOutJoystick.get() || oi.intakeOutJoystick.get()) {
			lowerIntake.pushOut(1.0);
			upperIntake.pushOut(1.0);
		} else {
			upperIntake.stop();
			lowerIntake.stop();
		}
		if(oi.rightJoystick.getThrottle() > THROTTLE_THRESHOLD) {
				arm.raise();
		} else if(oi.rightJoystick.getThrottle() < -1.0 * THROTTLE_THRESHOLD) {
			arm.lower();
		} else {
			/**
			 * If neither condition is true, the arm will remain in it's current state. If we utilize the kOff setting for the solenoid, it will move as
			 * gravity, inertia, etc. dictates. We want the arm to remain in a current position, so it does nothing otherwise.
			 */
		}
	}
}