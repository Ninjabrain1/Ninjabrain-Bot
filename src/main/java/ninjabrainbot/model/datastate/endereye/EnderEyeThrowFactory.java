package ninjabrainbot.model.datastate.endereye;

import ninjabrainbot.model.datastate.common.IDetailedPlayerPosition;
import ninjabrainbot.model.datastate.common.IPlayerPosition;
import ninjabrainbot.model.datastate.highprecision.BoatEnderEyeThrow;
import ninjabrainbot.model.datastate.highprecision.IBoatDataState;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.util.Assert;

public class EnderEyeThrowFactory implements IEnderEyeThrowFactory {

	private final NinjabrainBotPreferences preferences;
	private final IBoatDataState boatDataState;
	private final IStandardDeviationHandler standardDeviationHandler;

	public EnderEyeThrowFactory(NinjabrainBotPreferences preferences, IBoatDataState boatDataState, IStandardDeviationHandler standardDeviationHandler) {
		this.preferences = preferences;
		this.boatDataState = boatDataState;
		this.standardDeviationHandler = standardDeviationHandler;
	}

	@Override
	public IEnderEyeThrow createEnderEyeThrowFromDetailedPlayerPosition(IDetailedPlayerPosition detailedPlayerPosition) {
		Assert.isTrue(detailedPlayerPosition.isInOverworld());

		boolean isBoatThrow = preferences.useTallRes.get() && preferences.usePreciseAngle.get() && boatDataState.boatAngle().get() != null;
		if (isBoatThrow)
			return new BoatEnderEyeThrow(detailedPlayerPosition, preferences, standardDeviationHandler, boatDataState.boatAngle().get());

		return new NormalEnderEyeThrow(detailedPlayerPosition, preferences.crosshairCorrection.get(), standardDeviationHandler);
	}

	@Override
	public IEnderEyeThrow createEnderEyeThrowFromLimitedPlayerPosition(IPlayerPosition playerPosition) {
		Assert.isTrue(playerPosition.isInOverworld());

		return new ManualEnderEyeThrow(playerPosition, preferences.crosshairCorrection.get(), standardDeviationHandler);
	}

}
