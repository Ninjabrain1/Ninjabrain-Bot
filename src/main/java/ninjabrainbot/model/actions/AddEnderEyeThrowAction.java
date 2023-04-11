package ninjabrainbot.model.actions;

import ninjabrainbot.model.IDataState;
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
