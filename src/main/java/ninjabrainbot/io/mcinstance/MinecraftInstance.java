package ninjabrainbot.io.mcinstance;

import java.io.File;
import java.nio.file.Paths;

import ninjabrainbot.io.preferences.enums.McVersion;

public class MinecraftInstance {

	public final String minecraftDirectory;
	public final String savesDirectory;
	public final boolean isRanked;
	private McVersion minecraftVersion;

	public MinecraftInstance(String minecraftDirectory) {
		if (minecraftDirectory == null)
			throw new IllegalArgumentException(".minecraft directory cannot be null");
		this.minecraftDirectory = minecraftDirectory;
		savesDirectory = Paths.get(minecraftDirectory, "saves").toString();
		minecraftVersion = null;
		isRanked = isRanked();
	}

	public McVersion getMcVersion() {
		return minecraftVersion;
	}

	public void setMcVersion(McVersion minecraftVersion) {
		this.minecraftVersion = minecraftVersion;
	}

	private boolean isRanked() {
		File modsDirectory = Paths.get(minecraftDirectory, "mods").toFile();
		File[] modFiles = modsDirectory.listFiles();
		if (modFiles == null)
			return false;

		for (File modFile : modFiles) {
			String modFileName = modFile.getName();
			if (modFileName.contains("mcsrranked") && modFileName.endsWith(".jar"))
				return true;
		}
		return false;
	}

}
