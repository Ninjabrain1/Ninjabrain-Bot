package ninjabrainbot.data.information;

import ninjabrainbot.data.IDataState;
import ninjabrainbot.data.calculator.ICalculatorResult;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;

public class NextThrowDirectionInformationProvider extends InformationMessageProvider {

	public NextThrowDirectionInformationProvider(IDataState dataState, NinjabrainBotPreferences preferences) {
		dataState.calculatorResult().subscribe(__ -> updateInformationMessage(dataState));
	}

	private void updateInformationMessage(IDataState dataState) {
		InformationMessage informationMessageToShow = shouldShowInfoMessage(dataState) ? createtInformationMessage() : null;
		setInformationMessage(informationMessageToShow);
	}

	private boolean shouldShowInfoMessage(IDataState dataState) {
		ICalculatorResult calculatorResult = dataState.calculatorResult().get();
		if (calculatorResult == null || !calculatorResult.success())
			return false;

		return calculatorResult.getBestPrediction().chunk.weight > 0.99;
	}

	private InformationMessage createtInformationMessage() {
		
		
		return new InformationMessage(InformationType.Info, "");
	}

}
