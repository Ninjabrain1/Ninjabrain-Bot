package ninjabrainbot.model.statistics;

import java.util.HashMap;
import java.util.Map;

import ninjabrainbot.io.preferences.enums.McVersion;
import ninjabrainbot.model.datastate.divine.DivineContext;
import ninjabrainbot.model.datastate.statistics.ApproximatedPrior;
import ninjabrainbot.model.datastate.statistics.IPrior;
import ninjabrainbot.model.datastate.statistics.Prior;
import ninjabrainbot.model.datastate.statistics.RayApproximatedPrior;
import ninjabrainbot.model.datastate.stronghold.Chunk;
import ninjabrainbot.model.datastate.stronghold.Ring;
import ninjabrainbot.util.TestUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RayApproximatedPriorTests {

	static IPrior truePrior;
	static DivineContext divineContext;

	@BeforeAll
	static void calculateTruePrior() {
		divineContext = new DivineContext(null);
		Ring ring = Ring.get(1);
		int radius = (int) Math.ceil(ring.outerRadiusPostSnapping);
		truePrior = new Prior(0, 0, radius, divineContext);
	}

	@ParameterizedTest
	@CsvSource({ "8, 8, 0", "0, -5000, 25", "1000, -1000, 45", "-150, -10, 20" })
	void probabilitiesAreIdenticalToApproximatedPrior(double x, double z, double angle) {
		Ring ring = Ring.get(0);
		int radius = (int) Math.ceil(ring.outerRadiusPostSnapping);
		double ringArea = Math.PI * (ring.outerRadiusPostSnapping * ring.outerRadiusPostSnapping - ring.innerRadiusPostSnapping * ring.innerRadiusPostSnapping);
		double averageWeightInRing = ring.numStrongholds / ringArea;

		IPrior rayApproximatedPrior = new RayApproximatedPrior(TestUtils.createRay(x, z, angle), 10 * Math.PI / 180, divineContext, McVersion.PRE_119);
		IPrior approximatedPrior = new ApproximatedPrior(0, 0, radius, divineContext);

		Map<Chunk, Chunk> expectedChunks = new HashMap<>();
		for (Chunk chunk : approximatedPrior.getChunks()) {
			expectedChunks.put(chunk, chunk);
		}

		double totalSquaredError = 0;
		double totalError = 0;
		int numNonZeroChunks = 0;
		int numChunks = 0;
		for (Chunk chunk : rayApproximatedPrior.getChunks()) {
			Chunk expected = expectedChunks.get(chunk);
			if (expected == null)
				continue;
			assertEquals(expected.weight, chunk.weight, averageWeightInRing * 0.05, "Maximum allowed relative error is 5%, failed for chunk: " + chunk);
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
		assertEquals(0, rootMeanSquare / averageWeightInRing, 0.001, "Relative RMS in ring exceeded maximum tolerance of 0.1%.");
		assertEquals(0, meanError, 1e-6, "Mean error exceeded maximum tolerance of 1 PPM.");
	}

//	@ParameterizedTest
//	@CsvSource({ "8, 8, 0, 3", "0, -500, 25, 4", "-1000, 1000, -135, 11", "-150, -10, 20, 4" })
//	void probabilitiesAreIdenticalToApproximatedPrior_withFossilDivine(double x, double z, double angle, int fossil) {
//		DivineContext divineContext = new DivineContext(new AlwaysUnlocked());
//		divineContext.setFossil(new Fossil(fossil));
//		Ring ring = Ring.get(0);
//		int radius = (int) Math.ceil(ring.outerRadiusPostSnapping);
//
//		double ringArea = Math.PI * (ring.outerRadiusPostSnapping * ring.outerRadiusPostSnapping - ring.innerRadiusPostSnapping * ring.innerRadiusPostSnapping);
//		double averageWeightInRing = ring.numStrongholds / ringArea * divineContext.relativeDensity();
//
//		IPrior rayApproximatedPrior = new RayApproximatedPrior(TestUtils.createRay(x, z, angle), 10 * Math.PI / 180, divineContext, McVersion.PRE_119);
//		IPrior approximatedPrior = new ApproximatedPrior(0, 0, radius, divineContext);
//
//		Map<Chunk, Chunk> expectedChunks = new HashMap<>();
//		for (Chunk chunk : approximatedPrior.getChunks()) {
//			expectedChunks.put(chunk, chunk);
//		}
//
//		double totalSquaredError = 0;
//		double totalError = 0;
//		int numNonZeroChunks = 0;
//		int numChunks = 0;
//		for (Chunk chunk : rayApproximatedPrior.getChunks()) {
//			Chunk expected = expectedChunks.get(chunk);
//			if (expected == null)
//				continue;
//			assertEquals(expected.weight, chunk.weight, averageWeightInRing * 0.05, "Maximum allowed relative error is 10%, failed for chunk: " + chunk.toString());
//			numChunks++;
//			double error = chunk.weight - expected.weight;
//			totalError += error;
//			if (expected.weight != 0) {
//				numNonZeroChunks++;
//				totalSquaredError += error * error;
//			}
//		}
//		double meanError = totalError / numChunks;
//		double rootMeanSquare = Math.sqrt(totalSquaredError / numNonZeroChunks);
//		assertEquals(0, rootMeanSquare / averageWeightInRing, 0.001, "Relative RMS in ring exceeded maximum tolerance of 0.1%.");
//		assertEquals(0, meanError, 1e-6, "Mean error exceeded maximum tolerance of 1 PPM.");
//	}

	@ParameterizedTest
	@CsvSource({ "8, 8, 0", "1000, -1000, 45", "-150, -10, 20" })
	void approximationAccuracyFirstRing(double x, double z, double angle) {
		Ring ring = Ring.get(0);
		double ringArea = Math.PI * (ring.outerRadiusPostSnapping * ring.outerRadiusPostSnapping - ring.innerRadiusPostSnapping * ring.innerRadiusPostSnapping);
		double averageWeightInRing = ring.numStrongholds / ringArea;

		IPrior rayApproximatedPrior = new RayApproximatedPrior(TestUtils.createRay(x, z, angle), 10 * Math.PI / 180, divineContext, McVersion.PRE_119);

		Map<Chunk, Chunk> expectedChunks = new HashMap<>();
		for (Chunk chunk : truePrior.getChunks()) {
			expectedChunks.put(chunk, chunk);
		}

		double totalSquaredError = 0;
		double totalError = 0;
		int numNonZeroChunks = 0;
		int numChunks = 0;
		for (Chunk chunk : rayApproximatedPrior.getChunks()) {
			Chunk expected = expectedChunks.get(chunk);
			if (expected == null)
				continue;
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
		assertEquals(0, rootMeanSquare / averageWeightInRing, 0.02, "Relative RMS in ring exceeded maximum tolerance of 2%.");
		assertEquals(0, meanError, 1e-6, "Mean error exceeded maximum tolerance of 1 PPM.");
	}

	@ParameterizedTest
	@CsvSource({ "0, -5000, 25", "-1000, 5000, -95", "-4000, 300, -45", "3000, -4000, -170" })
	void approximationAccuracySecondRing(double x, double z, double angle) {
		Ring ring = Ring.get(1);
		double ringArea = Math.PI * (ring.outerRadiusPostSnapping * ring.outerRadiusPostSnapping - ring.innerRadiusPostSnapping * ring.innerRadiusPostSnapping);
		double averageWeightInRing = ring.numStrongholds / ringArea;

		IPrior rayApproximatedPrior = new RayApproximatedPrior(TestUtils.createRay(x, z, angle), 10 * Math.PI / 180, divineContext, McVersion.PRE_119);

		Map<Chunk, Chunk> expectedChunks = new HashMap<>();
		for (Chunk chunk : truePrior.getChunks()) {
			expectedChunks.put(chunk, chunk);
		}

		double totalSquaredError = 0;
		double totalError = 0;
		int numNonZeroChunks = 0;
		int numChunks = 0;
		for (Chunk chunk : rayApproximatedPrior.getChunks()) {
			Chunk expected = expectedChunks.get(chunk);
			if (expected == null)
				continue;
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
		assertEquals(0, rootMeanSquare / averageWeightInRing, 0.03, "Relative RMS in ring exceeded maximum tolerance of 3%.");
		assertEquals(0, meanError, 1e-6, "Mean error exceeded maximum tolerance of 1 PPM.");
	}

}
