package ninjabrainbot.io;

import java.util.prefs.Preferences;

import ninjabrainbot.gui.GUI;

public class BooleanPreference {

	Preferences pref;

	String key;
	boolean value;

	public BooleanPreference(String key, boolean defaultValue, Preferences pref) {
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
	}
	
	public void onChangedByUser(GUI gui) {}

}