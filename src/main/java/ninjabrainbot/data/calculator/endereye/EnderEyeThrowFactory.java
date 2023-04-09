package ninjabrainbot.data.calculator.endereye;

import ninjabrainbot.data.calculator.common.IDetailedPlayerPosition;
import ninjabrainbot.data.calculator.common.IPlayerPosition;
import ninjabrainbot.data.calculator.highprecision.IBoatDataState;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;

public class EnderEyeThrowFactory implements IEnderEyeThrowFactory {

	private final NinjabrainBotPreferences preferences;
	private final IBoatDataState boatDataState;
	private final IStdProfile stdProfile;

	public EnderEyeThrowFactory(NinjabrainBotPreferences preferences, IBoatDataState boatDataState, IStdProfile stdProfile) {
		this.preferences = preferences;
		this.boatDataState = boatDataState;
		this.stdProfile = stdProfile;
	}

	@Override
	public IEnderEyeThrow createEnderEyeThrowFromDetailedPlayerPosition(IDetailedPlayerPosition detailedPlayerPosition) {
		assert detailedPlayerPosition.isInOverworld();

		boolean boatThrow = preferences.useTallRes.get() && preferences.usePreciseAngle.get() && boatDataState.boatAngle().get() != null;

		double correctedHorizontalAngle = detailedPlayerPosition.horizontalAngle();
		if (boatThrow)
			correctedHorizontalAngle += getPreciseBoatHorizontalAngle(correctedHorizontalAngle, preferences, boatDataState.boatAngle().get());
		correctedHorizontalAngle = getCorrectedHorizontalAngle(correctedHorizontalAngle, preferences.crosshairCorrection.get());

		return new EnderEyeThrow(detailedPlayerPosition.xInPlayerDimension(), detailedPlayerPosition.zInPlayerDimension(),
				correctedHorizontalAngle, detailedPlayerPosition.verticalAngle(), boatThrow ? ThrowType.Boat : ThrowType.Normal, stdProfile);
	}

	@Override
	public IEnderEyeThrow createEnderEyeThrowFromLimitedPlayerPosition(IPlayerPosition playerPosition) {
		assert playerPosition.isInOverworld();

		double correctedHorizontalAngle = playerPosition.horizontalAngle() + preferences.crosshairCorrection.get();
		return new EnderEyeThrow(playerPosition.xInPlayerDimension(), playerPosition.zInPlayerDimension(), correctedHorizontalAngle, -31, ThrowType.McVersion1_12, stdProfile);
	}

	private static double getCorrectedHorizontalAngle(double alpha, double crosshairCorrection) {
		alpha += crosshairCorrection;

		// Determined experimentally, exact cause unknown
		alpha -= 0.00079 * Math.sin((alpha + 45) * Math.PI / 180.0);

		return alpha;
	}

	private static double getPreciseBoatHorizontalAngle(double alpha, NinjabrainBotPreferences preferences, float boatAngle) {
		double sensitivity = preferences.sensitivity.get();
		double preMultiplier = sensitivity * 0.6f + 0.2f;
		preMultiplier = preMultiplier * preMultiplier * preMultiplier * 8.0f;
		double minInc = preMultiplier * 0.15D;
		return boatAngle + Math.round((alpha - boatAngle) / minInc) * minInc;
	}

}
