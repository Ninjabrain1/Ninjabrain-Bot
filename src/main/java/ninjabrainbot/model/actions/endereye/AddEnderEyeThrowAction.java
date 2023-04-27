package ninjabrainbot.model.actions.endereye;

import ninjabrainbot.model.datastate.IDataState;
import ninjabrainbot.model.actions.IAction;
import ninjabrainbot.model.datastate.endereye.IEnderEyeThrow;

public class AddEnderEyeThrowAction implements IAction {

	private final IDataState dataState;
	private final IEnderEyeThrow throwToAdd;

	public AddEnderEyeThrowAction(IDataState dataState, IEnderEyeThrow throwToAdd) {
		this.dataState = dataState;
		this.throwToAdd = throwToAdd;
	}

	@Override
	public void execute() {
		dataState.getThrowList().add(throwToAdd);
	}

}
