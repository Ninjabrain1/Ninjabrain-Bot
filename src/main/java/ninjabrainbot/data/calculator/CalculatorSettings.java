package ninjabrainbot.data.calculator;

import ninjabrainbot.io.preferences.MultipleChoicePreferenceDataTypes.McVersion;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;

public class CalculatorSettings {

	public int numberOfReturnedPredictions;
	public boolean useAdvancedStatistics;
	public McVersion mcVersion;

	public CalculatorSettings() {
		numberOfReturnedPredictions = 5;
		useAdvancedStatistics = true;
		mcVersion = McVersion.PRE_119;
	}

	public CalculatorSettings(NinjabrainBotPreferences preferences) {
		numberOfReturnedPredictions = 5;
		useAdvancedStatistics = preferences.useAdvStatistics.get();
		mcVersion = preferences.mcVersion.get();
	}

}
