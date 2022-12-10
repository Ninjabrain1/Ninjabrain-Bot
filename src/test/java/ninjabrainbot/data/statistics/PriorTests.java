package ninjabrainbot.data.statistics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import ninjabrainbot.data.datalock.AlwaysUnlocked;
import ninjabrainbot.data.divine.DivineContext;
import ninjabrainbot.data.divine.IDivineContext;
import ninjabrainbot.data.stronghold.Chunk;
import ninjabrainbot.data.stronghold.Ring;
import ninjabrainbot.data.stronghold.RingIterator;

class PriorTests {

	IDivineContext divineContext;

	@BeforeEach
	void setup() {
		divineContext = new DivineContext(new AlwaysUnlocked());
	}

	@ParameterizedTest
	@ValueSource(ints = { 0, 1, 2, 3 })
	void probabilitySumsToNumberOfStringholds(int ringNumber) {
		Ring ring = Ring.get(ringNumber);
		IPrior prior = new Prior(0, 0, (int) Math.ceil(ring.outerRadiusPostSnapping), divineContext);

		int totalStrongholds = 0;
		for (Ring iteratingRing : new RingIterator()) {
			if (iteratingRing.ring > ringNumber)
				break;
			totalStrongholds += iteratingRing.numStrongholds;
		}

		double totalProbability = 0;
		for (Chunk chunk : prior.getChunks()) {
			if (chunk.chunkDistanceSquared(new Chunk(0, 0)) > ring.outerRadiusPostSnapping * ring.outerRadiusPostSnapping)
				continue;
			totalProbability += chunk.weight;
		}

		assertEquals(totalProbability, totalStrongholds, 1e-3);
	}

	@ParameterizedTest
	@CsvSource({ "128, 0, 3", "100, -100, 10", "-50, 111, 6" })
	void getChunks_returnsCorrectChunkCoords(int x, int z, int radius) {
		IPrior prior = new Prior(x, z, radius, divineContext);

		Set<Chunk> chunks = new HashSet<>();

		int numChunks = 0;
		for (Chunk chunk : prior.getChunks()) {
			if (!chunks.add(chunk))
				fail("Already contained chunk " + chunk);
			numChunks++;
			if (Math.abs(chunk.x - x) > radius || Math.abs(chunk.z - z) > radius) {
				fail("Chunk " + chunk + " is not in the square centered at (" + x + " ," + "z), with radius " + radius);
			}
		}
		assertEquals((1 + 2 * radius) * (1 + 2 * radius), numChunks, "Wrong number of chunks.");
	}

	@ParameterizedTest
	@CsvSource({ "128, 0, 3", "100, -100, 10", "-50, 111, 6", "-150, -10, 20" })
	void probabilityAtGivenCoordsIsIndependentOfRadiusOfPrior(int x, int z, int radius) {
		IPrior prior0 = new Prior(x, z, 0, divineContext);
		IPrior prior = new Prior(x, z, radius, divineContext);

		Chunk chunk = null;
		for (Chunk c : prior.getChunks()) {
			if (c.x == x && c.z == z) {
				chunk = c;
				break;
			}
		}

		assertEquals(prior0.getChunks().iterator().next().weight, chunk.weight);
	}

}
