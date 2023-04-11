package ninjabrainbot.model.actions.common;

import ninjabrainbot.model.IDataState;
import ninjabrainbot.model.actions.IAction;
import ninjabrainbot.model.datastate.common.IPlayerPosition;

public class SetPlayerPositionAction implements IAction {

	private final IDataState dataState;
	private final IPlayerPosition newPlayerPosition;

	public SetPlayerPositionAction(IDataState dataState, IPlayerPosition newPlayerPosition) {
		this.dataState = dataState;
		this.newPlayerPosition = newPlayerPosition;
	}

	@Override
	public void execute() {
		dataState.playerPosition().set(newPlayerPosition);
	}
}
