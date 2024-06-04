package ninjabrainbot.model.actions.endereye;

import ninjabrainbot.model.datastate.IDataState;
import ninjabrainbot.model.actions.IAction;
import ninjabrainbot.model.datastate.endereye.IEnderEyeThrow;

public class AddEnderEyeThrowAction implements IAction {

	private final IDataState dataState;
	private final IEnderEyeThrow throwToAdd;
	private final ChangeLastAngleAction changeAngleAction;

	public AddEnderEyeThrowAction(IDataState dataState, IEnderEyeThrow throwToAdd) {
		this(dataState, throwToAdd, null);
    }

	public AddEnderEyeThrowAction(IDataState dataState, IEnderEyeThrow throwToAdd, ChangeLastAngleAction changeAngleAction) {
		this.dataState = dataState;
		this.throwToAdd = throwToAdd;
        this.changeAngleAction = changeAngleAction;
    }

	@Override
	public void execute() {
		dataState.getThrowList().add(throwToAdd);

		if (changeAngleAction != null)
			changeAngleAction.execute();
	}

}
