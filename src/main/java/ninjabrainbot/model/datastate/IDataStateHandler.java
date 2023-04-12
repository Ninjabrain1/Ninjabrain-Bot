package ninjabrainbot.model.datastate;

import ninjabrainbot.event.ISubscribable;

public interface IDataStateHandler {

	IDataState getDataState();

	ISubscribable<IDataState> whenDataStateModified();

}
