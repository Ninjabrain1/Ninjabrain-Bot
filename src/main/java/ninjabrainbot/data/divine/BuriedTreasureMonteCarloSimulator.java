package ninjabrainbot.data.divine;

import java.util.Random;

public class BuriedTreasureMonteCarloSimulator implements IBuriedTreasureSimulator {

	private static double generationProbability = 0.01;

	private long regionSalt;

	private Random seedRng;
	private Random strongholdAngleRng;

	public BuriedTreasureMonteCarloSimulator() {
		seedRng = new Random();
		strongholdAngleRng = new Random();
	}

	@Override
	public void setBuriedTreasure(BuriedTreasure buriedTreasure) {
		regionSalt = (long) buriedTreasure.x * 341873128712L + (long) buriedTreasure.z * 132897987541L + 10387320L;
	}

	@Override
	public double nextAngle() {
		long seed = seedRng.nextLong();
		while (!seedSatisfiesBuriedTreasureCondition(seed)) {
			seed = seedRng.nextLong();
		}
		strongholdAngleRng.setSeed(seed);
		return strongholdAngleRng.nextDouble() * 3.141592653589793 * 2.0;
	}

	private boolean seedSatisfiesBuriedTreasureCondition(long seed) {
		long internalSeed = (seed + regionSalt) ^ 0x5deece66dL;
		internalSeed = lcg(internalSeed);
		if (new Random(seed + regionSalt).nextFloat() > generationProbability)
			return false;
		return true;
	}

	private static long lcg(long seed) {
		seed *= 0x5deece66dL;
		seed += 11L;
		seed &= 0xffff_ffff_ffffL;
		return seed;
	}

}
