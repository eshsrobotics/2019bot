//*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.cameraserver.*;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import frc.robot.OI;
import frc.robot.Sneak;
import frc.robot.driving.TankDrive;
import frc.robot.driving.RobotMap;
import frc.robot.arm.Arm;
import frc.robot.arm.Claw;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();
  private Spark motor;
  private TankDrive tank;
  private Claw claw;
  private Arm arm;
  private OI oi;
  private Sneak driveSneak;
  private Sneak wristSneak;

  // Toggles sneaking for both the wrist and the drive.
  private void handleSneak() {

    // Ordinarily, you sneak with the NetworkTables sneak button.
    boolean sneakButtonActivated = oi.sneak.get();

    // But you can also sneak with the joystick.
    //
    // TODO: Make a JoystickButton in OI for right joystick #2.
    if (oi.joysticksAttached && oi.rightJoystick.getRawButtonPressed(2)) {
      sneakButtonActivated = true;
    }

    if (sneakButtonActivated) {
      if (wristSneak.enabled()) {
        wristSneak.disable();
        driveSneak.disable();
      } else {
        wristSneak.enable();
        driveSneak.enable();
      }
    }
  }

  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  @Override
  public void robotInit() {
    try {
    CameraServer.getInstance().startAutomaticCapture();
    driveSneak = new Sneak(RobotMap.DRIVE_FORWARD_SNEAK_VALUE);
    wristSneak = new Sneak(RobotMap.WRIST_SNEAK_VALUE);
    tank = new TankDrive(driveSneak);
    oi = new OI();
    claw = new Claw();
    arm = new Arm(wristSneak);
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);
    } catch (Exception e) {
      System.out.printf("Caught an exception during robotInit(): %s\n", e.getMessage());
      e.printStackTrace();
      System.exit(1);
    }
  }

  @Override
  public void disabledInit() {
    System.out.println("I'm disabled daddy!");
    super.disabledInit();
  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for
   * items like diagnostics that you want ran during disabled, autonomous,
   * teleoperated and test.
   *
   * <p>
   * This runs after the mode specific periodic functions, but before LiveWindow
   * and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable chooser
   * code works with the Java SmartDashboard. If you prefer the LabVIEW Dashboard,
   * remove all of the chooser code and uncomment the getString line to get the
   * auto name from the text box below the Gyro
   *
   * <p>
   * You can add additional auto modes by adding additional comparisons to the
   * switch structure below with additional strings. If using the SendableChooser
   * make sure to add them to the chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    arm.arm(oi);
    claw.claw(oi);
    tank.tankDrive(oi);
    m_autoSelected = m_chooser.getSelected();
    // autoSelected = SmartDashboard.getString("Auto Selector",
    // defaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    arm.arm(oi);
    claw.claw(oi);
   /* if (!claw.canClawClose()) {
      claw.stopClosing();
    }*/
    tank.tankDrive(oi);
    handleSneak();    
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    arm.arm(oi);
    claw.claw(oi);
    /*if (!claw.canClawClose()) {
      claw.stopClosing();
    }*/
    tank.tankDrive(oi);
    handleSneak();
    // motor.set(1);
    // System.out.println(motor.get());
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
