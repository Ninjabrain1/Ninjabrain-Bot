package ninjabrainbot.data.actions;

import ninjabrainbot.data.IDataState;
import ninjabrainbot.data.calculator.endereye.IThrow;

public class SetPlayerPositionAction implements IAction {

	private final IDataState dataState;
	private final IThrow newPlayerPosition;

	public SetPlayerPositionAction(IDataState dataState, IThrow newPlayerPosition) {
		this.dataState = dataState;
		this.newPlayerPosition = newPlayerPosition;
	}

	@Override
	public void execute() {
		dataState.playerPosition().set(newPlayerPosition);
	}
}
