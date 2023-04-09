package ninjabrainbot.data.actions;

import ninjabrainbot.data.IDataState;
import ninjabrainbot.data.calculator.endereye.IEnderEyeThrow;

public class AddEnderEyeThrowAction implements IAction {

	private final IDataState dataState;
	private final IEnderEyeThrow throwToAdd;

	public AddEnderEyeThrowAction(IDataState dataState, IEnderEyeThrow throwToAdd) {
		this.dataState = dataState;
		this.throwToAdd = throwToAdd;
	}

	@Override
	public void execute() {
		dataState.getThrowSet().add(throwToAdd);
	}

}
