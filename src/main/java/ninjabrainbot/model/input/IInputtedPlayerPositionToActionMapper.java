package ninjabrainbot.model.input;

import ninjabrainbot.model.actions.IAction;
import ninjabrainbot.model.datastate.common.IDetailedPlayerPosition;
import ninjabrainbot.model.datastate.common.ILimitedPlayerPosition;

public interface IInputtedPlayerPositionToActionMapper {

	IAction[] getActionsForInputtedPlayerPosition(IDetailedPlayerPosition detailedPlayerPosition);

	IAction[] getActionsForInputtedLimitedPlayerPosition(ILimitedPlayerPosition limitedPlayerPosition);
}
