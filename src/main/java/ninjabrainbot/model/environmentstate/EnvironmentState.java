package ninjabrainbot.model.environmentstate;

import ninjabrainbot.event.DisposeHandler;
import ninjabrainbot.event.IDisposable;
import ninjabrainbot.event.IObservable;
import ninjabrainbot.event.Observable;
import ninjabrainbot.event.ObservableField;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.model.datastate.calculator.Calculator;
import ninjabrainbot.model.datastate.calculator.ICalculator;
import ninjabrainbot.model.domainmodel.IDomainModel;

/**
 * The state of external variables affecting the DataState. It is distinct from the DataState
 * because changes to the EnvironmentState are not undoable or resettable, for example settings
 * changes or the state of the Minecraft world.
 */
public class EnvironmentState implements IEnvironmentState, IDisposable {

	private final IDomainModel domainModel;
	private final NinjabrainBotPreferences preferences;

	private final IObservable<ICalculator> calculator;
	private final IObservable<CalculatorSettings> calculatorSettings;
	private final IObservable<StandardDeviationSettings> standardDeviationSettings;
	private final IObservable<Boolean> allAdvancementsModeEnabled;
	private final ObservableField<Boolean> hasEnteredEnd;

	private final DisposeHandler disposeHandler = new DisposeHandler();

	public EnvironmentState(IDomainModel domainModel, NinjabrainBotPreferences preferences) {
		this.domainModel = domainModel;
		this.preferences = preferences;
		calculatorSettings = disposeHandler.add(Observable
				.inferFrom(this::createCalculatorSettings)
				.dependsOn(preferences.useAdvStatistics, preferences.mcVersion)
				.whenPushingEventsDo(domainModel::applyWriteLock));
		standardDeviationSettings = disposeHandler.add(Observable
				.inferFrom(this::createStandardDeviationSettings)
				.dependsOn(preferences.sigma, preferences.sigmaAlt, preferences.sigmaManual, preferences.sigmaBoat)
				.whenPushingEventsDo(domainModel::applyWriteLock));
		calculator = disposeHandler.add(Observable
				.inferFrom(this::createCalculator)
				.dependsOn(calculatorSettings, standardDeviationSettings)
				.whenPushingEventsDo(domainModel::applyWriteLock));
		allAdvancementsModeEnabled = disposeHandler.add(Observable
				.inferFrom(preferences.allAdvancements::get)
				.dependsOn(preferences.allAdvancements)
				.whenPushingEventsDo(domainModel::applyWriteLock));
		hasEnteredEnd = new ObservableField<>(false);
	}

	@Override
	public IObservable<ICalculator> calculator() {
		return calculator;
	}

	@Override
	public IObservable<CalculatorSettings> calculatorSettings() {
		return calculatorSettings;
	}

	@Override
	public IObservable<StandardDeviationSettings> standardDeviationSettings() {
		return standardDeviationSettings;
	}

	@Override
	public IObservable<Boolean> allAdvancementsModeEnabled() {
		return allAdvancementsModeEnabled;
	}

	@Override
	public IObservable<Boolean> hasEnteredEnd() {
		return hasEnteredEnd;
	}

	@Override
	public void setHasEnteredEnd(boolean hasEnteredEnd) {
		domainModel.acquireWriteLock();
		try {
			this.hasEnteredEnd.set(hasEnteredEnd);
		} finally {
			domainModel.releaseWriteLock();
		}
	}

	private ICalculator createCalculator() {
		return new Calculator(calculatorSettings.get(), standardDeviationSettings.get());
	}

	private CalculatorSettings createCalculatorSettings(){
		return new CalculatorSettings(preferences.useAdvStatistics.get(), preferences.mcVersion.get());
	}

	private StandardDeviationSettings createStandardDeviationSettings() {
		return new StandardDeviationSettings(preferences.sigma.get(), preferences.sigmaAlt.get(), preferences.sigmaManual.get(), preferences.sigmaBoat.get());
	}

	@Override
	public void dispose() {
		disposeHandler.dispose();
	}
}
