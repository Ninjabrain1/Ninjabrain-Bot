package ninjabrainbot.model.environmentstate;

import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.io.preferences.enums.McVersion;

public class CalculatorSettings {

	public final int numberOfReturnedPredictions;
	public final boolean useAdvancedStatistics;
	public final McVersion mcVersion;

	public CalculatorSettings() {
		numberOfReturnedPredictions = 5;
		useAdvancedStatistics = true;
		mcVersion = McVersion.PRE_119;
	}

	public CalculatorSettings(boolean useAdvancedStatistics, McVersion mcVersion) {
		numberOfReturnedPredictions = 5;
		this.useAdvancedStatistics = useAdvancedStatistics;
		this.mcVersion = mcVersion;
	}

	public CalculatorSettings(NinjabrainBotPreferences preferences) {
		numberOfReturnedPredictions = 5;
		useAdvancedStatistics = preferences.useAdvStatistics.get();
		mcVersion = preferences.mcVersion.get();
	}

}
