package models;

public interface EncoderInterface {

	/**
	 * This resets the encoder (real or fake);
	 */
	abstract public void reset();
	/**
	 * This returns the distance that the encoder thinks it has traveled (in feet).
	 * @return
	 */
	abstract public double getDistance();
	
}
