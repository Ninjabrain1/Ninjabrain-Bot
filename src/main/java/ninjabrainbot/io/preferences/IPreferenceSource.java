package ninjabrainbot.io.preferences;

public interface IPreferenceSource {

	int getInt(String key, int defaultValue);

	void putInt(String key, int value);

	float getFloat(String key, float defaultValue);

	void putFloat(String key, float value);

	double getDouble(String key, double defaultValue);

	void putDouble(String key, double value);

	boolean getBoolean(String key, boolean defaultValue);

	void putBoolean(String key, boolean value);

	String getString(String key, String defaultValue);

	void putString(String key, String value);

}
