package ninjabrainbot.util;

import ninjabrainbot.model.datastate.endereye.EnderEyeThrowType;
import ninjabrainbot.model.datastate.endereye.IEnderEyeThrow;
import ninjabrainbot.model.environmentstate.StandardDeviationSettings;

public class TestEnderEyeThrow implements IEnderEyeThrow {

	double x, z, alpha, std;

	public TestEnderEyeThrow(double x, double z, double alpha, double std) {
		this.x = x;
		this.z = z;
		this.alpha = alpha;
		this.std = std;
	}

	@Override
	public double horizontalAngle() {
		return alpha;
	}

	@Override
	public double xInOverworld() {
		return x;
	}

	@Override
	public double zInOverworld() {
		return z;
	}

	@Override
	public double verticalAngle() {
		return -31;
	}

	@Override
	public double horizontalAngleWithoutCorrection() {
		return alpha;
	}

	@Override
	public IEnderEyeThrow withCorrection(double correction, int correctionIncrements) {
		return null;
	}

	@Override
	public IEnderEyeThrow withToggledAltStd() {
		return this;
	}

	@Override
	public EnderEyeThrowType getType() {
		return EnderEyeThrowType.NORMAL;
	}

	@Override
	public double getStandardDeviation(StandardDeviationSettings standardDeviationHandler) {
		return std;
	}

	@Override
	public double getExpectedStandardDeviationForNextEnderEyeThrow(StandardDeviationSettings standardDeviationHandler) {
		return std;
	}

	@Override
	public double correction() {
		return 0;
	}

	@Override
	public int correctionIncrements() {
		return 0;
	}

}
