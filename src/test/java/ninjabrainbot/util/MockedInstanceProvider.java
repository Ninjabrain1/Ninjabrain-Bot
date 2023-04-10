package ninjabrainbot.util;

import ninjabrainbot.event.IObservable;
import ninjabrainbot.event.ISubscribable;
import ninjabrainbot.event.ObservableField;
import ninjabrainbot.io.mcinstance.IActiveInstanceProvider;
import ninjabrainbot.io.mcinstance.IMinecraftWorldFile;
import ninjabrainbot.io.mcinstance.MinecraftInstance;

public class MockedInstanceProvider implements IActiveInstanceProvider {

	public final ObservableField<MinecraftInstance> currentInstance = new ObservableField<MinecraftInstance>(null);
	public final ObservableField<IMinecraftWorldFile> currentWorldFile = new ObservableField<IMinecraftWorldFile>(null);

	public MockedInstanceProvider() {
	}

	@Override
	public ObservableField<MinecraftInstance> activeMinecraftInstance() {
		return currentInstance;
	}

	@Override
	public ObservableField<IMinecraftWorldFile> activeMinecraftWorld() {
		return currentWorldFile;
	}

	@Override
	public ObservableField<IMinecraftWorldFile> whenActiveMinecraftWorldModified() {
		return currentWorldFile;
	}

}
