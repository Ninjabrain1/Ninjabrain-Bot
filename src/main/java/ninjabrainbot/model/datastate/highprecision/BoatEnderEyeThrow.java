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
		this(playerPosition.xInOverworld(), playerPosition.zInPlayerDimension(), getPreciseBoatHorizontalAngle(playerPosition.horizontalAngle(), preferences.sensitivityManual.get(), preferences.crosshairCorrection.get(), boatAngle),
				-31.6, 0, 0);
	}

	public BoatEnderEyeThrow(IDetailedPlayerPosition detailedPlayerPosition, NinjabrainBotPreferences preferences, float boatAngle) {
		this(detailedPlayerPosition.xInOverworld(), detailedPlayerPosition.zInPlayerDimension(), getPreciseBoatHorizontalAngle(detailedPlayerPosition.horizontalAngle(), preferences.sensitivityAutomatic.get(), preferences.crosshairCorrection.get(), boatAngle),
				detailedPlayerPosition.verticalAngle(), 0, 0);
	}

	private BoatEnderEyeThrow(double x, double z, double horizontalAngle, double verticalAngle, double correction, int correctionIncrements) {
		super(x, z, horizontalAngle, verticalAngle, correction, correctionIncrements);
	}

	@Override
	public IEnderEyeThrow withCorrection(double correction, int correctionIncrements) {
		return new BoatEnderEyeThrow(x, z, horizontalAngleWithoutCorrection, verticalAngle, correction, correctionIncrements);
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

	private static double getPreciseBoatHorizontalAngle(double alpha, double sensitivity, double crosshairCorrection, float boatAngle) {
		double preMultiplier = sensitivity * (double) 0.6f + (double) 0.2f;
		preMultiplier = preMultiplier * preMultiplier * preMultiplier * 8.0D;
		double minInc = preMultiplier * 0.15D;
		alpha = (float) (boatAngle + Math.round((alpha - boatAngle) / minInc) * minInc);
		return getCorrectedHorizontalAngle(alpha, crosshairCorrection);
	}

}
