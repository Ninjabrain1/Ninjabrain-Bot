package ninjabrainbot.data.blind;

import ninjabrainbot.data.stronghold.Ring;
import ninjabrainbot.util.Pair;

public class BlindResult {

	public final double x, z;
	public final double highrollProbability;
	public final double highrollThreshold;
	public final double avgDistance;
	public final double avgDistanceDerivative;
	public final double ninetiethPercentileDerivative;
	public final double improveDirection;
	public final double improveDistance;
	public final double optHighrollProb;
	
	public static final Pair<Float, String> EXCELLENT = new Pair<Float, String>(1.0f, "blind_excellent");
	public static final Pair<Float, String> HIGHROLL_GOOD = new Pair<Float, String>(0.9f, "blind_good_highroll");
	public static final Pair<Float, String> HIGHROLL_SUFFICIENT = new Pair<Float, String>(0.7f, "blind_okay_highroll");
	public static final Pair<Float, String> UNDESIRABLE = new Pair<Float, String>(0.5f, "blind_bad_in_ring");
	public static final Pair<Float, String> BAD = new Pair<Float, String>(0.2f, "blind_bad");
	public static final Pair<Float, String> NOT_IN_RING = new Pair<Float, String>(0.0f, "blind_not_in_ring");
	
	/**
	 * Creates a blind result.
	 */
	public BlindResult(double x, double z, double highrollProbability, double highrollThreshold, double avgDistance, double avgDistanceDerivative, double ninetiethPercentileDerivative, double improveDirection, double improveDistance, double optHighrollProb) {
		this.x = x;
		this.z = z;
		this.highrollProbability = highrollProbability;
		this.highrollThreshold = highrollThreshold;
		this.avgDistance = avgDistance;
		this.avgDistanceDerivative = !Double.isNaN(avgDistanceDerivative) ? avgDistanceDerivative : 0;
		this.ninetiethPercentileDerivative = !Double.isNaN(ninetiethPercentileDerivative) ? ninetiethPercentileDerivative : 0;
		this.optHighrollProb = optHighrollProb;
		this.improveDistance = improveDistance;
		this.improveDirection = improveDirection;
	}
	
	public Pair<Float, String> evaluation() {
		if (Ring.get(Math.sqrt(x * x + z * z) / 2.0) == null)
			return NOT_IN_RING;
		if (highrollProbability / optHighrollProb < 0.01) // prevents divine from showing "Excellent" when not in a sector
			return BAD;
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
