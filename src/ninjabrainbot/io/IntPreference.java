package ninjabrainbot.io;

import java.util.prefs.Preferences;

import ninjabrainbot.gui.GUI;

public class IntPreference {

	Preferences pref;

	String key;
	int value;

	public IntPreference(String key, int defaultValue, Preferences pref) {
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
	}

	public void onChangedByUser(GUI gui) { }

}