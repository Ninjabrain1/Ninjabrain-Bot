package ninjabrainbot.data.calculator.endereye;

/**
 * Represents an eye of ender throw, from a normal F3+C command.
 */
public class NormalEnderEyeThrow extends EnderEyeThrow {

	private final boolean altStandardDeviation;

	public NormalEnderEyeThrow(double x, double z, double horizontalAngle, double verticalAngle, IStandardDeviationHandler standardDeviationHandler) {
		this(x, z, horizontalAngle, verticalAngle, standardDeviationHandler, 0, false);
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
	public EnderEyeThrowType getType() {
		return altStandardDeviation ? EnderEyeThrowType.NormalWithAltStd : EnderEyeThrowType.Normal;
	}

}
