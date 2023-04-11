package ninjabrainbot.integrationtests;

import ninjabrainbot.gui.mainwindow.main.MainTextAreaTestAdapter;
import ninjabrainbot.util.TestUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class BoatCalculatedTravelTests {

	private static final String input1 =
			"/execute in minecraft:overworld run tp @s 1464.04 78.55 -2189.61 -95.63 18.16," +
			"/execute in minecraft:overworld run tp @s 1465.31 79.00 -2189.73 -282.21 -32.64," +
			"-1," +
			"-3,-117";
	private static final String input2 =
			"/execute in minecraft:overworld run tp @s -1442.62 70.00 1202.10 -40.78 37.05," +
			"/execute in minecraft:overworld run tp @s -1442.62 70.00 1202.10 -230.14 -32.40," +
			"2," +
			"-101,66";
	private static final String input3 =
			"/execute in minecraft:overworld run tp @s 3433.70 86.56 -4804.98 133.59 23.08," +
			"/execute in minecraft:overworld run tp @s 3433.70 86.56 -4804.98 231.84 -31.92," +
			"4," +
			"334,-395";

	@ParameterizedTest
	@CsvSource({ input1, input2, input3 })
	public void oneEyeBoatCalculatedTravelEyeSpies(String boatF3c, String enderEyeF3c, int subpixelCorrections, int strongholdChunkX, int strongholdChunkZ) {
		// Arrange
		IntegrationTestBuilder testBuilder = new IntegrationTestBuilder().withBoatSettings();
		MainTextAreaTestAdapter mainTextAreaTestAdapter = testBuilder.createMainTextArea();

		// Act
		testBuilder.triggerHotkey(testBuilder.preferences.hotkeyBoat);
		testBuilder.setClipboard(boatF3c);
		testBuilder.setClipboard(enderEyeF3c);
		testBuilder.inputSubpixelCorrections(subpixelCorrections);

		// Assert
		TestUtils.awaitSwingEvents();
		mainTextAreaTestAdapter.assertDetailedTriangulationTopPredictionIsEqualTo(strongholdChunkX, strongholdChunkZ);
		mainTextAreaTestAdapter.assertDetailedTriangulationTopNetherCoordsIsEqualTo(2 * strongholdChunkX, 2 * strongholdChunkZ);
	}

}
