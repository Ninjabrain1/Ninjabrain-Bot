package ninjabrainbot.data.actions;

import ninjabrainbot.data.IDataState;
import ninjabrainbot.data.calculator.endereye.IEnderEyeThrow;

public class RemoveTriangulationThrowAction implements IAction {

	private final IDataState dataState;
	private final IEnderEyeThrow throwToRemove;

	public RemoveTriangulationThrowAction(IDataState dataState, IEnderEyeThrow throwToRemove) {
		this.dataState = dataState;
		this.throwToRemove = throwToRemove;
	}

	@Override
	public void execute() {
		dataState.getThrowList().remove(throwToRemove);
	}

}
