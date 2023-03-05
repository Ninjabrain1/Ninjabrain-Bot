package ninjabrainbot.io.mcinstance;

import ninjabrainbot.event.ISubscribable;

public interface IActiveInstanceProvider {

	ISubscribable<MinecraftInstance> whenActiveMinecraftInstanceChanged();

	MinecraftInstance getActiveMinecraftInstance();

}
