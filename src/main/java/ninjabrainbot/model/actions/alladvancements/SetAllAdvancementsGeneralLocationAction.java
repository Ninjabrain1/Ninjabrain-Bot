package ninjabrainbot.model.actions.alladvancements;

import ninjabrainbot.model.actions.IAction;
import ninjabrainbot.model.datastate.alladvancements.AllAdvancementsPosition;
import ninjabrainbot.model.datastate.alladvancements.IAllAdvancementsDataState;
import ninjabrainbot.model.datastate.endereye.F3IData;

public class SetAllAdvancementsGeneralLocationAction implements IAction {

	private final IAllAdvancementsDataState allAdvancementsDataState;
	private final AllAdvancementsPosition allAdvancementsPosition;

	public SetAllAdvancementsGeneralLocationAction(IAllAdvancementsDataState allAdvancementsDataState, F3IData f3iData) {
		this.allAdvancementsDataState = allAdvancementsDataState;
		this.allAdvancementsPosition = new AllAdvancementsPosition(f3iData.x, f3iData.z);
	}

	@Override
	public void execute() {
		allAdvancementsDataState.generalLocationPosition().set(allAdvancementsPosition);
	}
}
