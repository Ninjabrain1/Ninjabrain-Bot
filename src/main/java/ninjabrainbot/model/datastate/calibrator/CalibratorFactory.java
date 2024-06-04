package ninjabrainbot.model.datastate.calibrator;

import ninjabrainbot.event.IObservable;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.model.datastate.common.IPlayerPositionInputSource;
import ninjabrainbot.model.environmentstate.CalculatorSettings;

public class CalibratorFactory implements ICalibratorFactory {

	private final IObservable<CalculatorSettings> calculatorSettings;
	private final IPlayerPositionInputSource playerPositionInputSource;
	private final NinjabrainBotPreferences preferences;

	public CalibratorFactory(IObservable<CalculatorSettings> calculatorSettings, IPlayerPositionInputSource playerPositionInputSource, NinjabrainBotPreferences preferences) {
		this.calculatorSettings = calculatorSettings;
		this.playerPositionInputSource = playerPositionInputSource;
		this.preferences = preferences;
	}

	@Override
	public Calibrator createCalibrator(boolean isBoatCalibrator, boolean isManualCalibrator) {
		return new Calibrator(calculatorSettings.get(), playerPositionInputSource, preferences, isBoatCalibrator, isManualCalibrator);
	}

}
