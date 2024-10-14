package ninjabrainbot.io.api.queries;

import ninjabrainbot.model.datastate.IDataState;
import ninjabrainbot.model.datastate.blind.BlindResult;
import ninjabrainbot.model.datastate.common.ResultType;
import org.json.JSONObject;

public class BlindQuery implements IQuery {

	private final IDataState dataState;
	private final boolean isPretty;

	public BlindQuery(IDataState dataState) {
		this(dataState, false);
	}

	public BlindQuery(IDataState dataState, boolean isPretty) {
		this.dataState = dataState;
		this.isPretty = isPretty;
	}

	public String get() {
		JSONObject rootObject = new JSONObject();
		rootObject.put("isBlindModeEnabled", dataState.resultType().get() == ResultType.BLIND);
		rootObject.put("hasDivine", dataState.getDivineContext().hasDivine());
		rootObject.put("blindResult", convertBlindResult(dataState.blindResult().get()));
		return rootObject.toString(isPretty ? 4 : 0);
	}

	@Override
	public boolean supportsSubscriptions() {
		return true;
	}

	private JSONObject convertBlindResult(BlindResult blindResult) {
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
		blindResultObject.put("evaluation", blindResult.evaluationEnum);
		return blindResultObject;
	}

}
