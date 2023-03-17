package ninjabrainbot.data.endereye;

import ninjabrainbot.data.divine.Fossil;
import ninjabrainbot.event.ISubscribable;

public interface IThrowSource {

	public ISubscribable<IThrow> whenNewThrowInputed();

	public ISubscribable<Fossil> whenNewFossilInputed();

}
