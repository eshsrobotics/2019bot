package frc.robot.arm;
//import edu.wpi.first.hal.AnalogGyroJNI;
import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
//import frc.robot.driving.RobotMap;
import edu.wpi.first.wpilibj.SPI.Port;

public class Gyro {
    //private static  ADXRS450_Gyro gyro;
    private static AnalogGyro gyro;

    public Gyro() {
       //gyro = new ADXRS450_Gyro(Port.kOnboardCS2);
       gyro = new AnalogGyro(0);
       gyro.setSensitivity(0.0007);
       // gyro.calibrate();
       
        
        //gryo = new AnalogGyroJNI();
    }
    public void calibrateGyro() {
        gyro.calibrate();
    }
    public void resetGyro() {
        gyro.reset();
    }
    public double getAngle() {
        double theAngle = gyro.getAngle();
        return theAngle;
    }
}