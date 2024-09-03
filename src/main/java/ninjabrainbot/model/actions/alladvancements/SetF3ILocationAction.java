package ninjabrainbot.model.actions.alladvancements;

import ninjabrainbot.model.actions.IAction;
import ninjabrainbot.model.datastate.alladvancements.IAllAdvancementsDataState;
import ninjabrainbot.model.datastate.common.StructurePosition;
import ninjabrainbot.model.datastate.endereye.F3IData;

public class SetF3ILocationAction implements IAction {

    private final IAllAdvancementsDataState allAdvancementsDataState;
    private final StructurePosition structurePosition;

    public SetF3ILocationAction(IAllAdvancementsDataState allAdvancementsDataState, F3IData f3iData) {
        this.allAdvancementsDataState = allAdvancementsDataState;
        this.structurePosition = new StructurePosition(f3iData.x, f3iData.z);
    }

    @Override
    public void execute() {
        allAdvancementsDataState.generalLocationPosition().set(structurePosition);
    }
    
}
