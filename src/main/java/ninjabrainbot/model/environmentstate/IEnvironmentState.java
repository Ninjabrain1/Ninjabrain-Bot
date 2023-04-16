package ninjabrainbot.model.environmentstate;

import ninjabrainbot.event.IObservable;
import ninjabrainbot.model.datastate.calculator.ICalculator;

/**
 * The state of external variables affecting the DataState. It is distinct from the DataState
 * because changes to the EnvironmentState are not undoable or resettable, for example settings
 * changes or the state of the Minecraft world.
 */
public interface IEnvironmentState {

	IObservable<ICalculator> calculator();

	IObservable<CalculatorSettings> calculatorSettings();

	IObservable<StandardDeviationSettings> standardDeviationSettings();

	IObservable<Boolean> allAdvancementsModeEnabled();

	IObservable<Boolean> hasEnteredEnd();

	void setHasEnteredEnd(boolean hasEnteredEnd);

}
