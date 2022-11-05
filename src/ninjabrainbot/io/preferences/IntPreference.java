package ninjabrainbot.io.preferences;

import ninjabrainbot.event.Modifiable;

public class IntPreference extends Modifiable<Integer> {

	IPreferenceSource pref;

	String key;
	int value;

	public IntPreference(String key, int defaultValue, IPreferenceSource pref) {
		this.pref = pref;
		this.key = key;
		value = pref.getInt(key, defaultValue);
	}

	public int get() {
		return value;
	}

	public void set(int value) {
		this.value = value;
		pref.putInt(key, value);
		notifySubscribers(value);
	}

}