package ninjabrainbot.model.statistics;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import ninjabrainbot.model.datastate.common.DetachedDomainModel;
import ninjabrainbot.model.datastate.divine.DivineContext;
import ninjabrainbot.model.datastate.statistics.ApproximatedPrior;
import ninjabrainbot.model.datastate.statistics.IPrior;
import ninjabrainbot.model.datastate.statistics.Prior;
import ninjabrainbot.model.datastate.stronghold.Chunk;
import ninjabrainbot.model.datastate.stronghold.Ring;
import ninjabrainbot.model.datastate.stronghold.RingIterator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class ApproximatedPriorTests {

	DivineContext divineContext;

	@BeforeEach
	void setup() {
		divineContext = new DivineContext(new DetachedDomainModel());
	}

	@ParameterizedTest
	@ValueSource(ints = { 0, 1, 2, 3 })
	void probabilitySumsToNumberOfStringholds(int ringNumber) {
		Ring ring = Ring.get(ringNumber);
		IPrior prior = new ApproximatedPrior(0, 0, (int) Math.ceil(ring.outerRadiusPostSnapping), divineContext);

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

		assertEquals(totalProbability, totalStrongholds, totalStrongholds * 1e-4);
	}

//	@ParameterizedTest
//	@ValueSource(ints = { 0, 1, 2, 3 })
//	void probabilitySumsToNumberOfStringholds_withFossilDivine(int fossil) {
//		divineContext.setFossil(new Fossil(fossil));
//		Ring ring = Ring.get(0);
//		IPrior prior = new ApproximatedPrior(0, 0, (int) Math.ceil(ring.outerRadiusPostSnapping), divineContext);
//
//		double totalProbability = 0;
//		for (Chunk chunk : prior.getChunks()) {
//			if (chunk.chunkDistanceSquared(new Chunk(0, 0)) > ring.outerRadiusPostSnapping * ring.outerRadiusPostSnapping)
//				continue;
//			totalProbability += chunk.weight;
//		}
//
//		assertEquals(totalProbability, ring.numStrongholds, ring.numStrongholds * 1e-2);
//	}

	@ParameterizedTest
	@CsvSource({ "128, 0, 3", "100, -100, 10", "-50, 111, 6" })
	void getChunks_returnsCorrectChunkCoords(int x, int z, int radius) {
		IPrior prior = new ApproximatedPrior(x, z, radius, divineContext);

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
		IPrior prior0 = new ApproximatedPrior(x, z, 0, divineContext);
		IPrior prior = new ApproximatedPrior(x, z, radius, divineContext);

		Chunk chunk = null;
		for (Chunk c : prior.getChunks()) {
			if (c.x == x && c.z == z) {
				chunk = c;
				break;
			}
		}

		assertEquals(prior0.getChunks().iterator().next().weight, chunk.weight);
	}

	@Test
	void approximatedPriorShouldBeNonZeroIfTruePriorIsNonZero() {
		Ring ring = Ring.get(0);
		int radius = (int) Math.ceil(ring.outerRadiusPostSnapping);

		IPrior approximatedPrior = new ApproximatedPrior(0, 0, radius, divineContext);
		IPrior truePrior = new Prior(0, 0, radius, divineContext);

		Map<Chunk, Chunk> expectedChunks = new HashMap<>();
		for (Chunk chunk : truePrior.getChunks()) {
			expectedChunks.put(chunk, chunk);
		}

		for (Chunk chunk : approximatedPrior.getChunks()) {
			Chunk expected = expectedChunks.get(chunk);
			if (chunk.weight == 0)
				assertEquals(0, expected.weight, "If the real probability is non-zero, the approximated probability has to be non-zero.");
		}
	}

	@Test
	void testApproximationAccuracyFirstRing() {
		Ring ring = Ring.get(0);
		int radius = (int) Math.ceil(ring.outerRadiusPostSnapping);
		double ringArea = Math.PI * (ring.outerRadiusPostSnapping * ring.outerRadiusPostSnapping - ring.innerRadiusPostSnapping * ring.innerRadiusPostSnapping);
		double averageWeightInRing = ring.numStrongholds / ringArea;

		IPrior approximatedPrior = new ApproximatedPrior(0, 0, radius, divineContext);
		IPrior truePrior = new Prior(0, 0, radius, divineContext);

		Map<Chunk, Chunk> expectedChunks = new HashMap<>();
		for (Chunk chunk : truePrior.getChunks()) {
			expectedChunks.put(chunk, chunk);
		}

		double totalSquaredError = 0;
		double totalError = 0;
		int numNonZeroChunks = 0;
		int numChunks = 0;
		for (Chunk chunk : approximatedPrior.getChunks()) {
			Chunk expected = expectedChunks.get(chunk);
			assertEquals(expected.weight, chunk.weight, averageWeightInRing * 0.15, "Maximum allowed relative error is 15%, failed for chunk: " + chunk);
			numChunks++;
			double error = chunk.weight - expected.weight;
			totalError += error;
			if (expected.weight != 0) {
				numNonZeroChunks++;
				totalSquaredError += error * error;
			}
		}
		double meanError = totalError / numChunks;
		double rootMeanSquare = Math.sqrt(totalSquaredError / numNonZeroChunks);
		assertEquals(0, rootMeanSquare / averageWeightInRing, 0.015, "Relative RMS in ring exceeded maximum tolerance of 1.5%.");
		assertEquals(0, meanError, 1e-6, "Mean error exceeded maximum tolerance of 1 PPM.");
	}

}
