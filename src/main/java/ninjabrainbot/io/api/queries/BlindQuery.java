package ninjabrainbot.io.api.queries;

import ninjabrainbot.model.datastate.IDataState;
import ninjabrainbot.model.datastate.blind.BlindResult;
import ninjabrainbot.model.datastate.common.ResultType;
import org.json.JSONObject;

public class BlindQuery implements IQuery {

	private final boolean isPretty;

	public BlindQuery() {
		this(false);
	}

	public BlindQuery(boolean isPretty) {
		this.isPretty = isPretty;
	}

	public String get(IDataState dataState) {
		JSONObject rootObject = new JSONObject();
		rootObject.put("isBlindModeEnabled", dataState.resultType().get() == ResultType.BLIND);
		rootObject.put("hasDivine", dataState.getDivineContext().hasDivine());
		rootObject.put("blindResult", convertPosition(dataState.blindResult().get()));
		return rootObject.toString(isPretty ? 4 : 0);
	}

	@Override
	public boolean supportsSubscriptions() {
		return true;
	}

	private JSONObject convertPosition(BlindResult blindResult) {
		JSONObject blindResultObject = new JSONObject();
		if (blindResult == null)
			return blindResultObject;

		blindResultObject.put("xInNether", blindResult.x);
		blindResultObject.put("zInNether", blindResult.z);
		blindResultObject.put("highrollProbability", blindResult.highrollProbability);
		blindResultObject.put("highrollThreshold", blindResult.highrollThreshold);
		blindResultObject.put("averageDistance", blindResult.averageDistance);
		blindResultObject.put("improveDirection", blindResult.improveDirection);
		blindResultObject.put("improveDistance", blindResult.improveDistance);
		return blindResultObject;
	}

}
