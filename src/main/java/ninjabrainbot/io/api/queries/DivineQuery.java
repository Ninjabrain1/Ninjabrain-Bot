package ninjabrainbot.io.api.queries;

import ninjabrainbot.model.datastate.IDataState;
import ninjabrainbot.model.datastate.common.ResultType;
import ninjabrainbot.model.datastate.divine.DivineResult;
import org.json.JSONObject;

public class DivineQuery implements IQuery {

	private final boolean isPretty;

	public DivineQuery() {
		this(false);
	}

	public DivineQuery(boolean isPretty) {
		this.isPretty = isPretty;
	}

	public String get(IDataState dataState) {
		JSONObject rootObject = new JSONObject();
		rootObject.put("isDivineModeEnabled", dataState.resultType().get() == ResultType.DIVINE);
		rootObject.put("divineResult", convertDivineResult(dataState.divineResult().get()));
		return rootObject.toString(isPretty ? 4 : 0);
	}

	@Override
	public boolean supportsSubscriptions() {
		return true;
	}

	private JSONObject convertDivineResult(DivineResult divineResult) {
		JSONObject divineResultObject = new JSONObject();
		if (divineResult == null)
			return divineResultObject;

		divineResultObject.put("fossilXCoordinate", divineResult.fossil.x);
		divineResultObject.put("formattedHighrollCoords", divineResult.formatHighrollCoords());
		divineResultObject.put("formattedSafeCoords", divineResult.formatSafeCoords());
		return divineResultObject;
	}

}
