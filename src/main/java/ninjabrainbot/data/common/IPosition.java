package ninjabrainbot.data.common;

public interface IPosition {

	public double xInOverworld();

	public double zInOverworld();

	public default double distanceInOverworldSquared(IPosition other) {
		double dx = xInOverworld() - other.xInOverworld();
		double dz = zInOverworld() - other.zInOverworld();
		return dx * dx + dz * dz;
	}

	public default double distanceInOverworld(IPosition other) {
		return Math.sqrt(distanceInOverworldSquared(other));
	}

}
