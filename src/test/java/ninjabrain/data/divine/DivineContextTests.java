package ninjabrain.data.divine;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ninjabrainbot.data.datalock.AlwaysUnlocked;
import ninjabrainbot.data.divine.BuriedTreasure;
import ninjabrainbot.data.divine.BuriedTreasureMonteCarloSimulator;
import ninjabrainbot.data.divine.DivineContext;
import ninjabrainbot.data.endereye.Throw;
import ninjabrainbot.data.statistics.RayApproximatedPrior;
import ninjabrainbot.data.stronghold.Chunk;
import ninjabrainbot.io.preferences.MultipleChoicePreferenceDataTypes.McVersion;

public class DivineContextTests {

	@BeforeEach
	void setup() {
	}

	@Test
	void samplesMatchExpectedDistribution() {
		int sampleSize = 10000000;

		BuriedTreasureMonteCarloSimulator simulator = new BuriedTreasureMonteCarloSimulator();
		simulator.setBuriedTreasure(new BuriedTreasure(1, 15));
		Random rRandom = new Random();

		HashMap<String, Integer> samples = new HashMap<>();
		samples.put("17,-97", 0);

		for (int i = 0; i < sampleSize; i++) {
			double r = 4.0 * 32.0 + (rRandom.nextDouble() - 0.5D) * (double) 32.0 * 2.5;
			double phi = simulator.nextAngle();
			int cx = (int) Math.round(r * Math.cos(phi));
			int cz = (int) Math.round(r * Math.sin(phi));

			String key = cx + "," + cz;
			if (samples.containsKey(key)) {
				samples.put(key, samples.get(key) + 1);
			} else {
				samples.put(key, 1);
			}
		}
		int max = 0;
		String maxKey = "";
		for (String key : samples.keySet()) {
			int a = samples.get(key);
			if (a > max) {
				max = a;
				maxKey = key;
			}
		}
		System.out.println(maxKey);
		System.out.println((float) max / sampleSize);
		System.out.println(samples.get("0,-90") / sampleSize);
		assertEquals(0.00163, (double) samples.get("17,-97") / sampleSize, 0.0001);
	}

	@Test
	void test() {
		DivineContext divineContext = new DivineContext(new AlwaysUnlocked());

		RayApproximatedPrior prior = new RayApproximatedPrior(Throw.parseF3C("/execute in minecraft:overworld run tp @s 2005.35 64.00 2.57 7.85 -31.08", 0, new AlwaysUnlocked()), divineContext, McVersion.PRE_119);

		for (Chunk chunk : prior.getChunks()) {
			if (chunk.x == 111 && chunk.z == 100)
				System.out.println(chunk);
			if (chunk.x == 119 && chunk.z == 42)
				System.out.println(chunk);
		}

		divineContext.setBuriedTreasure(new BuriedTreasure(-10, -43));
		prior = new RayApproximatedPrior(Throw.parseF3C("/execute in minecraft:overworld run tp @s 2005.35 64.00 2.57 7.85 -31.08", 0, new AlwaysUnlocked()), divineContext, McVersion.PRE_119);

		for (Chunk chunk : prior.getChunks()) {
			if (chunk.x == 111 && chunk.z == 100)
				System.out.println(chunk);
			if (chunk.x == 119 && chunk.z == 42)
				System.out.println(chunk);
		}
	}

}
