package ninjabrainbot.model.actions.endereye;

import ninjabrainbot.model.IDataState;
import ninjabrainbot.model.actions.IAction;
import ninjabrainbot.model.datastate.endereye.IEnderEyeThrow;

public class RemoveEnderEyeThrowAction implements IAction {

	private final IDataState dataState;
	private final IEnderEyeThrow throwToRemove;

	public RemoveEnderEyeThrowAction(IDataState dataState, IEnderEyeThrow throwToRemove) {
		this.dataState = dataState;
		this.throwToRemove = throwToRemove;
	}

	@Override
	public void execute() {
		dataState.getThrowList().remove(throwToRemove);
	}

}
