package org.usfirst.frc.team1759.robot;

import models.MatchDataInterface;
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
public class MatchData implements MatchDataInterface {
	public enum Position {
		LEFT,
		RIGHT,
		CENTER
	};

	public enum Target {
		CLOSE_SWITCH,
		FAR_SWITCH,
		SCALE
	};

	private DriverStation driverStation;

	// The position of our side of the switch closest to us
	private Position nearSwitchPosition;
	// The position of our side of the switch farthest from us
	private Position farSwitchPosition;
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
		nearSwitchPosition = getPosition(positionString.charAt(0));
		scalePosition = getPosition(positionString.charAt(1));
		farSwitchPosition = getPosition(positionString.charAt(2));
	}

	@Override
	public DriverStation.Alliance getAlliance() {
		return driverStation.getAlliance();
	}

	@Override
	public Position getNearSwitchPosition() {
		return nearSwitchPosition;
	}

	@Override
	public Position getFarSwitchPosition() {
		return farSwitchPosition;
	}

	@Override
	public Position getScalePosition() {
		return scalePosition;
	}

	@Override
	public Position getOwnStartPosition() {
		// Hardcoded temporarily, should read from a switch in the final version
		return Position.CENTER;
	}

	@Override
	public Target getTarget() {
		return Target.CLOSE_SWITCH;
	}

	private Position getPosition(char leftRightChar) {
		return leftRightChar == 'L' ? Position.LEFT : Position.RIGHT;
	}
}
