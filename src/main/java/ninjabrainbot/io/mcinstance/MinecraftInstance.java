package ninjabrainbot.io.mcinstance;

import ninjabrainbot.io.preferences.MultipleChoicePreferenceDataTypes.McVersion;

public class MinecraftInstance {

	public final String directory;
	McVersion minecraftVersion;

	public MinecraftInstance(String directory) {
		this.directory = directory;
		minecraftVersion = null;
	}

	public McVersion getMcVersion() {
		return minecraftVersion;
	}

}
