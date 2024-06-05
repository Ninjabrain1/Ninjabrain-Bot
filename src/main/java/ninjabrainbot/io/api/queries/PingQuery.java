package ninjabrainbot.io.api.queries;

import ninjabrainbot.Main;
import ninjabrainbot.model.datastate.IDataState;
import org.json.JSONObject;

public class PingQuery implements IQuery {

	public String get(IDataState dataState) {
		return "Ninjabrain Bot HTTP server is active!";
	}

	@Override
	public boolean supportsSubscriptions() {
		return false;
	}

}
