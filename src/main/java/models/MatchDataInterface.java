package models;

import org.usfirst.frc.team1759.robot.MatchData.Position;
import org.usfirst.frc.team1759.robot.MatchData.Target;

import edu.wpi.first.wpilibj.DriverStation;

/**
 * MatchData is a data structure that abstracts away information about where a
 * robot starts the match and where it is supposed to go during autonomous and
 * teleop mode.
 *
 * The concept of a "position" here (left, center, or right) is always done
 * from the perspective of someone looking at the field from their respective
 * alliance station.  So "left" can be the top of the field (y < 0) or the
 * bottom (y > 0), depending on which alliance you end up in.
 *
 * Each of the four interesting "horizontal strata" (the start, the near
 * switch, the scale, and the far switch) can have an indepently-assigned
 * position for our alliance.
 *
 * @author JasperNelson
 */
public interface MatchDataInterface {

	/**
	 * Returns the alliance for this particular match.
	 *
	 */
	public DriverStation.Alliance getAlliance();

	/**
	 * Gets the position from which our robot starts.
	 */
	public Position getOwnStartPosition();

	/**
	 * Returns the position of the switch on the side which is closest to our
	 * alliance's starting position (left center, or right.)
	 */
	public Position getNearSwitchPosition();

	/**
	 * Tells which side of the scale belongs to our alliance (left or right.)
	 */
	public Position getScalePosition();

	/**
	 * Returns the position of the switch on the side which is furthest from
	 * our alliance's starting positon (left center, or right.)
	 */
	public Position getFarSwitchPosition();

	/**
	 * Determines whether our target is the far switch, near switch, or
	 * center scale.
	 */
	public Target getTarget();
}
