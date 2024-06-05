package ninjabrainbot.io.api.queries;

import ninjabrainbot.Main;
import ninjabrainbot.model.datastate.IDataState;
import ninjabrainbot.model.datastate.calculator.ICalculatorResult;
import ninjabrainbot.model.datastate.common.IPlayerPosition;
import ninjabrainbot.model.datastate.endereye.IEnderEyeThrow;
import ninjabrainbot.model.datastate.stronghold.ChunkPrediction;
import ninjabrainbot.model.domainmodel.IListComponent;
import org.json.JSONArray;
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
