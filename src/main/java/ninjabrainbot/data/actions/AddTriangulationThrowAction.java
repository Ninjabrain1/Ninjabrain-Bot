package ninjabrainbot.data.actions;

import ninjabrainbot.data.IDataState;
import ninjabrainbot.data.calculator.endereye.IThrow;

public class AddTriangulationThrowAction implements IAction {

	private final IDataState dataState;
	private final IThrow throwToAdd;

	public AddTriangulationThrowAction(IDataState dataState, IThrow throwToAdd) {
		this.dataState = dataState;
		this.throwToAdd = throwToAdd;
	}

	@Override
	public void execute() {
		dataState.getThrowSet().add(throwToAdd);
	}

}
