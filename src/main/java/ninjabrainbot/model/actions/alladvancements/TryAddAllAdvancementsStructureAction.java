package ninjabrainbot.model.actions.alladvancements;

import ninjabrainbot.event.IObservable;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.model.actions.IAction;
import ninjabrainbot.model.datastate.IDataState;
import ninjabrainbot.model.datastate.alladvancements.IAllAdvancementsDataState;
import ninjabrainbot.model.datastate.alladvancements.StructureType;
import ninjabrainbot.model.datastate.common.IDetailedPlayerPosition;
import ninjabrainbot.model.datastate.common.IPlayerPosition;
import ninjabrainbot.model.datastate.common.StructurePosition;
import ninjabrainbot.model.domainmodel.IDataComponent;

public class TryAddAllAdvancementsStructureAction implements IAction {

	private final NinjabrainBotPreferences preferences;
	private final IAllAdvancementsDataState allAdvancementsDataState;
	private final IObservable<IPlayerPosition> playerPositionObservable;
	private final IDetailedPlayerPosition playerPosition;

	public TryAddAllAdvancementsStructureAction(IDataState dataState, IDetailedPlayerPosition playerPosition, NinjabrainBotPreferences preferences) {
		this.allAdvancementsDataState = dataState.allAdvancementsDataState();
		this.playerPositionObservable = dataState.playerPosition();
		this.playerPosition = playerPosition;
		this.preferences = preferences;
	}

	@Override
	public void execute() {
		StructureType structureType = getAllAdvancementStructureTypeFromPlayerPosition(playerPosition);
		if (structureType == StructureType.Unknown)
			return;

		StructurePosition structurePosition = getStructurePosition(structureType);

		IDataComponent<StructurePosition> dataComponent = getDataComponentFromStructureType(structureType);
		// Cities can be queried multiple times, so overwrite the position.
		if (structureType != StructureType.CityQuery && dataComponent.get() != null)
			return;

		dataComponent.set(structurePosition);
	}

	private StructureType getAllAdvancementStructureTypeFromPlayerPosition(IDetailedPlayerPosition t) {
		if (t.isInNether())
			return StructureType.Unknown;

		if (t.isInEnd())
			return preferences.oneDotTwentyPlusAA.get() ? StructureType.ShulkerTransport : StructureType.Unknown;

		if (Math.abs(t.xInOverworld()) <= 300 && Math.abs(t.zInOverworld()) <= 300 && Math.abs(Math.round(t.yInPlayerDimension()) - t.yInPlayerDimension()) < 0.001)
			return StructureType.Spawn;

		if (t.yInPlayerDimension() < 63) {
			if (preferences.oneDotTwentyPlusAA.get()) {
				if (t.yInPlayerDimension() > 30 && t.isInOverworld()) {
					return StructureType.Monument;
				}
			} else {
				return StructureType.Monument;
			}
		}

		if (preferences.oneDotTwentyPlusAA.get()) {
			if (t.yInPlayerDimension() <= 30 && t.isInOverworld())
				return StructureType.DeepDark;
			
			if (t.yInPlayerDimension() > 160 && t.isInOverworld())
				return StructureType.CityQuery;
		}

		return StructureType.Outpost;
	}

	private StructurePosition getStructurePosition(StructureType structureType) {
		switch (structureType) {
			case Outpost:
				return getOutpostPosition(playerPosition);
			case CityQuery:
				return getCityRegionCentre(playerPosition);
			default:
				return new StructurePosition((int) Math.floor(playerPosition.xInOverworld()), (int) Math.floor(playerPosition.zInOverworld()), playerPositionObservable);
		}
	}

	private StructurePosition getOutpostPosition(IDetailedPlayerPosition t) {
		int averageOutpostY = 80;
		double deltaY = averageOutpostY - t.yInPlayerDimension();
		double horizontalDistance = deltaY / Math.tan(-t.verticalAngle() * Math.PI / 180.0);
		double deltaX = horizontalDistance * Math.sin(-t.horizontalAngle() * Math.PI / 180.0);
		double deltaZ = horizontalDistance * Math.cos(t.horizontalAngle() * Math.PI / 180.0);
		deltaX = Math.max(Math.min(deltaX, 350), -350);
		deltaZ = Math.max(Math.min(deltaZ, 350), -350);
		return new StructurePosition((int) (t.xInOverworld() + deltaX), (int) (t.zInOverworld() + deltaZ), playerPositionObservable);
	}

	// City region centres are at chunk positions 24m + 8, where m is an integer.
	// Find the closest city region centre to the player's position.
	private StructurePosition getCityRegionCentre(IDetailedPlayerPosition t) {
		int chunkX = (int) Math.floor(t.xInOverworld() / 16);
		int chunkZ = (int) Math.floor(t.zInOverworld() / 16);
		int cityRegionCentreX = 24 * (int) Math.round((chunkX - 8) / 24D) + 8;
		int cityRegionCentreZ = 24 * (int) Math.round((chunkZ - 8) / 24D) + 8;
		return new StructurePosition(cityRegionCentreX * 16 + 8, cityRegionCentreZ * 16 + 8, playerPositionObservable);
	}

	private IDataComponent<StructurePosition> getDataComponentFromStructureType(StructureType structureType) {
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
