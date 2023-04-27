package ninjabrainbot.model.actions.common;

import ninjabrainbot.model.actions.IAction;
import ninjabrainbot.model.datastate.IDataState;

public class SetLockedAction implements IAction {

	private final IDataState dataState;
	private final boolean locked;

	public SetLockedAction(IDataState dataState, boolean locked) {
		this.dataState = dataState;
		this.locked = locked;
	}

	@Override
	public void execute() {
		dataState.locked().set(locked);
	}

}
