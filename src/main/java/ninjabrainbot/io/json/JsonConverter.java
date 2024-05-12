package ninjabrainbot.io.json;

import ninjabrainbot.model.datastate.IDataState;
import ninjabrainbot.model.datastate.calculator.ICalculatorResult;
import ninjabrainbot.model.datastate.common.IPlayerPosition;
import ninjabrainbot.model.datastate.stronghold.ChunkPrediction;
import org.json.JSONArray;
import org.json.JSONObject;

public class JsonConverter {

	private final boolean isPretty;

	public JsonConverter() {
		this(false);
	}

	public JsonConverter(boolean isPretty) {
		this.isPretty = isPretty;
	}

	public String convert(IDataState dataState) {
		JSONObject rootObject = new JSONObject();
		rootObject.put("resultType", dataState.resultType().get());
		rootObject.put("predictions", convertCalculatorResult(dataState.calculatorResult().get()));
		rootObject.put("playerPosition", convertPlayerPosition(dataState.playerPosition().get()));
		return rootObject.toString(isPretty ? 4 : 0);
	}

	private JSONArray convertCalculatorResult(ICalculatorResult calculatorResult) {
		JSONArray predictions = new JSONArray();
		for (ChunkPrediction chunkPrediction : calculatorResult.getTopPredictions()){
			JSONObject prediction = new JSONObject();
			prediction.put("certainty", chunkPrediction.chunk.weight);
			prediction.put("chunkX", chunkPrediction.chunk.x);
			prediction.put("chunkZ", chunkPrediction.chunk.z);
			predictions.put(prediction);
		}
		return predictions;
	}

	private JSONObject convertPlayerPosition(IPlayerPosition playerPosition) {
		JSONObject playerPositionObject = new JSONObject();
		playerPositionObject.put("isInOverworld", playerPosition.isInOverworld());
		playerPositionObject.put("isInNether", playerPosition.isInNether());
		playerPositionObject.put("xInOverworld", playerPosition.xInOverworld());
		playerPositionObject.put("zInOverworld", playerPosition.zInOverworld());
		playerPositionObject.put("horizontalAngle", playerPosition.horizontalAngle());
		return playerPositionObject;
	}

}
