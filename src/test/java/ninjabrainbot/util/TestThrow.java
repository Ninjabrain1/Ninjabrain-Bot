package ninjabrainbot.util;

import ninjabrainbot.data.calculator.endereye.IThrow;

public class TestThrow implements IThrow {

	double x, z, alpha, std;

	public TestThrow(double x, double z, double alpha, double std) {
		this.x = x;
		this.z = z;
		this.alpha = alpha;
		this.std = std;
	}

	@Override
	public double alpha() {
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
	public double xInPlayerDimension() {
		return x;
	}

	@Override
	public double yInPlayerDimension() {
		return 80;
	}

	@Override
	public double zInPlayerDimension() {
		return z;
	}

	@Override
	public double getStd() {
		return std;
	}

	@Override
	public double rawAlpha() {
		return alpha;
	}

	@Override
	public double alpha_0() {
		return alpha;
	}

	@Override
	public double beta() {
		return 0;
	}

	@Override
	public double correction() {
		return 0;
	}

	@Override
	public boolean lookingBelowHorizon() {
		return false;
	}

	@Override
	public boolean isNether() {
		return false;
	}

	@Override
	public int getStdProfileNumber() {
		return 0;
	}

	@Override
	public boolean isMcVersion1_12() {
		return false;
	}

	@Override
	public boolean isBoatThrow() {
		return false;
	}

}
