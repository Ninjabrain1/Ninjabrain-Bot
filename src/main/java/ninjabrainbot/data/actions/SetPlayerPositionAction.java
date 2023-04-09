package ninjabrainbot.data.actions;

import ninjabrainbot.data.IDataState;
import ninjabrainbot.data.calculator.common.IPlayerPosition;

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
