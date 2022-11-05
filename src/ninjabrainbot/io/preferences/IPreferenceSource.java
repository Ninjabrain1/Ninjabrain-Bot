package ninjabrainbot.io.preferences;

public interface IPreferenceSource {

	int getInt(String key, int defaultValue);

	void putInt(String key, int value);

	float getFloat(String key, float defaultValue);

	void putFloat(String key, float value);

	boolean getBoolean(String key, boolean defaultValue);

	void putBoolean(String key, boolean value);

}
