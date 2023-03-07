package ninjabrainbot.io.mcinstance;

import ninjabrainbot.event.IObservable;
import ninjabrainbot.event.ISubscribable;

public interface IActiveInstanceProvider {

	IObservable<MinecraftInstance> activeMinecraftInstance();

	IObservable<IMinecraftWorldFile> activeMinecraftWorld();

	ISubscribable<IMinecraftWorldFile> whenActiveMinecraftWorldModified();

}
