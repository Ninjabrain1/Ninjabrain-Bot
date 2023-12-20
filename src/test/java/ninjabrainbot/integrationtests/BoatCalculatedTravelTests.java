package ninjabrainbot.integrationtests;

import ninjabrainbot.gui.mainwindow.BoatIcon;
import ninjabrainbot.gui.mainwindow.main.MainTextAreaTestAdapter;
import ninjabrainbot.model.datastate.highprecision.BoatState;
import ninjabrainbot.util.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class BoatCalculatedTravelTests {

	private static final String input1 =
			"/execute in minecraft:overworld run tp @s 1274.04 92.55 1064.56 -78.75 32.82," +
			"/execute in minecraft:overworld run tp @s 1275.31 93.00 1064.81 -146.11 -32.13," +
			"7," +
			"133,-14";
	private static final String input2 =
			"/execute in minecraft:overworld run tp @s -1380.20 80.00 1138.90 -143.16 28.53," +
			"/execute in minecraft:overworld run tp @s -1376.84 80.00 1132.87 -60.46 -31.73," +
			"5," +
			"-43,95";
	private static final String input3 =
			"/execute in minecraft:overworld run tp @s 3430.24 62.07 -4805.87 104.06 74.04," +
			"/execute in minecraft:overworld run tp @s 3430.24 63.09 -4805.87 93.92 -31.81," +
			"23," +
			"111,-308";

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

		double angleError = testBuilder.dataState.calculatorResult().get().getBestPrediction().getAngleError(testBuilder.dataState.getThrowList().get(0));
		final double toRad = Math.PI / 180.0;
		double smallestPossibleCorrection = Math.atan(2 * Math.tan(15 * toRad) / testBuilder.preferences.resolutionHeight.get()) / Math.cos(-31 * toRad) / toRad;
		Assertions.assertTrue(Math.abs(angleError) < smallestPossibleCorrection);
	}

}
