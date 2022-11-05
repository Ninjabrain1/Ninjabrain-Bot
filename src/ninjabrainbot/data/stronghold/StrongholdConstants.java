package ninjabrainbot.data.stronghold;

import ninjabrainbot.io.preferences.NinjabrainBotPreferences.McVersion;

public class StrongholdConstants {

	public static final int snappingRadius = 7;
	public static final int distParam = 32;
	public static final int numStrongholds = 128;
	public static final int numRings = 8;
	public static final int maxChunk = (int) (distParam * ((4 + (numRings - 1) * 6) + 0.5f * 2.5f) + 2 * snappingRadius + 1);

	private static int strongholdCenterChunkCoord;

	/**
	 * Returns the maximum distance the stronghold can be from the given position
	 * (in blocks).
	 */
	public static double getMaxDistance(double x, double z) {
		double r = Math.sqrt(x * x + z * z) / 16.0;
		double maxDistance = Double.POSITIVE_INFINITY;
		for (Ring ring : new RingIterator()) {
			double inner = ring.innerRadius * ring.innerRadius + r * r - 2 * r * ring.innerRadius * Math.cos(Math.PI / ring.numStrongholds);
			double outer = ring.outerRadius * ring.outerRadius + r * r - 2 * r * ring.outerRadius * Math.cos(Math.PI / ring.numStrongholds);
			double max = Math.sqrt(inner > outer ? inner : outer);
			if (max < maxDistance)
				maxDistance = max;
		}
		return (maxDistance + Math.sqrt(2) * (snappingRadius + 0.5)) * 16.0;
	}

	public static void updateStrongholdChunkCoord(McVersion version) {
		boolean pre1_19 = version == McVersion.PRE_119;
		strongholdCenterChunkCoord = pre1_19 ? 8 : 0;
	}

	public static int getStrongholdChunkCoord() {
		return strongholdCenterChunkCoord;
	};

}
