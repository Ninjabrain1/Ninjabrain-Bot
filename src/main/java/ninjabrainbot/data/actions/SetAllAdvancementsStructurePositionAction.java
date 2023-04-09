package ninjabrainbot.data.actions;

import ninjabrainbot.data.IDataState;
import ninjabrainbot.data.calculator.alladvancements.IAllAdvancementsDataState;
import ninjabrainbot.data.calculator.alladvancements.StructureType;
import ninjabrainbot.data.calculator.common.IDetailedPlayerPosition;
import ninjabrainbot.data.calculator.common.IPlayerPosition;
import ninjabrainbot.data.calculator.common.StructurePosition;
import ninjabrainbot.data.domainmodel.IDataComponent;
import ninjabrainbot.event.IObservable;

public class SetAllAdvancementsStructurePositionAction implements IAction {

	private final IAllAdvancementsDataState allAdvancementsDataState;
	private final IObservable<IPlayerPosition> playerPositionObservable;
	private final IDetailedPlayerPosition playerPosition;

	public SetAllAdvancementsStructurePositionAction(IDataState dataState, IDetailedPlayerPosition playerPosition) {
		this.allAdvancementsDataState = dataState.allAdvancementsDataState();
		this.playerPositionObservable = dataState.playerPosition();
		this.playerPosition = playerPosition;
	}

	@Override
	public void execute() {
		StructureType structureType = getAllAdvancementStructureTypeFromPlayerPosition(playerPosition);
		if (structureType == StructureType.Unknown)
			return;

		StructurePosition structurePosition =
				structureType == StructureType.Outpost
						? getOutpostPosition(playerPosition)
						: new StructurePosition((int) Math.floor(playerPosition.xInOverworld()), (int) Math.floor(playerPosition.zInOverworld()), playerPositionObservable);

		getDataComponentFromStructureType(structureType).set(structurePosition);
	}

	private StructureType getAllAdvancementStructureTypeFromPlayerPosition(IDetailedPlayerPosition t) {
		if (t.isInNether())
			return StructureType.Unknown;

		if (Math.abs(t.xInOverworld()) <= 300 && Math.abs(t.zInOverworld()) <= 300)
			return StructureType.Spawn;

		if (t.yInPlayerDimension() < 63)
			return StructureType.Monument;

		return StructureType.Outpost;
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

	private IDataComponent<StructurePosition> getDataComponentFromStructureType(StructureType structureType) {
		switch (structureType) {
			case Spawn:
				return allAdvancementsDataState.spawnPosition();
			case Outpost:
				return allAdvancementsDataState.outpostPosition();
			case Monument:
				return allAdvancementsDataState.monumentPosition();
			default:
				throw new IllegalArgumentException("Setting of structure type " + structureType + " is not supported.");
		}
	}

}
