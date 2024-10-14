package ninjabrainbot.io.api.queries;

public class PingQuery implements IQuery {

	public String get() {
		return "Ninjabrain Bot HTTP server is active!";
	}

	@Override
	public boolean supportsSubscriptions() {
		return false;
	}

}
