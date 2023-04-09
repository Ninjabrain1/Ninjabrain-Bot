package ninjabrainbot.data.calculator.common;

public class LimitedPlayerPosition implements ILimitedPlayerPosition {

	private final double x, z, horizontalAngle;

	public LimitedPlayerPosition(double x, double z, double horizontalAngle) {
		this.x = x;
		this.z = z;
		this.horizontalAngle = horizontalAngle;
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
	public double horizontalAngle() {
		return horizontalAngle;
	}

}
