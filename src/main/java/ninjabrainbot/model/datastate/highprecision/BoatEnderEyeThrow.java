package ninjabrainbot.model.datastate.highprecision;

import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.model.datastate.common.IDetailedPlayerPosition;
import ninjabrainbot.model.datastate.common.IPlayerPosition;
import ninjabrainbot.model.datastate.endereye.EnderEyeThrow;
import ninjabrainbot.model.datastate.endereye.EnderEyeThrowType;
import ninjabrainbot.model.datastate.endereye.IEnderEyeThrow;
import ninjabrainbot.model.environmentstate.StandardDeviationSettings;

/**
 * Represents an ender eye throw from an F3+C command after a boat angle has been registered.
 */
public class BoatEnderEyeThrow extends EnderEyeThrow {

	public BoatEnderEyeThrow(IPlayerPosition playerPosition, NinjabrainBotPreferences preferences, float boatAngle) {
		this(playerPosition.xInOverworld(), playerPosition.zInPlayerDimension(), getPreciseBoatHorizontalAngle(playerPosition.horizontalAngle(), preferences, boatAngle),
				-31.6, 0);
	}

	public BoatEnderEyeThrow(IDetailedPlayerPosition detailedPlayerPosition, NinjabrainBotPreferences preferences, float boatAngle) {
		this(detailedPlayerPosition.xInOverworld(), detailedPlayerPosition.zInPlayerDimension(), getPreciseBoatHorizontalAngle(detailedPlayerPosition.horizontalAngle(), preferences, boatAngle),
				detailedPlayerPosition.verticalAngle(), 0);
	}

	private BoatEnderEyeThrow(double x, double z, double horizontalAngle, double verticalAngle, double correction) {
		super(x, z, horizontalAngle, verticalAngle, correction);
	}

	@Override
	public IEnderEyeThrow withCorrection(double correction) {
		return new BoatEnderEyeThrow(x, z, horizontalAngleWithoutCorrection, verticalAngle, correction);
	}

	@Override
	public IEnderEyeThrow withToggledAltStd() {
		return this;
	}

	@Override
	public double getStandardDeviation(StandardDeviationSettings standardDeviationSettings) {
		return standardDeviationSettings.boatStd;
	}

	@Override
	public double getExpectedStandardDeviationForNextEnderEyeThrow(StandardDeviationSettings standardDeviationSettings) {
		return standardDeviationSettings.boatStd;
	}

	@Override
	public EnderEyeThrowType getType() {
		return EnderEyeThrowType.Boat;
	}

	private static double getCorrectedHorizontalAngle(double alpha, double crosshairCorrection) {
		alpha += crosshairCorrection;

		// Caused by rounding in client-bound move entity packets
		alpha -= 0.000824 * Math.sin((alpha + 45) * Math.PI / 180.0);

		return alpha;
	}

	private static double getPreciseBoatHorizontalAngle(double alpha, NinjabrainBotPreferences preferences, float boatAngle) {
		double sensitivity = preferences.sensitivity.get();
		double preMultiplier = sensitivity * 0.6f + 0.2f;
		preMultiplier = preMultiplier * preMultiplier * preMultiplier * 8.0f;
		double minInc = preMultiplier * 0.15D;
		alpha = boatAngle + Math.round((alpha - boatAngle) / minInc) * minInc;
		return getCorrectedHorizontalAngle(alpha, preferences.crosshairCorrection.get());
	}

}
