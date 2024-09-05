package ninjabrainbot.model.actions.alladvancements;

import ninjabrainbot.model.actions.IAction;
import ninjabrainbot.model.datastate.alladvancements.IAllAdvancementsDataState;
import ninjabrainbot.model.datastate.common.StructureInformation;

public class RemoveStructureAction implements IAction {

	private final IAllAdvancementsDataState allAdvancementsDataState;
	private final StructureInformation structureInformation;

	public RemoveStructureAction(IAllAdvancementsDataState allAdvancementsDataState, StructureInformation structureInformation) {
		this.allAdvancementsDataState = allAdvancementsDataState;
		this.structureInformation = structureInformation;
	}

	@Override
	public void execute() {
		if (allAdvancementsDataState.spawnInformation().get() == structureInformation) {
			allAdvancementsDataState.spawnPosition().reset();
			return;
		}
		if (allAdvancementsDataState.outpostInformation().get() == structureInformation) {
			allAdvancementsDataState.outpostPosition().reset();
			return;
		}
		if (allAdvancementsDataState.monumentInformation().get() == structureInformation) {
			allAdvancementsDataState.monumentPosition().reset();
			return;
		}
		if (allAdvancementsDataState.shulkerTransportInformation().get() == structureInformation) {
			allAdvancementsDataState.shulkerTransportPosition().reset();
			return;
		}
		if (allAdvancementsDataState.deepDarkInformation().get() == structureInformation) {
			allAdvancementsDataState.deepDarkPosition().reset();
			return;
		}
		if (allAdvancementsDataState.cityQueryInformation().get() == structureInformation) {
			allAdvancementsDataState.cityQueryPosition().reset();
			return;
		}
		if (allAdvancementsDataState.generalLocationInformation().get() == structureInformation) {
			allAdvancementsDataState.generalLocationPosition().reset();
			return;
		}
		throw new IllegalArgumentException(String.format("Cannot remove structure position %s because it not present in the data state.", structureInformation));
	}

}
