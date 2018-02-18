/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team1759.robot;

import org.usfirst.frc.team1759.commands.LowerArm;
import org.usfirst.frc.team1759.commands.RaiseArm;
import org.usfirst.frc.team1759.robot.commands.FollowPath;
import org.usfirst.frc.team1759.robot.subsystems.Arm;
import org.usfirst.frc.team1759.robot.subsystems.Climber;
import org.usfirst.frc.team1759.robot.subsystems.Intake;
import org.usfirst.frc.team1759.robot.subsystems.Launcher;
import org.usfirst.frc.team1759.robot.subsystems.TankDrive;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import models.Graph;

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
	private LowerArm lower;
	private RaiseArm raise;

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
		lower = new LowerArm(arm);
		raise = new RaiseArm(arm);
		launcher = new Launcher();
		climber = new Climber();
		// Parse match data for use later on
		matchData = new MatchData(DriverStation.getInstance());
		encoder = new Encoder(0, 1);		//TODO: Determine pulses per revolution and distance per revolution in order to set distance per pulse
		
		// TODO
		//currentPosition = new Vector2(0, 0);
	}

	public void disabledInit() {

	}

	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	public void autonomousInit() {
		Graph graph = new Graph(matchData);
		FollowPath followPath = new FollowPath(encoder, tank, graph.currentNode, Graph.findPath(graph.currentNode, graph.target));
		followPath.start();
	}

	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}

	public void teleopInit() {

	}

	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
		tank.tankDrive(oi);
		launcher.launch(oi);
		climber.climb(oi);
		if(oi.in.get()) {
			lowerIntake.takeIn(1.0);
			upperIntake.takeIn(1.0);
		} else if(oi.out.get()) {
			lowerIntake.pushOut(1.0);
			upperIntake.pushOut(1.0);
		} else {
			upperIntake.stop();
			lowerIntake.stop();
		}
		oi.armIn.whenPressed(raise);
		oi.armOut.whenPressed(lower);
	}
}