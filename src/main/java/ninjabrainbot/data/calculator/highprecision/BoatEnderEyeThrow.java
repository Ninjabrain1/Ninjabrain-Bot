package ninjabrainbot.data.calculator.highprecision;

import ninjabrainbot.data.calculator.endereye.EnderEyeThrow;
import ninjabrainbot.data.calculator.endereye.EnderEyeThrowType;
import ninjabrainbot.data.calculator.endereye.IEnderEyeThrow;
import ninjabrainbot.data.calculator.endereye.IStandardDeviationHandler;

public class BoatEnderEyeThrow extends EnderEyeThrow {

	public BoatEnderEyeThrow(double x, double z, double horizontalAngle, double verticalAngle, IStandardDeviationHandler standardDeviationHandler) {
		this(x, z, horizontalAngle, verticalAngle, standardDeviationHandler, 0);
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
	public EnderEyeThrowType getType() {
		return EnderEyeThrowType.Boat;
	}

}
