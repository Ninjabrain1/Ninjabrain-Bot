package ninjabrainbot.model.input;

import ninjabrainbot.model.datastate.common.StructureInformation;
import ninjabrainbot.model.datastate.endereye.IEnderEyeThrow;

public interface IButtonInputHandler {

	void onResetButtonPressed();

	void onUndoButtonPressed();

	void onRedoButtonPressed();

	void onRemoveFossilButtonPressed();

	void onRemoveThrowButtonPressed(IEnderEyeThrow throwToRemove);

	void onRemoveAllAdvancementsStructureButtonPressed(StructureInformation structureInformation);

}
