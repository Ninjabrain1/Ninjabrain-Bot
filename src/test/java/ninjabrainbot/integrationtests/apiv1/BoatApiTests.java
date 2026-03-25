package ninjabrainbot.integrationtests.apiv1;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ninjabrainbot.integrationtests.IntegrationTestBuilder;
import ninjabrainbot.io.api.ApiV1HttpHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BoatApiTests {

	@Test
	void boat() throws IOException {
		// Arrange
		TestHttpExchange exchange = new TestHttpExchange("/api/v1/boat");
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		IntegrationTestBuilder builder = new IntegrationTestBuilder().withBoatSettings();
		ApiV1HttpHandler apiV1HttpHandler = new ApiV1HttpHandler(builder.dataState, builder.domainModel, builder.createInformationMessageList(), builder.actionExecutor, executorService, null, null);

		builder.triggerHotkey(builder.preferences.hotkeyBoat);
		builder.setClipboard("/execute in minecraft:overworld run tp @s 1274.04 92.55 1064.56 -77.34375 32.82");

		// Act
		apiV1HttpHandler.handle(exchange);

		// Assert
		Assertions.assertEquals(HttpURLConnection.HTTP_OK, exchange.getResponseCode());

//		System.out.println(exchange.getResponseBodyAsString());
		String expectedResult =
				("{" +
				 "    \"boatAngle\": -77.34375," +
				 "    \"boatState\": \"VALID\"" +
				 "}").replaceAll("\\s+", "");
		Assertions.assertEquals(expectedResult, exchange.getResponseBodyAsString());
	}

}

