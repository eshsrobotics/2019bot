/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team1759.robot;

import org.usfirst.frc.team1759.robot.commands.LowerArm;
import org.usfirst.frc.team1759.robot.commands.RaiseArm;
import org.usfirst.frc.team1759.robot.commands.FollowPath;
import org.usfirst.frc.team1759.robot.commands.ExpelCommand;
import org.usfirst.frc.team1759.robot.commands.GoEncoder;
import org.usfirst.frc.team1759.robot.subsystems.Arm;
import org.usfirst.frc.team1759.robot.subsystems.Intake;
import org.usfirst.frc.team1759.robot.subsystems.TankDrive;

import org.usfirst.frc.team1759.robot.MatchData;


import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import models.Graph;
import models.Vector2;
import models.Point;
import models.Constants;
import models.TestableCommandInterface;
import wrappers.EncoderWrapper;

/**
 * This is a demo program showing the use of the RobotDrive class, specifically
 * it contains the code necessary to operate a robot with tank drive.
 */
public class Robot extends IterativeRobot {
	private TankDrive tank;
	private long autonomousStartTimeMilliseconds;
	private Intake upperIntake;
	private Intake lowerIntake;
	private Arm arm;
	private OI oi;
	private MatchData matchData;
	private Encoder encoder;
	private Gyro gyro;
	static final double AUTONOMOUS_SPEED = 0.5;
	
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
		lowerIntake = new Intake(new WPI_TalonSRX(RobotMap.LOWER_LEFT_INTAKE),
				new WPI_TalonSRX(RobotMap.LOWER_RIGHT_INTAKE));
		arm = new Arm(new DoubleSolenoid(RobotMap.ARM_PORT_IN,
				RobotMap.ARM_PORT_OUT));
		// Parse match data for use later on
		matchData = new MatchData(DriverStation.getInstance());
		encoder = new Encoder(0, 1, false, EncodingType.k4X);
		
		//currentPosition = new Vector2(0, 0);
		gyro = new ADXRS450_Gyro();
		arm.raise();
	}

	public void disabledInit() {

	}

	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	public void autonomousInit() {
		GoEncoder goCommand = new GoEncoder(encoder, tank, new Point(0, 0), new Point(0, 5));
		Scheduler.getInstance().add(goCommand);
		this.autonomousStartTimeMilliseconds = System.currentTimeMillis();
		//goCommand.customStart();
	}

	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
		if (System.currentTimeMillis() - this.autonomousStartTimeMilliseconds < Constants. AUTONOMOUS_TIME_DRIVE_FORWARD_MILLISECONDS) {
			tank.tankDrive(-AUTONOMOUS_SPEED, -AUTONOMOUS_SPEED);
		} else {
			tank.tankDrive(0,0);
		}
	}

	public void teleopInit() {

		
	}

	@Override
	public void teleopPeriodic() {
		//System.out.printf("teleopPeriodic: encoder distance = %.2f \n", encoder.getDistance());
		Scheduler.getInstance().run();
		tank.tankDrive(oi);
		
		// Add a control that allows us to slow down the intake and outtake, which gives us more control when
		// getting cubes in and out of the portal.
		if (oi.halfPower.get()) {
			lowerIntake.speedMult = Intake.PARTIAL_MULT;
		} else {
			lowerIntake.speedMult = Intake.FULL_MULT;
		}
		double speedMult = 1;
		if (oi.sneak.get()) {
				speedMult = 0.65; 
		} else if (oi.precision.get()) {
				speedMult = 0.4;
		}
		if(oi.intakeIn.get()) {
			lowerIntake.takeIn(speedMult);
			//upperIntake.takeIn(1.0);
		} else if(oi.intakeOut.get()) {
			lowerIntake.pushOut(speedMult);
			//upperIntake.pushOut(1.0);
		} else {
			//upperIntake.stop();
			lowerIntake.stop();
		}
		if(oi.rightJoystick.getThrottle() > THROTTLE_THRESHOLD || oi.armIn.get()) {
				arm.lower();
		} else if(oi.rightJoystick.getThrottle() < -1.0 * THROTTLE_THRESHOLD || oi.armOut.get()) {
			arm.raise();
		} else {
			/**
			 * If neither condition is true, the arm will remain in it's current state. If we utilize the kOff setting for the solenoid, it will move as
			 * gravity, inertia, etc. dictates. We want the arm to remain in a current position, so it does nothing otherwise.
			 */
		}
	}
}