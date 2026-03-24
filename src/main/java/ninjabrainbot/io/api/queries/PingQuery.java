package ninjabrainbot.io.api.queries;

import ninjabrainbot.io.api.interfaces.IQuery;

public class PingQuery implements IQuery {

	public String get() {
		return "Ninjabrain Bot HTTP server is active!";
	}

	@Override
	public boolean supportsSubscriptions() {
		return false;
	}

}
