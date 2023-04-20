package ninjabrainbot.model.environmentstate;

import ninjabrainbot.model.datastate.calculator.ICalculator;
import ninjabrainbot.model.domainmodel.IEnvironmentComponent;

/**
 * The state of external variables affecting the DataState. It is distinct from the DataState
 * because changes to the EnvironmentState are not undoable or resettable, for example settings
 * changes or the state of the Minecraft world.
 */
public interface IEnvironmentState {

	IEnvironmentComponent<ICalculator> calculator();

	IEnvironmentComponent<CalculatorSettings> calculatorSettings();

	IEnvironmentComponent<StandardDeviationSettings> standardDeviationSettings();

	IEnvironmentComponent<Boolean> allAdvancementsModeEnabled();

	IEnvironmentComponent<Boolean> hasEnteredEnd();

	void setHasEnteredEnd(boolean hasEnteredEnd);

}
