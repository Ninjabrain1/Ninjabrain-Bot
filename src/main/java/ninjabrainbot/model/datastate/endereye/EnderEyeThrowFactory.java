package ninjabrainbot.model.datastate.endereye;

import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.model.datastate.common.IDetailedPlayerPosition;
import ninjabrainbot.model.datastate.common.IPlayerPosition;
import ninjabrainbot.model.datastate.highprecision.BoatEnderEyeThrow;
import ninjabrainbot.model.datastate.highprecision.IBoatDataState;
import ninjabrainbot.util.Assert;

public class EnderEyeThrowFactory implements IEnderEyeThrowFactory {

	private final NinjabrainBotPreferences preferences;
	private final IBoatDataState boatDataState;

	public EnderEyeThrowFactory(NinjabrainBotPreferences preferences, IBoatDataState boatDataState) {
		this.preferences = preferences;
		this.boatDataState = boatDataState;
	}

	@Override
	public IEnderEyeThrow createEnderEyeThrowFromDetailedPlayerPosition(IDetailedPlayerPosition detailedPlayerPosition) {
		Assert.isTrue(detailedPlayerPosition.isInOverworld());

		boolean isBoatThrow = preferences.usePreciseAngle.get() && boatDataState.boatAngle().get() != null;
		if (isBoatThrow)
			return new BoatEnderEyeThrow(detailedPlayerPosition, preferences, boatDataState.boatAngle().get());

		return new NormalEnderEyeThrow(detailedPlayerPosition, preferences.crosshairCorrection.get());
	}

	@Override
	public IEnderEyeThrow createEnderEyeThrowFromLimitedPlayerPosition(IPlayerPosition playerPosition) {
		Assert.isTrue(playerPosition.isInOverworld());

		boolean isBoatThrow = preferences.usePreciseAngle.get() && boatDataState.boatAngle().get() != null;
		if (isBoatThrow)
			return new BoatEnderEyeThrow(playerPosition, preferences, boatDataState.boatAngle().get());

		return new ManualEnderEyeThrow(playerPosition, preferences.crosshairCorrection.get());
	}

}
