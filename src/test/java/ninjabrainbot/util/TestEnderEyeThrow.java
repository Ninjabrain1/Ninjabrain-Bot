package ninjabrainbot.util;

import ninjabrainbot.data.calculator.common.IPlayerPosition;
import ninjabrainbot.data.calculator.common.LimitedPlayerPosition;
import ninjabrainbot.data.calculator.endereye.EnderEyeThrowType;
import ninjabrainbot.data.calculator.endereye.IEnderEyeThrow;

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
	public IEnderEyeThrow withCorrection(double correction) {
		return null;
	}

	@Override
	public IEnderEyeThrow withToggledAltStd() {
		return this;
	}

	@Override
	public EnderEyeThrowType getType() {
		return EnderEyeThrowType.Normal;
	}

	@Override
	public double getStd() {
		return std;
	}

	@Override
	public double horizontalAngleWithoutCorrection() {
		return alpha;
	}

	@Override
	public double correction() {
		return 0;
	}

	@Override
	public IPlayerPosition getPlayerPosition() {
		return new LimitedPlayerPosition(x, z, horizontalAngle());
	}

}
