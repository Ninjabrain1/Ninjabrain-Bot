package ninjabrainbot.data.calculator;

import ninjabrainbot.io.preferences.MultipleChoicePreferenceDataTypes.McVersion;

public class CalculatorSettings {

	public boolean approximatedBlindCalculations = true;
	public int numberOfReturnedPredictions = 5;

	public boolean useAdvStatistics = true;
	public McVersion version = McVersion.PRE_119;

}
