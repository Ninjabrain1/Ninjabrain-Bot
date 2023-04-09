package ninjabrainbot.data.input;

import ninjabrainbot.data.calculator.divine.Fossil;
import ninjabrainbot.event.ISubscribable;

public interface IFossilInputSource {

	/**
	 * Notifies subscribers whenever new fossil coordinates have been inputted, e.g. as a result of a F3+I command.
	 */
	ISubscribable<Fossil> whenNewFossilInputted();

}
