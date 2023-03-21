package ninjabrainbot.io.mcinstance;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashMap;

import ninjabrainbot.event.IObservable;
import ninjabrainbot.event.ISubscribable;
import ninjabrainbot.event.ObservableField;
import ninjabrainbot.event.ObservableProperty;

public class SavesReader {

	private WatchService watcher;
	private WatchedInstance currentWatchedInstance;
	private HashMap<MinecraftInstance, MinecraftWorldFile> activeWorldInEachInstance;

	private ObservableField<IMinecraftWorldFile> lastModifiedWorldFile;
	private ObservableProperty<IMinecraftWorldFile> whenActiveWorldFileModified;
	private MinecraftWorldFile activeWorldFile;

	public SavesReader(ISubscribable<MinecraftInstance> activeMinecraftInstance) throws IOException {
		activeWorldInEachInstance = new HashMap<>();
		lastModifiedWorldFile = new ObservableField<IMinecraftWorldFile>(null);
		whenActiveWorldFileModified = new ObservableProperty<>();
		watcher = FileSystems.getDefault().newWatchService();
		activeMinecraftInstance.subscribe(activeInstance -> monitorMinecraftInstance(activeInstance));
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

		Path savesDirectory = Path.of(minecraftInstance.savesDirectory);
		try {
			WatchKey watchKey = savesDirectory.register(watcher, StandardWatchEventKinds.ENTRY_MODIFY);
			currentWatchedInstance = new WatchedInstance(minecraftInstance, watchKey);
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (!activeWorldInEachInstance.containsKey(minecraftInstance)) {
			MinecraftWorldFile worldFile = new MinecraftWorldFile(minecraftInstance, getLatestModifiedWorldNameInSavesDirectory(minecraftInstance));
			activeWorldInEachInstance.put(minecraftInstance, worldFile);
		}
		activeWorldFile = activeWorldInEachInstance.get(minecraftInstance);
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

		if (!activeWorldFile.name().contentEquals(worldName.toString()))
			return true;

		return false;
	}

	private void onActiveMinecraftWorldFileModified() {
		if (!activeWorldFile.hasEnteredEnd() && activeWorldFile.getEndDimensionFile().exists()) {
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