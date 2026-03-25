package ninjabrainbot.integrationtests.apiv1;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ninjabrainbot.Main;
import ninjabrainbot.integrationtests.IntegrationTestBuilder;
import ninjabrainbot.io.api.ApiV1HttpHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class VersionApiTests {

	@Test
	void version() throws IOException {
		// Arrange
		TestHttpExchange exchange = new TestHttpExchange("/api/v1/version");
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		IntegrationTestBuilder builder = new IntegrationTestBuilder().withBoatSettings();
		ApiV1HttpHandler apiV1HttpHandler = new ApiV1HttpHandler(builder.dataState, builder.domainModel, builder.createInformationMessageList(), builder.actionExecutor, executorService, null, null, builder.preferences);

		// Act
		apiV1HttpHandler.handle(exchange);

		// Assert
		Assertions.assertEquals(HttpURLConnection.HTTP_OK, exchange.getResponseCode());

//		System.out.println(exchange.getResponseBodyAsString());
		String expectedResult =
				("{" +
				 "    \"version\": \"" + Main.VERSION + "\"" +
				 "}").replaceAll("\\s+", "");
		Assertions.assertEquals(expectedResult, exchange.getResponseBodyAsString());
	}

}

