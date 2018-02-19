package wrappers;

import models.EncoderInterface;
import edu.wpi.first.wpilibj.Encoder;

public class EncoderWrapper implements EncoderInterface {

	private Encoder encoder;
	
	public EncoderWrapper(Encoder encoder) {
		this.encoder = encoder;
	}
	
	@Override
	public void reset() {
		encoder.reset();

	}

	@Override
	public double getDistance() {
		return encoder.getDistance();
	}

}
