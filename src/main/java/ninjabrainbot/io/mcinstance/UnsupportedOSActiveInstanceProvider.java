package ninjabrainbot.io.mcinstance;

import ninjabrainbot.event.ISubscribable;
import ninjabrainbot.event.ObservableProperty;

public class UnsupportedOSActiveInstanceProvider implements IActiveInstanceProvider {

	private ISubscribable<MinecraftInstance> currentInstance = new ObservableProperty<MinecraftInstance>();

	UnsupportedOSActiveInstanceProvider() {
	}

	@Override
	public ISubscribable<MinecraftInstance> whenActiveMinecraftInstanceChanged() {
		return currentInstance;
	}

	@Override
	public MinecraftInstance getActiveMinecraftInstance() {
		return null;
	}

}
