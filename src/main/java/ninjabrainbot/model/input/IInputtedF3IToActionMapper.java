package ninjabrainbot.model.input;

import ninjabrainbot.model.actions.IAction;
import ninjabrainbot.model.datastate.common.IDetailedPlayerPosition;
import ninjabrainbot.model.datastate.common.ILimitedPlayerPosition;
import ninjabrainbot.model.datastate.endereye.F3IData;

public interface IInputtedF3IToActionMapper {

	IAction[] getActionsForInputtedF3I(F3IData f3IData);

}
