package ninjabrainbot.model.input;

import ninjabrainbot.model.IDataState;
import ninjabrainbot.model.actions.IActionExecutor;
import ninjabrainbot.model.actions.endereye.RemoveEnderEyeThrowAction;
import ninjabrainbot.model.actions.common.ResetAction;
import ninjabrainbot.model.actions.common.SetFossilAction;
import ninjabrainbot.model.datastate.endereye.IEnderEyeThrow;
import ninjabrainbot.model.domainmodel.IDomainModel;

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
		domainModel.undoUnderWriteLock();
	}

	@Override
	public void onRedoButtonPressed() {
		domainModel.redoUnderWriteLock();
	}

	@Override
	public void onRemoveFossilButtonPressed() {
		actionExecutor.executeImmediately(new SetFossilAction(dataState.getDivineContext(), null));
	}

	@Override
	public void onRemoveThrowButtonPressed(IEnderEyeThrow throwToRemove) {
		actionExecutor.executeImmediately(new RemoveEnderEyeThrowAction(dataState, throwToRemove));
	}

}
