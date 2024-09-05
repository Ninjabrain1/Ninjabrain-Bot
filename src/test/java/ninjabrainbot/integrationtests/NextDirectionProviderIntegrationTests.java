package ninjabrainbot.integrationtests;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import ninjabrainbot.model.datastate.calculator.ICalculatorResult;
import ninjabrainbot.model.datastate.common.DetailedPlayerPosition;
import ninjabrainbot.model.datastate.common.IDetailedPlayerPosition;
import ninjabrainbot.model.datastate.stronghold.Chunk;
import ninjabrainbot.model.datastate.endereye.MCDimension;
import ninjabrainbot.model.information.NextThrowDirectionInformationProvider;
import ninjabrainbot.simulations.RandomPlayerPositionProvider;
import ninjabrainbot.util.Assert;
import ninjabrainbot.util.Coords;
import ninjabrainbot.util.Logger;
import org.junit.jupiter.api.Test;

public class NextDirectionProviderIntegrationTests {

	@Test
	void expectedCertaintyOnNextThrowIsAtLeastAsGoodAsPromised() {
		Logger.enabled = false;
		// Arrange
		Random chunkRandom = new Random(111);
		IntegrationTestBuilder testBuilder = new IntegrationTestBuilder().withProSettings();
		testBuilder.preferences.informationDirectionHelpEnabled.set(true);
		RandomPlayerPositionProvider playerPositionProvider = new RandomPlayerPositionProvider(123);
		NextThrowDirectionInformationProvider infoMessage = new NextThrowDirectionInformationProvider(testBuilder.dataState, testBuilder.environmentState, testBuilder.preferences);

		// Act
		double totalProbability = 0;
		int samples = 0;
		for (int i = 0; i < 100; i++) {
			testBuilder.resetCalculator();
			IDetailedPlayerPosition firstPlayerPosition = playerPositionProvider.nextPlayerPositionFirstRing();
			testBuilder.inputDetailedPlayerPosition(firstPlayerPosition);
			if (infoMessage.get() == null)
				continue;
			Chunk randomStrongholdChunk = sampleRandomChunk(chunkRandom, testBuilder.dataState.calculatorResult().get());
			testBuilder.inputDetailedPlayerPosition(getPlayerPositionForNextThrow(firstPlayerPosition, randomStrongholdChunk, infoMessage.get().message));
			List<Chunk> topChunks = testBuilder.dataState.calculatorResult().get().getTopChunks();
			double eyeSpyProbability = topChunks.get(0).weight + (topChunks.get(0).isNeighboring(topChunks.get(1)) ? topChunks.get(1).weight : 0);
			totalProbability += eyeSpyProbability;
			samples++;
		}
		System.out.println(totalProbability / samples + ", " + samples);

		Assert.isTrue(totalProbability / samples > 0.95, "Average certainty after second throw did not exceed 95%: " + totalProbability / samples);

		Logger.enabled = true;
	}

	private Chunk sampleRandomChunk(Random random, ICalculatorResult calculatorResult) {
		List<Chunk> chunks = calculatorResult.getTopChunks();
		double sample = random.nextDouble();
		double cumProb = 0;
		for (Chunk chunk : chunks) {
			cumProb += chunk.weight;
			if (cumProb > sample)
				return chunk;
		}
		return null;
	}

	private IDetailedPlayerPosition getPlayerPositionForNextThrow(IDetailedPlayerPosition playerPosition, Chunk stronghold, String message) {
		String[] words = message.split(" ");
		int leftDistance = getFirstInt(words);
		double leftAngle = playerPosition.horizontalAngle() / 180.0 * Math.PI * 2 - 0.5 * Math.PI;
		double x = playerPosition.xInOverworld() + Coords.getX(leftDistance, leftAngle);
		double z = playerPosition.zInOverworld() + Coords.getZ(leftDistance, leftAngle);
		double horizontalAngle = Coords.getPhi(stronghold.eightEightX() - x, stronghold.eightEightZ() - z) / Math.PI * 180.0;
		return new DetailedPlayerPosition(x, 80, z, horizontalAngle, -31, MCDimension.OVERWORLD);
	}

	private static int getFirstInt(String[] words) {
		for (String word : words) {
			try {
				return Integer.parseInt(word);
			} catch (NumberFormatException ignored) {
			}
		}
		throw new NumberFormatException("Could not parse string array: " + Arrays.toString(words));
	}

}
