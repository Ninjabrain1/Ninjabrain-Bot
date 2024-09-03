package ninjabrainbot.io.api.queries;

import ninjabrainbot.model.datastate.IDataState;
import ninjabrainbot.model.datastate.highprecision.IBoatDataState;
import org.json.JSONObject;

public class BoatQuery implements IQuery {

	private final boolean isPretty;

	public BoatQuery() {
		this(false);
	}

	public BoatQuery(boolean isPretty) {
		this.isPretty = isPretty;
	}

	public String get(IDataState dataState) {
		JSONObject rootObject = new JSONObject();
		rootObject.put("boatAngle", dataState.boatDataState().boatAngle().get());
		rootObject.put("boatState", dataState.boatDataState().boatState().get());
		return rootObject.toString(isPretty ? 4 : 0);
	}

	@Override
	public boolean supportsSubscriptions() {
		return true;
	}

}
