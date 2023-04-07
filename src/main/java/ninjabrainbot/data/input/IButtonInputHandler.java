package ninjabrainbot.data.input;

import ninjabrainbot.data.calculator.endereye.IThrow;

public interface IButtonInputHandler {

	void onResetButtonPressed();

	void onUndoButtonPressed();

	void onRemoveFossilButtonPressed();

	void onRemoveThrowButtonPressed(IThrow throwToRemove);

}
