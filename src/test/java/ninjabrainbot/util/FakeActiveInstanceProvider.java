package ninjabrainbot.util;

import ninjabrainbot.event.IObservable;
import ninjabrainbot.event.ISubscribable;
import ninjabrainbot.event.ObservableField;
import ninjabrainbot.io.mcinstance.IActiveInstanceProvider;
import ninjabrainbot.io.mcinstance.IMinecraftWorldFile;
import ninjabrainbot.io.mcinstance.MinecraftInstance;

public class FakeActiveInstanceProvider implements IActiveInstanceProvider {

	private IObservable<MinecraftInstance> currentInstance = new ObservableField<MinecraftInstance>(null);
	private IObservable<IMinecraftWorldFile> currentWorldFile = new ObservableField<IMinecraftWorldFile>(null);

	public FakeActiveInstanceProvider() {
	}

	@Override
	public IObservable<MinecraftInstance> activeMinecraftInstance() {
		return currentInstance;
	}

	@Override
	public IObservable<IMinecraftWorldFile> activeMinecraftWorld() {
		return currentWorldFile;
	}

	@Override
	public ISubscribable<IMinecraftWorldFile> whenActiveMinecraftWorldModified() {
		return currentWorldFile;
	}

}
