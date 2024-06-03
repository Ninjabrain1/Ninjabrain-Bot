package ninjabrainbot.io.api.queries;

import ninjabrainbot.model.datastate.IDataState;

public interface IQuery {

	String get(IDataState dataState);

}
