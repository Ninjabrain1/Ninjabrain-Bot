package ninjabrainbot.model.input;

import ninjabrainbot.event.ISubscribable;
import ninjabrainbot.model.datastate.common.StructurePosition;

public interface IGeneralLocationInputSource {

	/**
	 * Notifies subscribers whenever new general coordinates have been inputted, e.g. as a result of a F3+I command.
	 */
	ISubscribable<StructurePosition> whenNewGeneralLocationInputted();

}