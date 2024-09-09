package ninjabrainbot.model.domainmodel;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.io.savestate.IFileAccessor;
import ninjabrainbot.util.Logger;

public class DomainModelImportExportService {

	private final IDomainModel domainModel;
	private final IFileAccessor fileAccessor;
	private final NinjabrainBotPreferences preferences;

	public DomainModelImportExportService(IDomainModel domainModel, IFileAccessor fileAccessor, NinjabrainBotPreferences preferences) {
		this.domainModel = domainModel;
		this.fileAccessor = fileAccessor;
		this.preferences = preferences;
	}

	public void triggerDeserialization() {
		if (!preferences.saveState.get())
			return;

		ObjectInputStream objectInputStream = null;
		try {
			objectInputStream = fileAccessor.getObjectInputStream();
		} catch (IOException e) {
			Logger.log("Domain model deserialization failed, failed to read file: " + e);
			return;
		}

		try {
			domainModel.deserialize(objectInputStream);
			try {
				objectInputStream.close();
			} catch (IOException ex) {
				Logger.log("Failed to close ObjectInputStream after successful deserialization: " + ex);
			}
		} catch (SerializationException e) {
			Logger.log("Domain model deserialization failed: " + e);
			try {
				objectInputStream.close();
				fileAccessor.deleteFile();
			} catch (IOException ex) {
				Logger.log("Failed to close ObjectInputStream after failed deserialization: " + ex);
			}
			if (!domainModel.isReset())
				throw new RuntimeException("Domain model is not in the default state after failed deserialization, crashing the application to avoid an inconsistent data state.");
		} catch (Exception e) {
			Logger.log("Unhandled exception during domain model deserialization: " + e);
			try {
				objectInputStream.close();
				fileAccessor.deleteFile();
			} catch (IOException ex) {
				Logger.log("Failed to close ObjectInputStream after failed deserialization: " + ex);
				fileAccessor.deleteFile();
			}
			throw e;
		}
	}

	private void triggerSerialization() {
		ObjectOutputStream objectOutputStream = null;
		try {
			objectOutputStream = fileAccessor.getObjectOutputStream();
		} catch (IOException e) {
			Logger.log("Domain model serialization failed, failed to open file: " + e);
			return;
		}

		try {
			domainModel.serialize(objectOutputStream);
			try {
				objectOutputStream.flush();
				objectOutputStream.close();
			} catch (IOException ex) {
				Logger.log("Failed to close ObjectInputStream after successful serialization: " + ex);
			}
		} catch (SerializationException e) {
			Logger.log("Domain model serialization failed: " + e);
			try {
				objectOutputStream.flush();
				objectOutputStream.close();
				fileAccessor.deleteFile();
			} catch (IOException ex) {
				Logger.log("Failed to close ObjectInputStream after failed serialization: " + ex);
			}
		}
	}

	public void onShutdown() {
		triggerSerialization();
	}
}
