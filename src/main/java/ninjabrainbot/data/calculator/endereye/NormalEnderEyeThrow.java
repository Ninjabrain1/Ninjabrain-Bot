package ninjabrainbot.data.calculator.endereye;

import ninjabrainbot.data.calculator.common.IDetailedPlayerPosition;

/**
 * Represents an eye of ender throw, from a normal F3+C command.
 */
public class NormalEnderEyeThrow extends EnderEyeThrow {

	private final boolean altStandardDeviation;

	public NormalEnderEyeThrow(IDetailedPlayerPosition detailedPlayerPosition, double crosshairCorrection, IStandardDeviationHandler standardDeviationHandler) {
		this(detailedPlayerPosition.xInOverworld(), detailedPlayerPosition.zInPlayerDimension(), getCorrectedHorizontalAngle(detailedPlayerPosition.horizontalAngle(), crosshairCorrection),
				detailedPlayerPosition.verticalAngle(), standardDeviationHandler, 0, false);
	}

	private NormalEnderEyeThrow(double x, double z, double horizontalAngle, double verticalAngle, IStandardDeviationHandler standardDeviationHandler, double correction, boolean altStandardDeviation) {
		super(x, z, horizontalAngle, verticalAngle, standardDeviationHandler, correction);
		this.altStandardDeviation = altStandardDeviation;
	}

	@Override
	public IEnderEyeThrow withCorrection(double correction) {
		return new NormalEnderEyeThrow(x, z, horizontalAngleWithoutCorrection, verticalAngle, standardDeviationHandler, correction, altStandardDeviation);
	}

	@Override
	public IEnderEyeThrow withToggledAltStd() {
		return new NormalEnderEyeThrow(x, z, horizontalAngleWithoutCorrection, verticalAngle, standardDeviationHandler, correction, !altStandardDeviation);
	}

	@Override
	protected double getStandardDeviation(IStandardDeviationHandler standardDeviationHandler) {
		return altStandardDeviation ? standardDeviationHandler.getAlternativeStandardDeviation() : standardDeviationHandler.getNormalStandardDeviation();
	}

	@Override
	protected double getExpectedStandardDeviationForNextEnderEyeThrow(IStandardDeviationHandler standardDeviationHandler) {
		return standardDeviationHandler.getNormalStandardDeviation();
	}

	@Override
	public EnderEyeThrowType getType() {
		return altStandardDeviation ? EnderEyeThrowType.NormalWithAltStd : EnderEyeThrowType.Normal;
	}

	private static double getCorrectedHorizontalAngle(double alpha, double crosshairCorrection) {
		alpha += crosshairCorrection;

		// Determined experimentally, exact cause unknown
		alpha -= 0.00079 * Math.sin((alpha + 45) * Math.PI / 180.0);

		return alpha;
	}

}
