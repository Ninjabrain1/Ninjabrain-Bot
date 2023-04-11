package ninjabrainbot.io.preferences;

import ninjabrainbot.event.Modifiable;

public class BooleanPreference extends Modifiable<Boolean> {

	final IPreferenceSource pref;

	final String key;
	boolean value;

	public BooleanPreference(String key, boolean defaultValue, IPreferenceSource pref) {
		this.pref = pref;
		this.key = key;
		value = pref.getBoolean(key, defaultValue);
	}

	public boolean get() {
		return value;
	}

	public void set(boolean value) {
		this.value = value;
		pref.putBoolean(key, value);
		notifySubscribers(value);
	}

}