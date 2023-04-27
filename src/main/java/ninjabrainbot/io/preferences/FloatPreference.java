package ninjabrainbot.io.preferences;

import ninjabrainbot.event.Modifiable;

public class FloatPreference extends Modifiable<Float> {

	final IPreferenceSource pref;

	final String key;
	float value;
	final float max;
	final float min;

	public FloatPreference(String key, float defaultValue, float minValue, float maxValue, IPreferenceSource pref) {
		this.pref = pref;
		this.key = key;
		value = pref.getFloat(key, defaultValue);
		this.min = minValue;
		this.max = maxValue;
		if (value > max)
			value = max;
		if (value < min)
			value = min;
	}

	public float get() {
		return value;
	}

	public float max() {
		return max;
	}

	public float min() {
		return min;
	}

	public void set(float value) {
		if (value > max)
			value = max;
		if (value < min)
			value = min;
		this.value = value;
		pref.putFloat(key, value);
		notifySubscribers(value);
	}

}