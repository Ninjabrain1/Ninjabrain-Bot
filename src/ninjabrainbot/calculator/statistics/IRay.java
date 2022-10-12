package ninjabrainbot.calculator.statistics;

import ninjabrainbot.calculator.Throw;

public interface IRay {
	
	public double x();
	
	public double z();
	
	public double alpha();
	
	/**
	 * Returns the squared distance between this ray and the given ray.
	 */
	public default double distance2(Throw other) {
		double dx = x() - other.x();
		double dz = z() - other.z();
		return dx * dx + dz * dz;
	}
	
}
