package ninjabrainbot.model.datastate.common;

import java.io.Serializable;

public class OverworldPosition implements IOverworldPosition, Serializable {

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
