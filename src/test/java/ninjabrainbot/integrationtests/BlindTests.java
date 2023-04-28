package ninjabrainbot.integrationtests;

import ninjabrainbot.model.datastate.blind.BlindResult;
import ninjabrainbot.util.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class BlindTests {

	private static final String input1_1 =
			"/execute in minecraft:the_nether run tp @s -1.00 80.00 -19.50 0.04 0.08," +
			"0";
	private static final String input1_2 =
			"/execute in minecraft:the_nether run tp @s -3.50 81.50 95.33 1.37 47.75," +
			"0";
	private static final String input1_3 =
			"/execute in minecraft:the_nether run tp @s 10.03 81.50 -107.62 -300.56 35.95," +
			"0";
	private static final String input1_4 =
			"/execute in minecraft:the_nether run tp @s -116.91 81.50 -85.55 -389.51 29.71," +
			"1.9";
	private static final String input1_5 =
			"/execute in minecraft:the_nether run tp @s 358.32 80.00 -192.67 -157.99 39.66," +
			"0";

	@ParameterizedTest
	@CsvSource({ input1_1, input1_2, input1_3, input1_4, input1_5 })
	public void blindCalculationGivesExpectedResult_NotInRing(String f3c1, double expectedHighrollProbabilityAsPercentage) {
		blindTestBase(f3c1, expectedHighrollProbabilityAsPercentage, BlindResult.NOT_IN_RING);
	}

	private static final String input2_1 =
			"/execute in minecraft:the_nether run tp @s 155.00 73.00 157.60 -25.41 77.67," +
			"10.2";
	private static final String input2_2 =
			"/execute in minecraft:the_nether run tp @s -155.00 73.00 -155.00 -25.41 77.67," +
			"10.1";
	private static final String input2_3 =
			"/execute in minecraft:the_nether run tp @s 7.46 80.00 217.01 -197.69 37.85," +
			"10.2";
	private static final String input2_4 =
			"/execute in minecraft:the_nether run tp @s 462.88 80.00 -398.47 -292.43 37.24," +
			"7.5";
	private static final String input2_5 =
			"/execute in minecraft:the_nether run tp @s -636.77 80.00 -27.55 -254.74 43.32," +
			"7.4";

	@ParameterizedTest
	@CsvSource({ input2_1, input2_2, input2_3, input2_4, input2_5 })
	public void blindCalculationGivesExpectedResult_Excellent(String f3c1, double expectedHighrollProbabilityAsPercentage) {
		blindTestBase(f3c1, expectedHighrollProbabilityAsPercentage, BlindResult.EXCELLENT);
	}

	private static final String input3_1 =
			"/execute in minecraft:the_nether run tp @s 21.65 80.00 170.19 -362.57 33.70," +
			"6.0";
	private static final String input3_2 =
			"/execute in minecraft:the_nether run tp @s -40.88 80.00 -155.30 -540.73 27.98," +
			"4.0";

	@ParameterizedTest
	@CsvSource({ input3_1, input3_2 })
	public void blindCalculationGivesExpectedResult_Bad(String f3c1, double expectedHighrollProbabilityAsPercentage) {
		blindTestBase(f3c1, expectedHighrollProbabilityAsPercentage, BlindResult.BAD);
	}

	public void blindTestBase(String f3c1, double expectedHighrollProbabilityAsPercentage, Pair<Float, String> expectedEvaluation) {
		// Arrange
		IntegrationTestBuilder testBuilder = new IntegrationTestBuilder().withProSettings();

		// Act
		testBuilder.setClipboard(f3c1);

		// Assert
		BlindResult blindResult = testBuilder.dataState.blindResult().get();
		Assertions.assertEquals(expectedEvaluation, blindResult.evaluation());
		Assertions.assertEquals(expectedHighrollProbabilityAsPercentage / 100.0, blindResult.highrollProbability, 0.001);
	}

	private static final String input4_1 =
			"/execute in minecraft:the_nether run tp @s 155.00 73.00 157.60 -25.41 77.67," +
			"/setblock 10 73 0 minecraft:bone_block[axis=y]," +
			"10.2";

	@ParameterizedTest
	@CsvSource({ input4_1 })
	public void blindWithDivine_NotInDivineSector(String blindF3c, String fossilF3c, double expectedHighrollProbabilityWithoutDivineAsPercentage) {
		// Arrange
		IntegrationTestBuilder testBuilder = new IntegrationTestBuilder().withProSettings();

		// Act + Assert
		testBuilder.setClipboard(fossilF3c);
		testBuilder.setClipboard(blindF3c);

		BlindResult blindResult = testBuilder.dataState.blindResult().get();
		Assertions.assertEquals(BlindResult.BAD, blindResult.evaluation());
		Assertions.assertEquals(0, blindResult.highrollProbability, 0.001);

		testBuilder.clickRemoveFossilButton();

		blindResult = testBuilder.dataState.blindResult().get();
		Assertions.assertEquals(BlindResult.EXCELLENT, blindResult.evaluation());
		Assertions.assertEquals(expectedHighrollProbabilityWithoutDivineAsPercentage / 100.0, blindResult.highrollProbability, 0.001);
	}

	private static final String input5_1 =
			"/execute in minecraft:the_nether run tp @s 170.00 73.00 150.60 -25.41 77.67," +
			"/setblock 12 73 0 minecraft:bone_block[axis=y]," +
			"10.2, 50.0";

	@ParameterizedTest
	@CsvSource({ input5_1 })
	public void blindWithDivine_InDivineSector(String blindF3c, String fossilF3c, double expectedHighrollProbabilityWithoutDivineAsPercentage, double expectedHighrollProbabilityWithDivineAsPercentage) {
		// Arrange
		IntegrationTestBuilder testBuilder = new IntegrationTestBuilder().withProSettings();

		// Act + Assert
		testBuilder.setClipboard(fossilF3c);
		testBuilder.setClipboard(blindF3c);

		BlindResult blindResult = testBuilder.dataState.blindResult().get();
		Assertions.assertEquals(BlindResult.EXCELLENT, blindResult.evaluation());
		Assertions.assertEquals(expectedHighrollProbabilityWithDivineAsPercentage / 100.0, blindResult.highrollProbability, 0.001);

		testBuilder.clickRemoveFossilButton();

		blindResult = testBuilder.dataState.blindResult().get();
		Assertions.assertEquals(BlindResult.EXCELLENT, blindResult.evaluation());
		Assertions.assertEquals(expectedHighrollProbabilityWithoutDivineAsPercentage / 100.0, blindResult.highrollProbability, 0.001);
	}

}
