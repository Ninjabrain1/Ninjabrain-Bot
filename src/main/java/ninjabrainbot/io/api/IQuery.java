package ninjabrainbot.io.api;

import ninjabrainbot.model.datastate.IDataState;

public interface IQuery {

	String get(IDataState dataState);

}
