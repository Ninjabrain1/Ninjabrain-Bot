package ninjabrainbot.io.api.queries;

import ninjabrainbot.model.datastate.IDataState;
import ninjabrainbot.model.datastate.alladvancements.AllAdvancementsStructureType;
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
		for (AllAdvancementsStructureType allAdvancementsStructureType : AllAdvancementsStructureType.values()){
			String key = mapAllAdvancementsStructureTypeName(allAdvancementsStructureType);
			rootObject.put(key, convertPosition(dataState.allAdvancementsDataState().getStructureInformation(allAdvancementsStructureType).get()));
		}
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

	private String mapAllAdvancementsStructureTypeName(AllAdvancementsStructureType allAdvancementsStructureType){
		switch (allAdvancementsStructureType){
			case Spawn:
				return "spawn";
			case Outpost:
				return "outpost";
			case Monument:
				return "monument";
			case Stronghold:
				return "stronghold";
			case DeepDark:
				return "deepDark";
			case CityQuery:
				return "cityQuery";
			case ShulkerTransport:
				return "shulkerTransport";
			case GeneralLocation:
				return "generalLocation";
		}
		throw new IllegalArgumentException("Unknown all advancements structure type: " + allAdvancementsStructureType);
	}

}
