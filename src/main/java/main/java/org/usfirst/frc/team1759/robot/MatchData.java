package org.usfirst.frc.team1759.robot;

import edu.wpi.first.wpilibj.DriverStation;

/** 
 This class provides a more convenient interface to access match-specific data
 
 Alliance can be RED, BLUE, or INVALID
 Position is always from the perspective of looking down the field from our side
 Switch 1 is always the switch closer to us
 
 Example of values
 
 getAlliance() - returning DriverStation.Alliance.RED
 getSwitch1Position() - returning Position.RIGHT
 getScalePosition()   - returning Position.LEFT
 getSwitch2Position() - returning Position.RIGHT
 
 B for blue-lit objects
 R for red-lit objects
           +------------------------------------+
           | switch1      scale       switch2   |
           |   B            R           B       |  
Our side   |   |            |           |       |  Other side
  Red      |   R            B           R       |     Blue
           |                                    |
           +------------------------------------+
 
 @author Andrew McLees
 */
public class MatchData { 
  public enum Position {
    LEFT,
    RIGHT,
    CENTER
  };
  
  private DriverStation driverStation;
  
  // The position of our side of the switch closest to us
  private Position switch1Position;
  // The position of our side of the switch farthest from us
  private Position switch2Position;
  // The position of our side of the scale
  private Position scalePosition;
  // The position of our robot
  private Position ourPosition;
  
  public MatchData(DriverStation driverStation) {
    if (driverStation == null) {
      throw new IllegalArgumentException();
    }
    this.driverStation = driverStation;
    
    String positionString = driverStation.getGameSpecificMessage();
    if (positionString == null || positionString.length() != 3) {
      positionString = "LLL";
      System.out.println("No position string returned by driver station: defaulting to string of \"" + positionString + "\"");
    }
    switch1Position = getPosition(positionString.charAt(0));
    scalePosition = getPosition(positionString.charAt(1));
    switch2Position = getPosition(positionString.charAt(2));
  }
  
  public DriverStation.Alliance getAlliance() {
    return driverStation.getAlliance();
  }
  
  public Position getSwitch1Position() {
    return switch1Position;
  }
  
  public Position getSwitch2Position() {
    return switch2Position;
  }
  
  public Position getScalePosition() {
    return scalePosition;
  }
  
  public Position getOwnStartPosition() {
	  // Hardcoded temporarily, should read from a switch in the final version
	  return Position.LEFT;
  }
  
  private Position getPosition(char leftRightChar) {
    return leftRightChar == 'L' ? Position.LEFT : Position.RIGHT;
  }
}
