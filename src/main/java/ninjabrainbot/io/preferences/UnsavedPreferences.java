package ninjabrainbot.io.preferences;

public class UnsavedPreferences implements IPreferenceSource {

	@Override
	public int getInt(String key, int defaultValue) {
		return defaultValue;
	}

	@Override
	public void putInt(String key, int value) {
	}

	@Override
	public float getFloat(String key, float defaultValue) {
		return defaultValue;
	}

	@Override
	public void putFloat(String key, float value) {
	}

	@Override
	public double getDouble(String key, double defaultValue) {
		return defaultValue;
	}

	@Override
	public void putDouble(String key, double value) {
	}

	@Override
	public boolean getBoolean(String key, boolean defaultValue) {
		return defaultValue;
	}

	@Override
	public void putBoolean(String key, boolean value) {
	}

	@Override
	public String getString(String key, String defaultValue) {
		return defaultValue;
	}

	@Override
	public void putString(String key, String value) {
	}

}
