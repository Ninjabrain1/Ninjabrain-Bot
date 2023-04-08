package ninjabrainbot.data.input;

import ninjabrainbot.data.IDataState;
import ninjabrainbot.data.actions.IActionExecutor;
import ninjabrainbot.data.actions.RemoveTriangulationThrowAction;
import ninjabrainbot.data.actions.ResetAction;
import ninjabrainbot.data.actions.SetFossilAction;
import ninjabrainbot.data.actions.UndoAction;
import ninjabrainbot.data.calculator.endereye.IThrow;
import ninjabrainbot.data.temp.IDomainModel;

public class ButtonInputHandler implements IButtonInputHandler {

	private final IDomainModel domainModel;
	private final IDataState dataState;
	private final IActionExecutor actionExecutor;

	public ButtonInputHandler(IDomainModel domainModel, IDataState dataState, IActionExecutor actionExecutor) {
		this.domainModel = domainModel;
		this.dataState = dataState;
		this.actionExecutor = actionExecutor;
	}

	@Override
	public void onResetButtonPressed() {
		actionExecutor.executeImmediately(new ResetAction(domainModel));
	}

	@Override
	public void onUndoButtonPressed() {
		actionExecutor.executeImmediately(new UndoAction(domainModel));
	}

	@Override
	public void onRemoveFossilButtonPressed() {
		actionExecutor.executeImmediately(new SetFossilAction(dataState.getDivineContext(), null));
	}

	@Override
	public void onRemoveThrowButtonPressed(IThrow throwToRemove) {
		actionExecutor.executeImmediately(new RemoveTriangulationThrowAction(dataState, throwToRemove));
	}

}
