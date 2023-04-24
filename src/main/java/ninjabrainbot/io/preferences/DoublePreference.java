package ninjabrainbot.io.preferences;

import ninjabrainbot.event.Modifiable;

public class DoublePreference extends Modifiable<Double> {

	final IPreferenceSource pref;

	final String key;
	double value;
	final double max;
	final double min;

	public DoublePreference(String key, double defaultValue, double minValue, double maxValue, IPreferenceSource pref) {
		this.pref = pref;
		this.key = key;
		value = pref.getDouble(key, defaultValue);
		this.min = minValue;
		this.max = maxValue;
		if (value > max)
			value = max;
		if (value < min)
			value = min;
	}

	public double get() {
		return value;
	}

	public double max() {
		return max;
	}

	public double min() {
		return min;
	}

	public void set(double value) {
		if (value > max)
			value = max;
		if (value < min)
			value = min;
		this.value = value;
		pref.putDouble(key, value);
		notifySubscribers(value);
	}

}