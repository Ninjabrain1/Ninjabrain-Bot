package ninjabrainbot.model.actions.alladvancements;

import ninjabrainbot.model.actions.IAction;
import ninjabrainbot.model.datastate.alladvancements.IAllAdvancementsDataState;

public class SetHasEnteredEndAction implements IAction {

	private final IAllAdvancementsDataState allAdvancementsDataState;
	private final boolean hasEnteredEnd;

	public SetHasEnteredEndAction(IAllAdvancementsDataState allAdvancementsDataState, boolean hasEnteredEnd) {
		this.allAdvancementsDataState = allAdvancementsDataState;
		this.hasEnteredEnd = hasEnteredEnd;
	}

	@Override
	public void execute() {
		allAdvancementsDataState.hasEnteredEnd().set(hasEnteredEnd);
	}

}
