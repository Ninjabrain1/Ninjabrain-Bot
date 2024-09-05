package ninjabrainbot.io.api.queries;

import ninjabrainbot.Main;
import ninjabrainbot.model.datastate.IDataState;
import org.json.JSONObject;

public class VersionQuery implements IQuery {

	public String get(IDataState dataState) {
		JSONObject rootObject = new JSONObject();
		rootObject.put("version", Main.VERSION);
		return rootObject.toString(0);
	}

	@Override
	public boolean supportsSubscriptions() {
		return false;
	}

}
