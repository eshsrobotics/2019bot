package models;

/**
 * It sure would be nice if there were an
 * edu.wpi.first.wpilibj.interfaces.Encoder interface to import.  But as far as
 * I know, there isn't, and we need an interface that allows our real code to
 * use either a real encoder (for the actual robot) or a fake one that doesn't
 * touch hardware (for autonomous simulations.)
 *
 * That's where this interface comes in.
 *
 * @author uakotaobi
 */
public interface EncoderInterface {

	/**
	 * This resets the encoder (real or fake);
	 */
	abstract public void reset();
	/**
	 * This returns the distance that the encoder thinks it has traveled (in feet).
	 */
	abstract public double getDistance();
	/**
	* This sets the distance traveled for each pulse of the encoder.
	*/
	abstract public void setDistancePerPulse(double distancePerPulseInFeet);
}
