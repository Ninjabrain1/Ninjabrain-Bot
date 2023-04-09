package ninjabrainbot.data;

import ninjabrainbot.event.ISubscribable;

public interface IDataStateHandler {

	IDataState getDataState();

	void toggleAltStdOnLastThrowIfNotLocked();

	ISubscribable<IDataState> whenDataStateModified();

}
