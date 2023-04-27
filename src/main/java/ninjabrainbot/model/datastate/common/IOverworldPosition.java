package ninjabrainbot.model.datastate.common;

public interface IOverworldPosition {

	double xInOverworld();

	double zInOverworld();

	default double distanceInOverworldSquared(IOverworldPosition other) {
		double dx = xInOverworld() - other.xInOverworld();
		double dz = zInOverworld() - other.zInOverworld();
		return dx * dx + dz * dz;
	}

	default double distanceInOverworld(IOverworldPosition other) {
		return Math.sqrt(distanceInOverworldSquared(other));
	}

}
