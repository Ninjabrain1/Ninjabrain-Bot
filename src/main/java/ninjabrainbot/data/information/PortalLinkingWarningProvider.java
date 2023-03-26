package ninjabrainbot.data.information;

import ninjabrainbot.data.IDataState;
import ninjabrainbot.data.calculator.ICalculatorResult;
import ninjabrainbot.data.endereye.IThrow;
import ninjabrainbot.data.stronghold.ChunkPrediction;
import ninjabrainbot.util.I18n;

public class PortalLinkingWarningProvider extends InformationMessageProvider {

	public PortalLinkingWarningProvider(IDataState dataState) {
		dataState.calculatorResult().subscribe(__ -> updateInformationMessage(dataState));
	}

	private void updateInformationMessage(IDataState dataState) {
		InformationMessage informationMessageToShow = shouldShowInfoMessage(dataState) ? geOrCreateWarningMessage() : null;
		setInformationMessage(informationMessageToShow);
	}

	private boolean shouldShowInfoMessage(IDataState dataState) {
		ICalculatorResult calculatorResult = dataState.calculatorResult().get();
		if (calculatorResult == null || !calculatorResult.success())
			return false;

		IThrow t = dataState.getThrowSet().get(0);
		double approximatePortalNetherX = t.xInOverworld() / 8;
		double approximatePortalNetherZ = t.zInOverworld() / 8;

		ChunkPrediction bestPrediction = calculatorResult.getBestPrediction();
		double maxAxisDistance = Math.max(Math.abs(approximatePortalNetherX - (bestPrediction.getNetherX() + 0.5)), Math.abs(approximatePortalNetherZ - (bestPrediction.getNetherZ() + 0.5)));

		return maxAxisDistance < 24; // if portals are 22 blocks away they won't link, but the precise location of blind portal is unknown, so use 1 chunk of margin.
	}

	private InformationMessage warningMessage = null;

	private InformationMessage geOrCreateWarningMessage() {
		if (warningMessage == null)
			warningMessage = new InformationMessage(InformationType.Warning, I18n.get("information.portal_linking"));
		return warningMessage;
	}

}
