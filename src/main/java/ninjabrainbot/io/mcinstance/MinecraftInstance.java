package ninjabrainbot.io.mcinstance;

import ninjabrainbot.io.preferences.enums.McVersion;

public class MinecraftInstance {

	public final String dotMinecraftDirectory;
	public final String savesDirectory;
	McVersion minecraftVersion;

	public MinecraftInstance(String dotMinecraftDirectory) {
		if (dotMinecraftDirectory == null)
			throw new IllegalArgumentException(".minecraft directory cannot be null");
		this.dotMinecraftDirectory = dotMinecraftDirectory;
		savesDirectory = dotMinecraftDirectory + "\\saves";
		minecraftVersion = null;
	}

	public McVersion getMcVersion() {
		return minecraftVersion;
	}

}
