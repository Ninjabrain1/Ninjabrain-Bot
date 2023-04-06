package ninjabrainbot.data.common;

public interface IPosition {

	double xInOverworld();

	double zInOverworld();

	default double distanceInOverworldSquared(IPosition other) {
		double dx = xInOverworld() - other.xInOverworld();
		double dz = zInOverworld() - other.zInOverworld();
		return dx * dx + dz * dz;
	}

	default double distanceInOverworld(IPosition other) {
		return Math.sqrt(distanceInOverworldSquared(other));
	}

}
