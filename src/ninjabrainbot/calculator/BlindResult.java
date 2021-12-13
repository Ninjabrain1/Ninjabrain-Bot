package ninjabrainbot.calculator;

import java.util.Locale;

public class BlindResult {

	public final double highrollProbability;
	public final double highrollThreshold;
	
	/**
	 * Creates a blind result.
	 */
	public BlindResult(double highrollProbability, double highrollThreshold) {
		this.highrollProbability = highrollProbability;
		this.highrollThreshold = highrollThreshold;
	}
	
	public String format() {
		return String.format(Locale.US, "%.1f%% chance of <%d block blind", highrollProbability*100, (int) highrollThreshold);
	}

}
