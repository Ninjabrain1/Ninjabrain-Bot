package ninjabrainbot.model.input;

import ninjabrainbot.event.ISubscribable;
import ninjabrainbot.model.datastate.endereye.F3IData;

public interface IF3ILocationInputSource {

	/**
	 * Notifies subscribers whenever new F3+I coordinates have been inputted.
	 */
	ISubscribable<F3IData> whenNewF3ILocationInputted();

}