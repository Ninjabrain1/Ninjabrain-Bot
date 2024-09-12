package ninjabrainbot.model.actions.alladvancements;

import ninjabrainbot.model.actions.IAction;
import ninjabrainbot.model.datastate.alladvancements.AllAdvancementsStructureType;
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
		for (AllAdvancementsStructureType allAdvancementsStructureType : AllAdvancementsStructureType.values()) {
			if (allAdvancementsDataState.getStructureInformation(allAdvancementsStructureType).get() == structureInformation) {
				allAdvancementsDataState.getAllAdvancementsPosition(allAdvancementsStructureType).reset();
				return;
			}
		}
		throw new IllegalArgumentException(String.format("Cannot remove structure position %s because it not present in the data state.", structureInformation));
	}

}
