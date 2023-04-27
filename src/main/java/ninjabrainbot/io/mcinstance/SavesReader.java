package ninjabrainbot.io.mcinstance;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import ninjabrainbot.event.IObservable;
import ninjabrainbot.event.ISubscribable;
import ninjabrainbot.event.ObservableField;
import ninjabrainbot.event.ObservableProperty;

public class SavesReader {

	private final WatchService watcher;
	private WatchedInstance currentWatchedInstance;

	private final ObservableField<IMinecraftWorldFile> lastModifiedWorldFile;
	private final ObservableProperty<IMinecraftWorldFile> whenActiveWorldFileModified;
	private MinecraftWorldFile activeWorldFile;

	public SavesReader(ISubscribable<MinecraftInstance> activeMinecraftInstance) throws IOException {
		lastModifiedWorldFile = new ObservableField<>(null);
		whenActiveWorldFileModified = new ObservableProperty<>();
		watcher = FileSystems.getDefault().newWatchService();
		activeMinecraftInstance.subscribe(this::monitorMinecraftInstance);
	}

	public void pollEvents() {
		if (currentWatchedInstance != null)
			pollEventsForActiveMinecraftInstance(currentWatchedInstance.watchKey);
	}

	public IObservable<IMinecraftWorldFile> activeMinecraftWorld() {
		return lastModifiedWorldFile;
	}

	public ISubscribable<IMinecraftWorldFile> whenActiveMinecraftWorldModified() {
		return whenActiveWorldFileModified;
	}

	private void monitorMinecraftInstance(MinecraftInstance minecraftInstance) {
		if (currentWatchedInstance != null) {
			if (minecraftInstance == currentWatchedInstance.minecraftInstance)
				return;

			currentWatchedInstance.watchKey.cancel();
			currentWatchedInstance.watchKey.pollEvents();
			currentWatchedInstance = null;
		}

		Path savesDirectory = Paths.get(minecraftInstance.savesDirectory);
		try {
			WatchKey watchKey = savesDirectory.register(watcher, StandardWatchEventKinds.ENTRY_MODIFY);
			currentWatchedInstance = new WatchedInstance(minecraftInstance, watchKey);
		} catch (IOException e) {
			e.printStackTrace();
		}

//		if (!activeWorldInEachInstance.containsKey(minecraftInstance)) {
//			MinecraftWorldFile worldFile = new MinecraftWorldFile(minecraftInstance, getLatestModifiedWorldNameInSavesDirectory(minecraftInstance));
//			activeWorldInEachInstance.put(minecraftInstance, worldFile);
//		}
		activeWorldFile = new MinecraftWorldFile(minecraftInstance, null);
		lastModifiedWorldFile.set(activeWorldFile);
		onActiveMinecraftWorldFileModified();
	}

	private void pollEventsForActiveMinecraftInstance(WatchKey watchKey) {
		boolean modifiedEventAlreadyHandled = false;
		for (WatchEvent<?> event : watchKey.pollEvents()) {
			WatchEvent.Kind<?> kind = event.kind();

			if (kind == StandardWatchEventKinds.OVERFLOW) {
				continue;
			}

			if (kind == StandardWatchEventKinds.ENTRY_MODIFY && !modifiedEventAlreadyHandled) {
				modifiedEventAlreadyHandled = true;
				whenActiveMinecraftInstanceSaveDirectoryModified(event.context().toString());
			}
		}
	}

	private void whenActiveMinecraftInstanceSaveDirectoryModified(String worldName) {
		if (isWorldFileDifferentFromTheLastModifiedWorldFile(worldName)) {
			if (activeWorldFile.name() == null)
				activeWorldFile.setName(worldName);
			else
				activeWorldFile = new MinecraftWorldFile(currentWatchedInstance.minecraftInstance, worldName);
			lastModifiedWorldFile.set(activeWorldFile);
		}
		onActiveMinecraftWorldFileModified();
	}

	private boolean isWorldFileDifferentFromTheLastModifiedWorldFile(String worldName) {
		if (activeWorldFile == null)
			return true;

		if (activeWorldFile.minecraftInstance() != currentWatchedInstance.minecraftInstance)
			return true;

		return activeWorldFile.name() == null || !activeWorldFile.name().contentEquals(worldName);
	}

	private void onActiveMinecraftWorldFileModified() {
		if (!activeWorldFile.hasEnteredEnd() && activeWorldFile.getEndDimensionFile() != null && activeWorldFile.getEndDimensionFile().exists()) {
			activeWorldFile.setHasEnteredEnd(true);
		}
		whenActiveWorldFileModified.notifySubscribers(activeWorldFile);
	}

	private String getLatestModifiedWorldNameInSavesDirectory(MinecraftInstance minecraftInstance) {
		File savesDirectory = new File(minecraftInstance.savesDirectory);
		File[] worldFiles = savesDirectory.listFiles();
		if (worldFiles == null || worldFiles.length == 0) {
			return null;
		}

		File lastModifiedWorldFile = null;
		for (int i = worldFiles.length - 1; i >= 0; i--) {
			if (lastModifiedWorldFile == null || lastModifiedWorldFile.lastModified() < worldFiles[i].lastModified()) {
				if (!worldFiles[i].isDirectory())
					continue;
				lastModifiedWorldFile = worldFiles[i];
			}
		}
		return lastModifiedWorldFile.getName();
	}

}

class WatchedInstance {

	final MinecraftInstance minecraftInstance;
	final WatchKey watchKey;

	public WatchedInstance(MinecraftInstance minecraftInstance, WatchKey watchKey) {
		this.minecraftInstance = minecraftInstance;
		this.watchKey = watchKey;
	}

}