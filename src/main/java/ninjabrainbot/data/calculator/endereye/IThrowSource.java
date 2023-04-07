package ninjabrainbot.data.calculator.endereye;

import ninjabrainbot.data.calculator.divine.Fossil;
import ninjabrainbot.event.ISubscribable;

public interface IThrowSource {

	public ISubscribable<IThrow> whenNewThrowInputted();

	public ISubscribable<Fossil> whenNewFossilInputted();

}
