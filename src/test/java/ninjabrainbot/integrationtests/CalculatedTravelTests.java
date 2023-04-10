package ninjabrainbot.integrationtests;

import ninjabrainbot.gui.mainwindow.main.MainTextAreaTestAdapter;
import ninjabrainbot.util.TestUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class CalculatedTravelTests {

	private static final String input1 =
			"/execute in minecraft:overworld run tp @s 199.50 71.00 -63.50 60.23 -31.39," +
			"/execute in minecraft:overworld run tp @s 196.49 69.00 -69.35 59.99 -31.52," +
			"-78,47";
	private static final String input2 =
			"/execute in minecraft:overworld run tp @s 809.90 69.00 -2091.99 91.72 -31.27," +
			"/execute in minecraft:overworld run tp @s 812.25 63.09 -2100.82 91.48 -31.60," +
			"-74,-135";
	private static final String input3 =
			"/execute in minecraft:overworld run tp @s 4798.40 63.00 -307.89 174.14 -31.39," +
			"/execute in minecraft:overworld run tp @s 4786.13 64.00 -309.25 174.56 -31.60," +
			"289,-121";

	@ParameterizedTest
	@CsvSource({ input1, input2, input3 })
	public void twoEyeCalculatedTravelEyeSpies(String f3c1, String f3c2, int strongholdChunkX, int strongholdChunkZ) {
		// Arrange
		IntegrationTestBuilder testBuilder = new IntegrationTestBuilder().withProSettings();
		MainTextAreaTestAdapter mainTextAreaTestAdapter = testBuilder.createMainTextArea();

		// Act
		testBuilder.setClipboard(f3c1);
		testBuilder.setClipboard(f3c2);

		// Assert
		TestUtils.awaitSwingEvents();
		mainTextAreaTestAdapter.assertDetailedTriangulationTopPredictionIsEqualTo(strongholdChunkX, strongholdChunkZ);
		mainTextAreaTestAdapter.assertDetailedTriangulationTopNetherCoordsIsEqualTo(2 * strongholdChunkX, 2 * strongholdChunkZ);
	}

	private static final String input4 =
			"/execute in minecraft:overworld run tp @s 199.50 71.00 -63.50 60.23 -31.39," +
			"/execute in minecraft:overworld run tp @s 196.49 69.00 -69.35 59.99 -31.52," +
			"-78,47";
	private static final String input5 =
			"/execute in minecraft:overworld run tp @s 809.90 69.00 -2091.99 91.72 -31.27," +
			"/execute in minecraft:overworld run tp @s 812.25 63.09 -2100.82 91.48 -31.60," +
			"-74,-135";
	private static final String input6 =
			"/execute in minecraft:overworld run tp @s 4798.40 63.00 -307.89 174.14 -31.39," +
			"/execute in minecraft:overworld run tp @s 4786.13 64.00 -309.25 174.56 -31.60," +
			"289,-121";

	@ParameterizedTest
	@CsvSource({ input1, input2, input3 })
	public void twoEyeCalculatedTravelFailsWhenMoving(String f3c1, String f3c2, int strongholdChunkX, int strongholdChunkZ) {
		// Arrange
		IntegrationTestBuilder testBuilder = new IntegrationTestBuilder().withProSettings();
		MainTextAreaTestAdapter mainTextAreaTestAdapter = testBuilder.createMainTextArea();

		// Act
		testBuilder.setClipboard(f3c1);
		testBuilder.setClipboard(f3c2);

		// Assert
		TestUtils.awaitSwingEvents();
		mainTextAreaTestAdapter.assertDetailedTriangulationTopPredictionIsEqualTo(strongholdChunkX, strongholdChunkZ);
		mainTextAreaTestAdapter.assertDetailedTriangulationTopNetherCoordsIsEqualTo(2 * strongholdChunkX, 2 * strongholdChunkZ);
	}

}
