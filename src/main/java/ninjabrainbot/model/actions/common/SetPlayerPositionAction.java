package ninjabrainbot.model.actions.common;

import ninjabrainbot.model.actions.IAction;
import ninjabrainbot.model.datastate.IDataState;
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
		IPlayerPosition currentPlayerPosition = dataState.playerPosition().get();
		if (currentPlayerPosition != null &&
			newPlayerPosition != null &&
			currentPlayerPosition.xInPlayerDimension() == newPlayerPosition.xInPlayerDimension() &&
			currentPlayerPosition.zInPlayerDimension() == newPlayerPosition.zInPlayerDimension() &&
			currentPlayerPosition.isInNether() == newPlayerPosition.isInNether() &&
			currentPlayerPosition.isInOverworld() == newPlayerPosition.isInOverworld() &&
			currentPlayerPosition.isInEnd() == newPlayerPosition.isInEnd() &&
			currentPlayerPosition.horizontalAngle() == newPlayerPosition.horizontalAngle())
			return;

		dataState.playerPosition().set(newPlayerPosition);
	}
}
