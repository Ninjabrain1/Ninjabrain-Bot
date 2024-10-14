package ninjabrainbot.io.api.queries;

import ninjabrainbot.Main;
import org.json.JSONObject;

public class VersionQuery implements IQuery {

	public String get() {
		JSONObject rootObject = new JSONObject();
		rootObject.put("version", Main.VERSION);
		return rootObject.toString(0);
	}

	@Override
	public boolean supportsSubscriptions() {
		return false;
	}

}
