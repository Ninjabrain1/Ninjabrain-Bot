package ninjabrainbot.model.datastate.common;

import ninjabrainbot.model.datastate.endereye.MCDimension;

public class DetailedPlayerPosition implements IDetailedPlayerPosition {

	private final double x, y, z, horizontalAngle, verticalAngle;
	private final MCDimension dimension;

	public DetailedPlayerPosition(double x, double y, double z, double horizontalAngle, double verticalAngle, MCDimension dimension) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.horizontalAngle = horizontalAngle;
		this.verticalAngle = verticalAngle;
		this.dimension = dimension;
	}

	@Override
	public double xInOverworld() {
		return x * (dimension == MCDimension.NETHER ? 8.0 : 1.0);
	}

	@Override
	public double zInOverworld() {
		return z * (dimension == MCDimension.NETHER ? 8.0 : 1.0);
	}

	@Override
	public double xInPlayerDimension() {
		return x;
	}

	@Override
	public double yInPlayerDimension() {
		return y;
	}

	@Override
	public double zInPlayerDimension() {
		return z;
	}

	@Override
	public double horizontalAngle() {
		return horizontalAngle;
	}

	@Override
	public double verticalAngle() {
		return verticalAngle;
	}

	@Override
	public boolean lookingBelowHorizon() {
		return verticalAngle > 0;
	}

	@Override
	public boolean isInOverworld() {
		return dimension == MCDimension.OVERWORLD;
	}

	@Override
	public boolean isInNether() {
		return dimension == MCDimension.NETHER;
	}

	@Override
	public boolean isInEnd() {
		return dimension == MCDimension.END;
	}
}
