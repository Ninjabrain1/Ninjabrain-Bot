package ninjabrainbot.integrationtests;

import java.util.Locale;

import javax.swing.SwingUtilities;

import ninjabrainbot.gui.frames.CalibrationDialog;
import ninjabrainbot.model.datastate.calibrator.CalibratorFactory;
import ninjabrainbot.util.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CalibrationIntegrationTests {

	@Test
	public void canOpenDialog() {
		// Arrange
		IntegrationTestBuilder integrationTestBuilder = new IntegrationTestBuilder();
		Locale.setDefault(Locale.US);
		CalibratorFactory calibratorFactory = integrationTestBuilder.createCalibratorFactory();

		// Act + Assert
		try {
			SwingUtilities.invokeAndWait(() -> {
				CalibrationDialog dialog = new CalibrationDialog(TestUtils.createStyleManager(), integrationTestBuilder.preferences, calibratorFactory, integrationTestBuilder.actionExecutor, null, false);
			});
		} catch (Exception e) {
			Assertions.fail(e);
		}
	}

	@Test
	public void canOpenDialog_manualCalibration() {
		// Arrange
		IntegrationTestBuilder integrationTestBuilder = new IntegrationTestBuilder();
		Locale.setDefault(Locale.US);
		CalibratorFactory calibratorFactory = integrationTestBuilder.createCalibratorFactory();

		// Act + Assert
		try {
			SwingUtilities.invokeAndWait(() -> {
				CalibrationDialog dialog = new CalibrationDialog(TestUtils.createStyleManager(), integrationTestBuilder.preferences, calibratorFactory, integrationTestBuilder.actionExecutor, null, true);
			});
		} catch (Exception e) {
			Assertions.fail(e);
		}
	}

}
