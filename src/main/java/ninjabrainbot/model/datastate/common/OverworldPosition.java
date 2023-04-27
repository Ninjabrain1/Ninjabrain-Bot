package ninjabrainbot.model.datastate.common;

public class OverworldPosition implements IOverworldPosition {

	private final double x, z;

	public OverworldPosition(double x, double z) {
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
