package ninjabrainbot.io.preferences;

import ninjabrainbot.event.Modifiable;

public class IntPreference extends Modifiable<Integer> {

	final IPreferenceSource pref;

	final String key;
	int value;
	int min;
	int max;

	
	
	public IntPreference(String key, int defaultValue, int minValue, int maxValue, IPreferenceSource pref) { // override for actual IntPreferences
		this.pref = pref;
		this.key = key;
		value = pref.getInt(key, defaultValue);
		this.min = minValue;
		this.max = maxValue;
		if (value > max)
			value = max;
		if (value < min)
			value = min;
	}


	public IntPreference(String key, int defaultValue, IPreferenceSource pref) { // This is important to keep for hotkeys, themes, et cetera.
		this.pref = pref;
		this.key = key;
		value = pref.getInt(key, defaultValue);	
	}
	
	public int get() {
		return value;
	}

	public double max() {
		return max;
	}

	public double min() {
		return min;
	}

	public void set(int value) {
		this.value = value;
		pref.putInt(key, value);
		notifySubscribers(value);
	}
	
}
