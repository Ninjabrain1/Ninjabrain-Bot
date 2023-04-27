package ninjabrainbot.model.input;

import ninjabrainbot.model.datastate.divine.Fossil;
import ninjabrainbot.event.ISubscribable;

public interface IFossilInputSource {

	/**
	 * Notifies subscribers whenever new fossil coordinates have been inputted, e.g. as a result of a F3+I command.
	 */
	ISubscribable<Fossil> whenNewFossilInputted();

}
