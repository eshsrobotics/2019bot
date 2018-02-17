/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team1759.robot;

import models.Graph;

import org.usfirst.frc.team1759.robot.commands.FollowPath;
import org.usfirst.frc.team1759.robot.subsystems.Intake;
import org.usfirst.frc.team1759.robot.subsystems.Launcher;
import org.usfirst.frc.team1759.robot.subsystems.TankDrive;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Scheduler;

/**
 * This is a demo program showing the use of the RobotDrive class, specifically
 * it contains the code necessary to operate a robot with tank drive.
 */
public class Robot extends IterativeRobot {
	private TankDrive tank;
	private Launcher launcher;
	private Intake intake;
	private OI oi;
	private MatchData matchData;
	private Encoder encoder;
	
	@Override
	public void robotInit() {
		//Streams usb camera video feed straight to Dashboard.
		CameraServer.getInstance().startAutomaticCapture();
		//Initialize drive.
		launcher = new Launcher();
		oi = new OI();
		tank = new TankDrive();
		intake = new Intake();
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
		intake.intake(oi);
	}
}