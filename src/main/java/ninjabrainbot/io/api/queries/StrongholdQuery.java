package ninjabrainbot.io.api.queries;

import ninjabrainbot.model.datastate.IDataState;
import ninjabrainbot.model.datastate.calculator.ICalculatorResult;
import ninjabrainbot.model.datastate.common.IPlayerPosition;
import ninjabrainbot.model.datastate.endereye.IEnderEyeThrow;
import ninjabrainbot.model.datastate.stronghold.ChunkPrediction;
import ninjabrainbot.model.domainmodel.IListComponent;
import org.json.JSONArray;
import org.json.JSONObject;

public class StrongholdQuery implements IQuery {

	private final boolean isPretty;

	public StrongholdQuery() {
		this(false);
	}

	public StrongholdQuery(boolean isPretty) {
		this.isPretty = isPretty;
	}

	public String get(IDataState dataState) {
		JSONObject rootObject = new JSONObject();
		rootObject.put("resultType", dataState.resultType().get());
		rootObject.put("predictions", convertCalculatorResult(dataState.calculatorResult().get(), dataState.playerPosition().get()));
		rootObject.put("eyeThrows", convertEyeThrows(dataState.getThrowList(), dataState.calculatorResult().get()));
		rootObject.put("playerPosition", convertPlayerPosition(dataState.playerPosition().get()));
		return rootObject.toString(isPretty ? 4 : 0);
	}

	private JSONArray convertCalculatorResult(ICalculatorResult calculatorResult, IPlayerPosition playerPosition) {
		JSONArray predictions = new JSONArray();
		if (calculatorResult == null || !calculatorResult.success())
			return predictions;

		for (ChunkPrediction chunkPrediction : calculatorResult.getTopPredictions()) {
			JSONObject prediction = new JSONObject();
			prediction.put("certainty", chunkPrediction.chunk.weight);
			prediction.put("chunkX", chunkPrediction.chunk.x);
			prediction.put("chunkZ", chunkPrediction.chunk.z);
			prediction.put("overworldDistance", chunkPrediction.distanceInOverworld(playerPosition));
			predictions.put(prediction);
		}
		return predictions;
	}

	private JSONArray convertEyeThrows(IListComponent<IEnderEyeThrow> throwList, ICalculatorResult calculatorResult) {
		JSONArray eyeThrows = new JSONArray();
		ChunkPrediction prediction = calculatorResult != null && calculatorResult.success() ? calculatorResult.getBestPrediction() : null;
		for (IEnderEyeThrow eyeThrow : throwList) {
			JSONObject eyeThrowJson = new JSONObject();
			eyeThrowJson.put("xInOverworld", eyeThrow.xInOverworld());
			eyeThrowJson.put("zInOverworld", eyeThrow.zInOverworld());
			eyeThrowJson.put("angle", eyeThrow.horizontalAngle());
			eyeThrowJson.put("angleWithoutCorrection", eyeThrow.horizontalAngleWithoutCorrection());
			eyeThrowJson.put("correction", eyeThrow.correction());
			eyeThrowJson.put("error", prediction != null ? prediction.getAngleError(eyeThrow) : 0);
			eyeThrows.put(eyeThrowJson);
		}
		return eyeThrows;
	}

	private JSONObject convertPlayerPosition(IPlayerPosition playerPosition) {
		JSONObject playerPositionObject = new JSONObject();
		if (playerPosition == null)
			return playerPositionObject;

		playerPositionObject.put("isInOverworld", playerPosition.isInOverworld());
		playerPositionObject.put("isInNether", playerPosition.isInNether());
		playerPositionObject.put("xInOverworld", playerPosition.xInOverworld());
		playerPositionObject.put("zInOverworld", playerPosition.zInOverworld());
		playerPositionObject.put("horizontalAngle", playerPosition.horizontalAngle());
		return playerPositionObject;
	}

}
