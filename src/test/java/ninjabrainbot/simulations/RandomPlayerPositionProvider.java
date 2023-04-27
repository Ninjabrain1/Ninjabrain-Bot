package ninjabrainbot.simulations;

import java.util.Random;

import ninjabrainbot.model.datastate.common.DetailedPlayerPosition;
import ninjabrainbot.model.datastate.common.IDetailedPlayerPosition;
import ninjabrainbot.model.datastate.stronghold.Ring;
import ninjabrainbot.util.Coords;

public class RandomPlayerPositionProvider {

	Random random;

	public RandomPlayerPositionProvider(long seed) {
		random = new Random(seed);
	}

	public IDetailedPlayerPosition nextPlayerPositionFirstRing() {
		Ring ring = Ring.get(0);
		double phi = random.nextDouble() * 2 * Math.PI;
		double r = random.nextDouble() * ring.outerRadiusPostSnapping * 16;
		double x = Coords.getX(r, phi);
		double z = Coords.getZ(r, phi);
		return new DetailedPlayerPosition(x, 80, z, random.nextDouble() * 2 * Math.PI, -31, false);
	}

}
