package ninjabrainbot.data.calculator.common;

public class Position implements IPosition {

	private final double x, z;

	public Position(double x, double z) {
		this.x = x;
		this.z = z;
	}

	@Override
	public double xInOverworld() {
		return x;
	}

	@Override
	public double zInOverworld() {
		return z;
	}

}
