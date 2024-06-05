package ninjabrainbot.integrationtests;

import ninjabrainbot.io.api.queries.StrongholdQuery;
import ninjabrainbot.model.datastate.common.DetailedPlayerPosition;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class JsonIntegrationTests {

	@Disabled("Just for testing how json looks")
	@Test
	void testJson_triangulation() {
		// Arrange
		IntegrationTestBuilder testBuilder = new IntegrationTestBuilder().withProSettings();

		testBuilder.inputDetailedPlayerPosition(new DetailedPlayerPosition(0, 80, 0, 161.9, -31, false));
		testBuilder.inputDetailedPlayerPosition(new DetailedPlayerPosition(20, 80, 0, 161.2, -31, false));
		testBuilder.inputSubpixelCorrections(2);
		testBuilder.inputDetailedPlayerPosition(new DetailedPlayerPosition(-60, 80, -150, 12, -31, true));
		testBuilder.inputStandardDeviationToggle();

		StrongholdQuery jsonConverter = new StrongholdQuery(true);

		long t0 = System.nanoTime();

		// Act
		String json = jsonConverter.get(testBuilder.dataState);

		// Assert
		long deltaT = System.nanoTime() - t0;
		System.out.println("Elapsed time [seconds]: " + deltaT * 1e-9);
		System.out.println(json);
	}

	@Disabled("Just for testing how json looks")
	@Test
	void testJson_failed() {
		// Arrange
		IntegrationTestBuilder testBuilder = new IntegrationTestBuilder().withProSettings();

		testBuilder.inputDetailedPlayerPosition(new DetailedPlayerPosition(0, 80, 0, 161.9, -31, false));
		testBuilder.inputDetailedPlayerPosition(new DetailedPlayerPosition(20, 80, 0, -30, -31, false));

		StrongholdQuery jsonConverter = new StrongholdQuery(true);

		long t0 = System.nanoTime();

		// Act
		String json = jsonConverter.get(testBuilder.dataState);

		// Assert
		long deltaT = System.nanoTime() - t0;
		System.out.println("Elapsed time [seconds]: " + deltaT * 1e-9);
		System.out.println(json);
	}

	@Disabled("Just for testing how json looks")
	@Test
	void testJson_empty() {
		// Arrange
		IntegrationTestBuilder testBuilder = new IntegrationTestBuilder().withProSettings();

		StrongholdQuery jsonConverter = new StrongholdQuery(true);

		long t0 = System.nanoTime();

		// Act
		String json = jsonConverter.get(testBuilder.dataState);

		// Assert
		long deltaT = System.nanoTime() - t0;
		System.out.println("Elapsed time [seconds]: " + deltaT * 1e-9);
		System.out.println(json);
	}

}
