package ninjabrainbot.io.mcinstance;

import ninjabrainbot.io.preferences.enums.McVersion;

public class McInstanceTestAdapter {

	public static void setMinecraftInstanceVersion(MinecraftInstance minecraftInstance, McVersion mcVersion){
		minecraftInstance.minecraftVersion = mcVersion;
	}

}
