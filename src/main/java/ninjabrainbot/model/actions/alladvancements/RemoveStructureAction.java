package ninjabrainbot.model.actions.alladvancements;

import ninjabrainbot.model.actions.IAction;
import ninjabrainbot.model.datastate.alladvancements.IAllAdvancementsDataState;
import ninjabrainbot.model.datastate.common.StructurePosition;

public class RemoveStructureAction implements IAction {

	private final IAllAdvancementsDataState allAdvancementsDataState;
	private final StructurePosition structurePosition;

	public RemoveStructureAction(IAllAdvancementsDataState allAdvancementsDataState, StructurePosition structurePosition) {
		this.allAdvancementsDataState = allAdvancementsDataState;
		this.structurePosition = structurePosition;
	}

	@Override
	public void execute() {
		if (allAdvancementsDataState.spawnPosition().get() == structurePosition) {
			allAdvancementsDataState.spawnPosition().reset();
			return;
		}
		if (allAdvancementsDataState.outpostPosition().get() == structurePosition) {
			allAdvancementsDataState.outpostPosition().reset();
			return;
		}
		if (allAdvancementsDataState.monumentPosition().get() == structurePosition) {
			allAdvancementsDataState.monumentPosition().reset();
			return;
		}
		throw new IllegalArgumentException(String.format("Cannot remove structure position %s because it not present in the data state.", structurePosition));
	}

}
