package models;

/**
 * 
 * @author Aidan Galbreath
 * 
 * This class is used to house various mathematical and program-specific constants.
 *
 */

public class Constants {
	public static final double RADIANS_TO_DEGREES = 180 / Math.PI;
	public static final double DEGREES_TO_RADIANS = Math.PI / 180;
	
	public static final int MILLI_TO_SEC = 1 / 1000;
	
	public static final Point ORIGIN = new Point(0, 0);
}
