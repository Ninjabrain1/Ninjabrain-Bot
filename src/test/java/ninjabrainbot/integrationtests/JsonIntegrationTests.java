package ninjabrainbot.integrationtests;

import ninjabrainbot.io.json.JsonConverter;
import ninjabrainbot.model.datastate.common.DetailedPlayerPosition;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class JsonIntegrationTests {

	@Disabled("Just for testing how json looks")
	@Test
	void dataComponentsOnlySendsOneEventOnReset() {
		// Arrange
		IntegrationTestBuilder testBuilder = new IntegrationTestBuilder().withProSettings();

		testBuilder.inputDetailedPlayerPosition(new DetailedPlayerPosition(0, 80, 0, 12, -31, false));
		testBuilder.inputDetailedPlayerPosition(new DetailedPlayerPosition(0, 80, 1000, 12, -31, true));

		JsonConverter jsonConverter = new JsonConverter(true);

		long t0 = System.nanoTime();

		// Act
		String json = jsonConverter.convert(testBuilder.dataState);

		// Assert
		long deltaT = System.nanoTime() - t0;
		System.out.println("Elapsed time [seconds]: " + deltaT * 1e-9);
		System.out.println(json);
	}

}
