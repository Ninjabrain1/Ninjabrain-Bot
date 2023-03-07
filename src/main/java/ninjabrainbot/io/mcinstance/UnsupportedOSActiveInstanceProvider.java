package ninjabrainbot.io.mcinstance;

import ninjabrainbot.event.IObservable;
import ninjabrainbot.event.ISubscribable;
import ninjabrainbot.event.ObservableField;

public class UnsupportedOSActiveInstanceProvider implements IActiveInstanceProvider {

	private IObservable<MinecraftInstance> currentInstance = new ObservableField<MinecraftInstance>(null);
	private IObservable<IMinecraftWorldFile> currentWorldFile = new ObservableField<IMinecraftWorldFile>(null);

	UnsupportedOSActiveInstanceProvider() {
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
