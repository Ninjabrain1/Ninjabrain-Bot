package ninjabrainbot.data.actions;

import ninjabrainbot.data.calculator.alladvancements.IAllAdvancementsDataState;
import ninjabrainbot.data.calculator.alladvancements.StructureType;
import ninjabrainbot.data.calculator.common.StructurePosition;
import ninjabrainbot.data.temp.IDataComponent;

public class SetAllAdvancementsStructurePositionAction implements IAction {

	private final IDataComponent<StructurePosition> structurePositionDataComponent;
	private final StructurePosition structurePosition;

	public SetAllAdvancementsStructurePositionAction(IAllAdvancementsDataState allAdvancementsDataState, StructureType structureType, StructurePosition structurePosition) {
		switch (structureType) {
			case Spawn:
				structurePositionDataComponent = allAdvancementsDataState.spawnPosition();
				break;
			case Outpost:
				structurePositionDataComponent = allAdvancementsDataState.outpostPosition();
				break;
			case Monument:
				structurePositionDataComponent = allAdvancementsDataState.monumentPosition();
				break;
			default:
				throw new IllegalArgumentException("Setting of structure type " + structureType + " is not supported.");
		}
		this.structurePosition = structurePosition;
	}

	@Override
	public void execute() {
		structurePositionDataComponent.set(structurePosition);
	}
}
