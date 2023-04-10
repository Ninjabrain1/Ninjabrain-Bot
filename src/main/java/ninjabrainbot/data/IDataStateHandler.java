package ninjabrainbot.data;

import ninjabrainbot.event.ISubscribable;

public interface IDataStateHandler {

	IDataState getDataState();

	ISubscribable<IDataState> whenDataStateModified();

}
