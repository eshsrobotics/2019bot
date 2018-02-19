package models;

/**
 * This interface is needed to use both real and fake tankDrives to simulate our robot drive.
 * @author Aidan Galbreath
 *
 */
public interface TankDriveInterface {
	
	/**
	 * This is the model of what a tank drive does: drive with a left speed and a right speed.
	 * @param leftSpeed
	 * @param rightSpeed
	 */
	abstract public void tankDrive(double leftSpeed, double rightSpeed);
}
