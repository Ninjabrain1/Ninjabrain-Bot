package ninjabrainbot.io.mcinstance;

import java.io.File;

import ninjabrainbot.io.preferences.enums.McVersion;

public class MinecraftInstance {

	public final String dotMinecraftDirectory;
	public final String savesDirectory;
	public final boolean isRanked;
	McVersion minecraftVersion;

	public MinecraftInstance(String dotMinecraftDirectory) {
		if (dotMinecraftDirectory == null)
			throw new IllegalArgumentException(".minecraft directory cannot be null");
		this.dotMinecraftDirectory = dotMinecraftDirectory;
		savesDirectory = dotMinecraftDirectory + "\\saves";
		minecraftVersion = null;
		isRanked = isRanked();
	}

	public McVersion getMcVersion() {
		return minecraftVersion;
	}

	private boolean isRanked(){
		File modsDirectory = new File(dotMinecraftDirectory + "\\mods");
		File[] modFiles = modsDirectory.listFiles();
		if (modFiles == null)
			return false;

		for (File modFile : modFiles){
			String modFileName = modFile.getName();
			if (modFileName.contains("mcsrranked") && modFileName.endsWith(".jar"))
				return true;
		}
		return false;
	}

}
