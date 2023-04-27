package ninjabrainbot.model.actions.common;

import ninjabrainbot.model.datastate.IDataState;
import ninjabrainbot.model.datastate.common.ResultType;
import ninjabrainbot.model.actions.IAction;

public class ToggleLockedAction implements IAction {

	private final IDataState dataState;

	public ToggleLockedAction(IDataState dataState) {
		this.dataState = dataState;
	}

	@Override
	public void execute() {
		if (dataState.resultType().get() == ResultType.NONE && !dataState.locked().get())
			return;
		dataState.locked().set(!dataState.locked().get());
	}

}
