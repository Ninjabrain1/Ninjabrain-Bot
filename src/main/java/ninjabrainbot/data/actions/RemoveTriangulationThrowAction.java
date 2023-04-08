package ninjabrainbot.data.actions;

import ninjabrainbot.data.IDataState;
import ninjabrainbot.data.calculator.endereye.IThrow;

public class RemoveTriangulationThrowAction implements IAction {

	private final IDataState dataState;
	private final IThrow throwToRemove;

	public RemoveTriangulationThrowAction(IDataState dataState, IThrow throwToRemove) {
		this.dataState = dataState;
		this.throwToRemove = throwToRemove;
	}

	@Override
	public void execute() {
		dataState.getThrowSet().remove(throwToRemove);
	}

}
