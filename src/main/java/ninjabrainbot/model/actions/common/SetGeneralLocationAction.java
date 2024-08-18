package ninjabrainbot.model.actions.common;

import ninjabrainbot.model.actions.IAction;
import ninjabrainbot.model.datastate.alladvancements.IAllAdvancementsDataState;
import ninjabrainbot.model.datastate.common.StructurePosition;

public class SetGeneralLocationAction implements IAction {

    private final IAllAdvancementsDataState allAdvancementsDataState;
    private final StructurePosition structurePosition;

	public SetGeneralLocationAction(IAllAdvancementsDataState allAdvancementsDataState, StructurePosition structurePosition) {
        this.allAdvancementsDataState = allAdvancementsDataState;
        this.structurePosition = structurePosition;
	}

	@Override
	public void execute() {
        allAdvancementsDataState.generalLocationPosition().set(structurePosition);
	}

}
