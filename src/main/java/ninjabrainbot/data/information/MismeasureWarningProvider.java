package ninjabrainbot.data.information;

import ninjabrainbot.data.IDataState;
import ninjabrainbot.data.calculator.ICalculatorResult;
import ninjabrainbot.data.endereye.IThrow;
import ninjabrainbot.data.stronghold.ChunkPrediction;
import ninjabrainbot.util.I18n;

public class MismeasureWarningProvider extends InformationMessageProvider {

	public MismeasureWarningProvider(IDataState dataState) {
		updateInformationMessage(dataState);
		dataState.calculatorResult().subscribe(__ -> updateInformationMessage(dataState));
	}

	private void updateInformationMessage(IDataState dataState) {
		InformationMessage informationMessageToShow = shouldShowWarningMessage(dataState) ? geOrCreateWarningMessage() : null;
		setInformationMessage(informationMessageToShow);
	}

	private boolean shouldShowWarningMessage(IDataState dataState) {
		ICalculatorResult calculatorResult = dataState.calculatorResult().get();
		if (calculatorResult == null || !calculatorResult.success())
			return false;

		ChunkPrediction bestPrediction = calculatorResult.getBestPrediction();
		double likelihood = 1;
		double expectedLikelihood = 1;
		for (IThrow t : dataState.getThrowSet()) {
			double error = bestPrediction.getAngleError(t);
			double sigma = t.getStd();
			likelihood *= Math.exp(-0.5 * (error / sigma) * (error / sigma));
			expectedLikelihood *= 1.0 / Math.sqrt(2);
		}

		return (likelihood / expectedLikelihood) < 0.01;
	}

	private InformationMessage warningMessage = null;

	private InformationMessage geOrCreateWarningMessage() {
		if (warningMessage == null)
			warningMessage = new InformationMessage(InformationType.Warning, I18n.get("information.mismeasure"));
		return warningMessage;
	}

}
