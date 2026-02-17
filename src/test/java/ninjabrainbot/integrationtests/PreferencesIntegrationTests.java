package ninjabrainbot.integrationtests;

import java.util.Random;

import ninjabrainbot.gui.mainwindow.eyethrows.EnderEyePanelTestAdapter;
import ninjabrainbot.util.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class PreferencesIntegrationTests {

	Random random;

	@BeforeEach
	void setUp() {
		random = new Random(15478);
	}

	@ParameterizedTest
	@ValueSource(floats = { 0.1f, -0.231f, 1, -1, -0.001f, 0.014f })
	void crosshairCorrectionIntegrationTest(float crosshairCorrection) {
		// Arrange
		IntegrationTestBuilder integrationTestBuilder = new IntegrationTestBuilder();
		integrationTestBuilder.preferences.crosshairCorrection.set(crosshairCorrection);
		EnderEyePanelTestAdapter enderEyePanelTestAdapter = integrationTestBuilder.createEnderEyePanel();
		float horizontalAngle = crosshairCorrection * 1522545 % 360;
		if (horizontalAngle > 180)
			horizontalAngle -= 360;
		if (horizontalAngle < -180)
			horizontalAngle += 360;
		String f3c = String.format("/execute in minecraft:overworld run tp @s 0 64 0 %s -31", horizontalAngle);

		// Act
		integrationTestBuilder.setClipboard(f3c);

		// Assert
		TestUtils.awaitSwingEvents();
		double packetRoundingCorrectedHorizontalAngle = horizontalAngle - 0.000824 * Math.sin((horizontalAngle + 45) * Math.PI / 180.0);
		Assertions.assertEquals(Math.round((horizontalAngle + crosshairCorrection) * 100f) / 100f, enderEyePanelTestAdapter.getPanel(0).getAngle(), 1e-4);
		Assertions.assertEquals(packetRoundingCorrectedHorizontalAngle + crosshairCorrection, integrationTestBuilder.dataState.getThrowList().get(0).horizontalAngle(), 1e-4);
	}
}
