package ninjabrainbot.model.information;

import ninjabrainbot.model.datastate.IDataState;
import ninjabrainbot.model.datastate.common.ResultType;
import ninjabrainbot.model.datastate.calculator.ICalculatorResult;
import ninjabrainbot.model.datastate.endereye.IEnderEyeThrow;
import ninjabrainbot.model.datastate.stronghold.ChunkPrediction;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.util.I18n;

public class PortalLinkingWarningProvider extends InformationMessageProvider {

	private final IDataState dataState;

	public PortalLinkingWarningProvider(IDataState dataState, NinjabrainBotPreferences preferences) {
		super(preferences.informationPortalLinkingEnabled);
		this.dataState = dataState;
		disposeHandler.add(dataState.calculatorResult().subscribe(this::raiseInformationMessageChanged));
		disposeHandler.add(dataState.resultType().subscribe(this::raiseInformationMessageChanged));
	}

	@Override
	protected boolean shouldShowInformationMessage() {
		if (dataState.resultType().get() != ResultType.TRIANGULATION)
			return false;

		ICalculatorResult calculatorResult = dataState.calculatorResult().get();
		if (calculatorResult == null || !calculatorResult.success())
			return false;

		IEnderEyeThrow t = dataState.getThrowList().get(0);
		double approximatePortalNetherX = t.xInOverworld() / 8;
		double approximatePortalNetherZ = t.zInOverworld() / 8;

		ChunkPrediction bestPrediction = calculatorResult.getBestPrediction();
		double maxAxisDistance = Math.max(Math.abs(approximatePortalNetherX - (bestPrediction.xInNether() + 0.5)), Math.abs(approximatePortalNetherZ - (bestPrediction.zInNether() + 0.5)));

		return maxAxisDistance < 24; // if portals are 22 blocks away they won't link, but the precise location of blind portal is unknown, so use 1 chunk of margin.
	}

	private InformationMessage warningMessage = null;

	@Override
	protected InformationMessage getInformationMessage() {
		if (warningMessage == null)
			warningMessage = new InformationMessage(InformationType.Warning, I18n.get("information.portal_linking"));
		return warningMessage;
	}

}
