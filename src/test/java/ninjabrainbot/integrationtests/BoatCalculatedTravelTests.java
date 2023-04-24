package ninjabrainbot.integrationtests;

import ninjabrainbot.model.datastate.highprecision.BoatState;
import ninjabrainbot.gui.mainwindow.BoatIcon;
import ninjabrainbot.gui.mainwindow.main.MainTextAreaTestAdapter;
import ninjabrainbot.util.TestUtils;
import org.junit.jupiter.api.Assertions;
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
		BoatIcon boatIcon = testBuilder.createBoatIcon();

		// Act + Assert
		TestUtils.awaitSwingEvents();
		Assertions.assertTrue(boatIcon.isVisible());
		Assertions.assertEquals(boatIcon.getIcon(), BoatIcon.getBoatIcon(BoatState.NONE));

		testBuilder.triggerHotkey(testBuilder.preferences.hotkeyBoat);
		TestUtils.awaitSwingEvents();
		Assertions.assertEquals(boatIcon.getIcon(), BoatIcon.getBoatIcon(BoatState.MEASURING));

		testBuilder.setClipboard(boatF3c);
		TestUtils.awaitSwingEvents();
		Assertions.assertEquals(boatIcon.getIcon(), BoatIcon.getBoatIcon(BoatState.VALID));

		testBuilder.setClipboard(enderEyeF3c);
		testBuilder.inputSubpixelCorrections(subpixelCorrections);

		// Assert
		TestUtils.awaitSwingEvents();
		Assertions.assertEquals(boatIcon.getIcon(), BoatIcon.getBoatIcon(BoatState.VALID));
		mainTextAreaTestAdapter.assertDetailedTriangulationTopPredictionIsEqualTo(strongholdChunkX, strongholdChunkZ);
		mainTextAreaTestAdapter.assertDetailedTriangulationTopNetherCoordsIsEqualTo(2 * strongholdChunkX, 2 * strongholdChunkZ);

		var angleError = testBuilder.dataState.calculatorResult().get().getBestPrediction().getAngleError(testBuilder.dataState.getThrowList().get(0));
		final double toRad = Math.PI / 180.0;
		var smallestPossibleCorrection = Math.atan(2 * Math.tan(15 * toRad) / testBuilder.preferences.resolutionHeight.get()) / Math.cos(-31 * toRad) / toRad;
		Assertions.assertTrue(Math.abs(angleError) < smallestPossibleCorrection);
	}

}
