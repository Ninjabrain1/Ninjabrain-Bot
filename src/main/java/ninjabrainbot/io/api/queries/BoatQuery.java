package ninjabrainbot.io.api.queries;

import ninjabrainbot.model.datastate.IDataState;
import org.json.JSONObject;

public class BoatQuery implements IQuery {

	private final IDataState dataState;
	private final boolean isPretty;

	public BoatQuery(IDataState dataState) {
		this(dataState, false);
	}

	public BoatQuery(IDataState dataState, boolean isPretty) {
		this.dataState = dataState;
		this.isPretty = isPretty;
	}

	public String get() {
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
