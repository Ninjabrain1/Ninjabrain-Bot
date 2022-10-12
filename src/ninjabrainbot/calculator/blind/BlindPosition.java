package ninjabrainbot.calculator.blind;

import ninjabrainbot.calculator.statistics.IRay;

public class BlindPosition {
	public final double x, z;

	public BlindPosition(double x, double z) {
		this.x = x;
		this.z = z;
	}
	
	public BlindPosition(IRay ray) {
		this(ray.x(), ray.z());
	}
	
}
