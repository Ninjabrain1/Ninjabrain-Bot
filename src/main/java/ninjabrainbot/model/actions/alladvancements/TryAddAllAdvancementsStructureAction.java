package ninjabrainbot.model.actions.alladvancements;

import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.model.actions.IAction;
import ninjabrainbot.model.datastate.IDataState;
import ninjabrainbot.model.datastate.alladvancements.AllAdvancementsPosition;
import ninjabrainbot.model.datastate.alladvancements.IAllAdvancementsDataState;
import ninjabrainbot.model.datastate.alladvancements.IAllAdvancementsPosition;
import ninjabrainbot.model.datastate.alladvancements.AllAdvancementsStructureType;
import ninjabrainbot.model.datastate.common.IDetailedPlayerPosition;
import ninjabrainbot.model.domainmodel.IDataComponent;

public class TryAddAllAdvancementsStructureAction implements IAction {

	private final NinjabrainBotPreferences preferences;
	private final IAllAdvancementsDataState allAdvancementsDataState;
	private final IDetailedPlayerPosition playerPosition;

	public TryAddAllAdvancementsStructureAction(IDataState dataState, IDetailedPlayerPosition playerPosition, NinjabrainBotPreferences preferences) {
		this.allAdvancementsDataState = dataState.allAdvancementsDataState();
		this.playerPosition = playerPosition;
		this.preferences = preferences;
	}

	@Override
	public void execute() {
		AllAdvancementsStructureType structureType = getAllAdvancementStructureTypeFromPlayerPosition(playerPosition);
		if (structureType == AllAdvancementsStructureType.Unknown)
			return;

		IAllAdvancementsPosition structurePosition = getStructurePosition(structureType);

		IDataComponent<IAllAdvancementsPosition> dataComponent = getDataComponentFromStructureType(structureType);
		// Cities can be queried multiple times, so overwrite the position.
		if (structureType != AllAdvancementsStructureType.CityQuery && dataComponent.get() != null)
			return;

		dataComponent.set(structurePosition);
	}

	private AllAdvancementsStructureType getAllAdvancementStructureTypeFromPlayerPosition(IDetailedPlayerPosition t) {
		if (t.isInNether())
			return AllAdvancementsStructureType.Unknown;

		if (t.isInEnd())
			return preferences.oneDotTwentyPlusAA.get() ? AllAdvancementsStructureType.ShulkerTransport : AllAdvancementsStructureType.Unknown;

		if (Math.abs(t.xInOverworld()) <= 300 && Math.abs(t.zInOverworld()) <= 300 && Math.abs(Math.round(t.yInPlayerDimension()) - t.yInPlayerDimension()) < 0.001)
			return AllAdvancementsStructureType.Spawn;

		if (t.yInPlayerDimension() < 63) {
			if (preferences.oneDotTwentyPlusAA.get()) {
				if (t.yInPlayerDimension() > 30 && t.isInOverworld()) {
					return AllAdvancementsStructureType.Monument;
				}
			} else {
				return AllAdvancementsStructureType.Monument;
			}
		}

		if (preferences.oneDotTwentyPlusAA.get()) {
			if (t.yInPlayerDimension() <= 30 && t.isInOverworld())
				return AllAdvancementsStructureType.DeepDark;

			if (t.yInPlayerDimension() > 160 && t.isInOverworld())
				return AllAdvancementsStructureType.CityQuery;
		}

		return AllAdvancementsStructureType.Outpost;
	}

	private IAllAdvancementsPosition getStructurePosition(AllAdvancementsStructureType structureType) {
		switch (structureType) {
			case Outpost:
				return getOutpostPosition(playerPosition);
			case CityQuery:
				return getCityRegionCentre(playerPosition);
			default:
				return new AllAdvancementsPosition((int) Math.floor(playerPosition.xInOverworld()), (int) Math.floor(playerPosition.zInOverworld()));
		}
	}

	private IAllAdvancementsPosition getOutpostPosition(IDetailedPlayerPosition t) {
		int averageOutpostY = 80;
		double deltaY = averageOutpostY - t.yInPlayerDimension();
		double horizontalDistance = deltaY / Math.tan(-t.verticalAngle() * Math.PI / 180.0);
		double deltaX = horizontalDistance * Math.sin(-t.horizontalAngle() * Math.PI / 180.0);
		double deltaZ = horizontalDistance * Math.cos(t.horizontalAngle() * Math.PI / 180.0);
		deltaX = Math.max(Math.min(deltaX, 350), -350);
		deltaZ = Math.max(Math.min(deltaZ, 350), -350);
		return new AllAdvancementsPosition((int) (t.xInOverworld() + deltaX), (int) (t.zInOverworld() + deltaZ));
	}

	// City region centres are at chunk positions 24m + 8, where m is an integer.
	// Find the closest city region centre to the player's position.
	private IAllAdvancementsPosition getCityRegionCentre(IDetailedPlayerPosition t) {
		int chunkX = (int) Math.floor(t.xInOverworld() / 16);
		int chunkZ = (int) Math.floor(t.zInOverworld() / 16);
		int cityRegionCentreX = 24 * (int) Math.round((chunkX - 8) / 24D) + 8;
		int cityRegionCentreZ = 24 * (int) Math.round((chunkZ - 8) / 24D) + 8;
		return new AllAdvancementsPosition(cityRegionCentreX * 16 + 8, cityRegionCentreZ * 16 + 8);
	}

	private IDataComponent<IAllAdvancementsPosition> getDataComponentFromStructureType(AllAdvancementsStructureType structureType) {
		switch (structureType) {
			case Spawn:
				return allAdvancementsDataState.spawnPosition();
			case Outpost:
				return allAdvancementsDataState.outpostPosition();
			case Monument:
				return allAdvancementsDataState.monumentPosition();
			case DeepDark:
				return allAdvancementsDataState.deepDarkPosition();
			case CityQuery:
				return allAdvancementsDataState.cityQueryPosition();
			case ShulkerTransport:
				return allAdvancementsDataState.shulkerTransportPosition();
			case GeneralLocation:
				return allAdvancementsDataState.generalLocationPosition();
			default:
				throw new IllegalArgumentException("Setting of structure type " + structureType + " is not supported.");
		}
	}

}
