package ninjabrainbot.calculator;

import ninjabrainbot.util.Pair;

public class BlindResult {

	public final double x, z;
	public final double highrollProbability;
	public final double highrollThreshold;
	public final double avgDistance;
	public final double avgDistanceDerivative;
	public final double ninetiethPercentileDerivative;
	
	private static final Pair<Float, String> EXCELLENT = new Pair<Float, String>(1.0f, "excellent");
	private static final Pair<Float, String> HIGHROLL_GOOD = new Pair<Float, String>(0.8f, "good for highroll");
	private static final Pair<Float, String> HIGHROLL_SUFFICIENT = new Pair<Float, String>(0.6f, "okay for highroll");
	private static final Pair<Float, String> UNDESIRABLE = new Pair<Float, String>(0.4f, "bad, but in ring");
	private static final Pair<Float, String> BAD = new Pair<Float, String>(0.2f, "bad");
	private static final Pair<Float, String> NOT_IN_RING = new Pair<Float, String>(0.0f, "not in any ring");
	
	/**
	 * Creates a blind result.
	 */
	public BlindResult(double x, double z, double highrollProbability, double highrollThreshold, double avgDistance, double avgDistanceDerivative, double ninetiethPercentileDerivative) {
		this.x = x;
		this.z = z;
		this.highrollProbability = highrollProbability;
		this.highrollThreshold = highrollThreshold;
		this.avgDistance = avgDistance;
		this.avgDistanceDerivative = !Double.isNaN(avgDistanceDerivative) ? avgDistanceDerivative : 0;
		this.ninetiethPercentileDerivative = !Double.isNaN(ninetiethPercentileDerivative) ? ninetiethPercentileDerivative : 0;
	}
	
	public Pair<Float, String> evaluation() {
		if (Ring.get(Math.sqrt(x * x + z * z) / 2.0) == null)
			return NOT_IN_RING;
		double avg_speed = Math.abs(avgDistanceDerivative);
		double highroll_speed = Math.abs(ninetiethPercentileDerivative);
		if (avg_speed < 1 && highroll_speed < 1)
			return EXCELLENT;
		if (highroll_speed < 2f)
			return HIGHROLL_GOOD;
		if (highroll_speed < 4f)
			return HIGHROLL_SUFFICIENT;
		if (highroll_speed < 8f)
			return UNDESIRABLE;
		return BAD;
	}

}
