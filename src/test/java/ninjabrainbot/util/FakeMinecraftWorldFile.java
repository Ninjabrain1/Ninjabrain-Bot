package ninjabrainbot.util;

import ninjabrainbot.io.mcinstance.IMinecraftWorldFile;
import ninjabrainbot.io.mcinstance.MinecraftInstance;

public class FakeMinecraftWorldFile implements IMinecraftWorldFile {

	private final MinecraftInstance minecraftInstance;
	private final String name;
	private final boolean hasEnteredEnd;

	public FakeMinecraftWorldFile(MinecraftInstance minecraftInstance, String name, boolean hasEnteredEnd) {
		this.minecraftInstance = minecraftInstance;
		this.name = name;
		this.hasEnteredEnd = hasEnteredEnd;
	}


	@Override
	public MinecraftInstance minecraftInstance() {
		return minecraftInstance;
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public boolean hasEnteredEnd() {
		return hasEnteredEnd;
	}

}
