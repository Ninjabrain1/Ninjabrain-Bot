package ninjabrainbot.io.api.queries;

import ninjabrainbot.model.datastate.IDataState;

public class PingQuery implements IQuery {

	public String get(IDataState dataState) {
		return "Ninjabrain Bot HTTP server is active!";
	}

	@Override
	public boolean supportsSubscriptions() {
		return false;
	}

}
