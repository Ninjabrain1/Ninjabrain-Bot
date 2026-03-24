package ninjabrainbot.integrationtests.apiv1;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ninjabrainbot.integrationtests.IntegrationTestBuilder;
import ninjabrainbot.io.api.ApiV1HttpHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PingApiTests {

	@Test
	void ping() throws IOException {
		// Arrange
		TestHttpExchange exchange = new TestHttpExchange("/api/v1/ping");
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		IntegrationTestBuilder builder = new IntegrationTestBuilder().withBoatSettings();
		ApiV1HttpHandler apiV1HttpHandler = new ApiV1HttpHandler(builder.dataState, builder.domainModel, builder.createInformationMessageList(), builder.actionExecutor, executorService);

		// Act
		apiV1HttpHandler.handle(exchange);

		// Assert
//		System.out.println(exchange.getResponseBodyAsString());
		Assertions.assertEquals(HttpURLConnection.HTTP_OK, exchange.getResponseCode());

		String expectedResult = "Ninjabrain Bot HTTP server is active!";
		Assertions.assertEquals(expectedResult, exchange.getResponseBodyAsString());
	}

}

