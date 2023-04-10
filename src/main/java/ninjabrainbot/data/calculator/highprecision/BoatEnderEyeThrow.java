package ninjabrainbot.data.calculator.highprecision;

import ninjabrainbot.data.calculator.common.IDetailedPlayerPosition;
import ninjabrainbot.data.calculator.endereye.EnderEyeThrow;
import ninjabrainbot.data.calculator.endereye.EnderEyeThrowType;
import ninjabrainbot.data.calculator.endereye.IEnderEyeThrow;
import ninjabrainbot.data.calculator.endereye.IStandardDeviationHandler;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;

/**
 * Represents an ender eye throw from an F3+C command after a boat angle has been registered.
 */
public class BoatEnderEyeThrow extends EnderEyeThrow {

	public BoatEnderEyeThrow(IDetailedPlayerPosition detailedPlayerPosition, NinjabrainBotPreferences preferences, IStandardDeviationHandler standardDeviationHandler, float boatAngle) {
		this(detailedPlayerPosition.xInOverworld(), detailedPlayerPosition.zInPlayerDimension(), getPreciseBoatHorizontalAngle(detailedPlayerPosition.horizontalAngle(), preferences, boatAngle),
				detailedPlayerPosition.verticalAngle(), standardDeviationHandler, 0);
	}

	private BoatEnderEyeThrow(double x, double z, double horizontalAngle, double verticalAngle, IStandardDeviationHandler standardDeviationHandler, double correction) {
		super(x, z, horizontalAngle, verticalAngle, standardDeviationHandler, correction);
	}

	@Override
	public IEnderEyeThrow withCorrection(double correction) {
		return new BoatEnderEyeThrow(x, z, horizontalAngleWithoutCorrection, verticalAngle, standardDeviationHandler, correction);
	}

	@Override
	public IEnderEyeThrow withToggledAltStd() {
		return this;
	}

	@Override
	protected double getStandardDeviation(IStandardDeviationHandler standardDeviationHandler) {
		return standardDeviationHandler.getBoatStandardDeviation();
	}

	@Override
	protected double getExpectedStandardDeviationForNextEnderEyeThrow(IStandardDeviationHandler standardDeviationHandler) {
		return standardDeviationHandler.getBoatStandardDeviation();
	}

	@Override
	public EnderEyeThrowType getType() {
		return EnderEyeThrowType.Boat;
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
		alpha = boatAngle + Math.round((alpha - boatAngle) / minInc) * minInc;
		return getCorrectedHorizontalAngle(alpha, preferences.crosshairCorrection.get());
	}

}
