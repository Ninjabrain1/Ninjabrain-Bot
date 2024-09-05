package ninjabrainbot.io.api.queries;

import ninjabrainbot.model.datastate.IDataState;
import ninjabrainbot.model.datastate.common.StructurePosition;
import org.json.JSONObject;

public class AllAdvancementsQuery implements IQuery {

	private final boolean isPretty;

	public AllAdvancementsQuery() {
		this(false);
	}

	public AllAdvancementsQuery(boolean isPretty) {
		this.isPretty = isPretty;
	}

	public String get(IDataState dataState) {
		JSONObject rootObject = new JSONObject();
		rootObject.put("isAllAdvancementsModeEnabled", dataState.allAdvancementsDataState().allAdvancementsModeEnabled().get());
		rootObject.put("strongholdPosition", convertPosition(dataState.allAdvancementsDataState().strongholdPosition().get()));
		rootObject.put("spawnPosition", convertPosition(dataState.allAdvancementsDataState().spawnPosition().get()));
		rootObject.put("outpostPosition", convertPosition(dataState.allAdvancementsDataState().outpostPosition().get()));
		rootObject.put("monumentPosition", convertPosition(dataState.allAdvancementsDataState().monumentPosition().get()));
		return rootObject.toString(isPretty ? 4 : 0);
	}

	@Override
	public boolean supportsSubscriptions() {
		return true;
	}

	private JSONObject convertPosition(StructurePosition structurePosition) {
		JSONObject structurePositionObject = new JSONObject();
		if (structurePosition == null)
			return structurePositionObject;

		structurePositionObject.put("xInOverworld", structurePosition.xInOverworld());
		structurePositionObject.put("zInOverworld", structurePosition.zInOverworld());
		structurePositionObject.put("overworldDistance", structurePosition.getOverworldDistance());
		structurePositionObject.put("travelAngle", structurePosition.getTravelAngle());
		return structurePositionObject;
	}

}
