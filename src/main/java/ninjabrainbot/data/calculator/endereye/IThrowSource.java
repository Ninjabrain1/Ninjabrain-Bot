package ninjabrainbot.data.calculator.endereye;

import ninjabrainbot.data.calculator.divine.Fossil;
import ninjabrainbot.event.ISubscribable;

public interface IThrowSource {

	public ISubscribable<IThrow> whenNewThrowInputed();

	public ISubscribable<Fossil> whenNewFossilInputed();

}
