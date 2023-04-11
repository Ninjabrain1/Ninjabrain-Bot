package ninjabrainbot.io.preferences;

import java.util.prefs.Preferences;

import ninjabrainbot.Main;

public class SavedPreferences implements IPreferenceSource {

	final Preferences pref;

	public SavedPreferences() {
		pref = Preferences.userNodeForPackage(Main.class);
	}

	@Override
	public int getInt(String key, int defaultValue) {
		return pref.getInt(key, defaultValue);
	}

	@Override
	public void putInt(String key, int value) {
		pref.putInt(key, value);
	}

	@Override
	public float getFloat(String key, float defaultValue) {
		return pref.getFloat(key, defaultValue);
	}

	@Override
	public void putFloat(String key, float value) {
		pref.putFloat(key, value);
	}

	@Override
	public boolean getBoolean(String key, boolean defaultValue) {
		return pref.getBoolean(key, defaultValue);
	}

	@Override
	public void putBoolean(String key, boolean value) {
		pref.putBoolean(key, value);
	}

	@Override
	public String getString(String key, String defaultValue) {
		return pref.get(key, defaultValue);
	}

	@Override
	public void putString(String key, String value) {
		pref.put(key, value);
	}

}
