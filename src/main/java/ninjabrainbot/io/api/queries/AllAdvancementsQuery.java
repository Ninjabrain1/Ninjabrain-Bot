package ninjabrainbot.io.api.queries;

import ninjabrainbot.model.datastate.IDataState;
import ninjabrainbot.model.datastate.common.StructureInformation;
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
		rootObject.put("strongholdPosition", convertPosition(dataState.allAdvancementsDataState().strongholdInformation().get()));
		rootObject.put("spawnPosition", convertPosition(dataState.allAdvancementsDataState().spawnInformation().get()));
		rootObject.put("outpostPosition", convertPosition(dataState.allAdvancementsDataState().outpostInformation().get()));
		rootObject.put("monumentPosition", convertPosition(dataState.allAdvancementsDataState().monumentInformation().get()));
		return rootObject.toString(isPretty ? 4 : 0);
	}

	@Override
	public boolean supportsSubscriptions() {
		return true;
	}

	private JSONObject convertPosition(StructureInformation structureInformation) {
		JSONObject structurePositionObject = new JSONObject();
		if (structureInformation == null)
			return structurePositionObject;

		structurePositionObject.put("xInOverworld", structureInformation.xInOverworld());
		structurePositionObject.put("zInOverworld", structureInformation.zInOverworld());
		structurePositionObject.put("overworldDistance", structureInformation.getOverworldDistance());
		structurePositionObject.put("travelAngle", structureInformation.getTravelAngle());
		return structurePositionObject;
	}

}
