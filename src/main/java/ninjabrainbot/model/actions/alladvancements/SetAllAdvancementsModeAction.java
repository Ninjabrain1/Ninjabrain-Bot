package ninjabrainbot.model.actions.alladvancements;

import ninjabrainbot.model.actions.IAction;
import ninjabrainbot.model.datastate.alladvancements.IAllAdvancementsDataState;

public class SetAllAdvancementsModeAction implements IAction {

	private final IAllAdvancementsDataState allAdvancementsDataState;
	private final boolean enabled;

	public SetAllAdvancementsModeAction(IAllAdvancementsDataState allAdvancementsDataState, boolean allAdvancementsModeEnabled) {
		this.allAdvancementsDataState = allAdvancementsDataState;
		this.enabled = allAdvancementsModeEnabled;
	}

	@Override
	public void execute() {
		allAdvancementsDataState.allAdvancementsModeEnabled().set(enabled);
	}

}