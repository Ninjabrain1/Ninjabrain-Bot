package ninjabrainbot.model.datastate.endereye;

import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.model.datastate.common.IDetailedPlayerPosition;
import ninjabrainbot.model.datastate.common.IPlayerPosition;
import ninjabrainbot.util.Assert;

public class EnderEyeThrowFactory implements IEnderEyeThrowFactory {

	private final NinjabrainBotPreferences preferences;

	public EnderEyeThrowFactory(NinjabrainBotPreferences preferences) {
		this.preferences = preferences;
	}

	@Override
	public IEnderEyeThrow createEnderEyeThrowFromDetailedPlayerPosition(IDetailedPlayerPosition detailedPlayerPosition) {
		Assert.isTrue(detailedPlayerPosition.isInOverworld());
		return new NormalEnderEyeThrow(detailedPlayerPosition, preferences.crosshairCorrection.get());
	}

	@Override
	public IEnderEyeThrow createEnderEyeThrowFromLimitedPlayerPosition(IPlayerPosition playerPosition) {
		Assert.isTrue(playerPosition.isInOverworld());

		return new ManualEnderEyeThrow(playerPosition, preferences.crosshairCorrection.get());
	}

}
