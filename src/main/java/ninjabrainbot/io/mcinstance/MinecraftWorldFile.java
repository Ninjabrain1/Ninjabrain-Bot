package ninjabrainbot.io.mcinstance;

import java.io.File;
import java.nio.file.Paths;

public class MinecraftWorldFile implements IMinecraftWorldFile {

	private final MinecraftInstance minecraftInstance;
	// Name can be null if the name has not been found out yet
	private String name;
	private boolean hasEnteredEnd;

	private File endDimensionFile;

	private static final String[] END_DIMENSION_REGION_NEW = {"dimensions", "minecraft", "the_end", "region"};
	private static final String[] END_DIMENSION_REGION_LEGACY = {"DIM1", "region"};
	private static final String[] LEGACY_LAYOUT_MARKER = {"region"};
	private static final String[] NEW_LAYOUT_MARKER = {"dimensions", "minecraft"};

	public MinecraftWorldFile(MinecraftInstance minecraftInstance, String name) {
		this.minecraftInstance = minecraftInstance;
		this.name = name;
		hasEnteredEnd = false;
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

	public void setName(String name) {
		this.name = name;
	}

	File getEndDimensionFile() {
		if (name == null)
			return null;

		if (worldPath(LEGACY_LAYOUT_MARKER).exists())
			endDimensionFile = worldPath(END_DIMENSION_REGION_LEGACY);

		if (worldPath(NEW_LAYOUT_MARKER).exists())
			endDimensionFile = worldPath(END_DIMENSION_REGION_NEW);

		if (endDimensionFile != null)
			return endDimensionFile;

		return null;
	}

	private File worldPath(String[] parts) {
		return Paths.get(minecraftInstance.savesDirectory).resolve(Paths.get(name, parts)).toFile();
	}

	void setHasEnteredEnd(boolean hasEnteredEnd) {
		this.hasEnteredEnd = hasEnteredEnd;
	}

	@Override
	public String toString() {
		return Paths.get(minecraftInstance.savesDirectory, name != null ? name : "").toString();
	}

}
