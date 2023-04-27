package ninjabrainbot.io.preferences;

import ninjabrainbot.event.Modifiable;

public class StringPreference extends Modifiable<String> {

	final IPreferenceSource pref;

	final String key;
	String value;

	public StringPreference(String key, String defaultValue, IPreferenceSource pref) {
		this.pref = pref;
		this.key = key;
		value = pref.getString(key, defaultValue);
	}

	public String get() {
		return value;
	}

	public void set(String value) {
		this.value = value;
		pref.putString(key, value);
		notifySubscribers(value);
	}

}