package ninjabrainbot.data.calculator.common;

/**
 * Limited player position information, for pre 1.12 where coordinates are inputted manually.
 */
public class LimitedPlayerPosition implements IPlayerPosition {

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

	@Override
	public double xInPlayerDimension() {
		return x;
	}

	@Override
	public double zInPlayerDimension() {
		return z;
	}

	@Override
	public boolean isInOverworld() {
		return true;
	}

	@Override
	public boolean isInNether() {
		return false;
	}
}
