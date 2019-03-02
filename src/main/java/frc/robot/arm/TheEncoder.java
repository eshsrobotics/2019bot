package frc.robot.arm;

import java.lang.invoke.SwitchPoint;

import  edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.arm.*;
import frc.robot.driving.*;

public class TheEncoder {
    private static Encoder encoder;
    private static DigitalInput in1;
    private static DigitalInput in2;
    public TheEncoder() {
        //encoder = new Encoder(RobotMap.ENCODER_PORT_A,RobotMap.ENCODER_PORT_B);
        //encoder = new Encoder(RobotMap.ENCODER_PORT_A,RobotMap.ENCODER_PORT_B);
        //encoder = new Encoder(5,6);
        in1 = new DigitalInput(9);
        in2 = new DigitalInput(6);
       // encoder.setMaxPeriod(1);
        //encoder.setMinRate(1);
       // encoder.setDistancePerPulse(5);
       // encoder.setReverseDirection(true);
        //encoder.setSamplesToAverage(7);
        //encoder.reset();
        //encoder.setIndexSource(8);
        //encoder.getRaw()
    }
    public int getDistance() {
       // System.out.println("" + " and " + encoder.getDistance());
        int h = 0;
        int h2 = 0;
        if (in1.get() == true) {
            h = 1;
        } else {
            h = 0;
        }
        if (in2.get() == true) {
            h2 = 1;
        } else {
            h2 = 0;
        }
       // SmartDashboard.
        SmartDashboard.putNumber("In1", h);
        SmartDashboard.putNumber("In2", h2);
        SmartDashboard.updateValues();
        return h + h2;
    }
    public void resetEncoder() {
        encoder.reset();
        System.out.println("Encoder reset");
    }
    
    
}