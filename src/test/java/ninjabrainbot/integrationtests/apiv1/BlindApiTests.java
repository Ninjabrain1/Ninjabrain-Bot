package ninjabrainbot.integrationtests.apiv1;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ninjabrainbot.integrationtests.IntegrationTestBuilder;
import ninjabrainbot.io.api.ApiV1HttpHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BlindApiTests {

	@Test
	void blind() throws IOException {
		// Arrange
		TestHttpExchange exchange = new TestHttpExchange("/api/v1/blind");
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		IntegrationTestBuilder builder = new IntegrationTestBuilder();
		ApiV1HttpHandler apiV1HttpHandler = new ApiV1HttpHandler(builder.dataState, builder.domainModel, builder.createInformationMessageList(), builder.actionExecutor, executorService, null, null, builder.preferences);

		builder.setClipboard("/execute in minecraft:the_nether run tp @s -217.82 85.00 6.88 -133.68 81.14");

		// Act
		apiV1HttpHandler.handle(exchange);

		// Assert
		Assertions.assertEquals(HttpURLConnection.HTTP_OK, exchange.getResponseCode());

//		System.out.println(exchange.getResponseBodyAsString());
		String expectedResult =
				("{" +
				 "    \"isBlindModeEnabled\": true," +
				 "    \"hasDivine\": false," +
				 "    \"blindResult\": {" +
				 "        \"evaluation\": \"HIGHROLL_GOOD\"," +
				 "        \"xInNether\": -217.82," +
				 "        \"improveDistance\": 8.071372233935255," +
				 "        \"zInNether\": 6.88," +
				 "        \"averageDistance\": 1086.9952915836398," +
				 "        \"improveDirection\": 1.5392211114431098," +
				 "        \"highrollProbability\": 0.10072320582001268," +
				 "        \"highrollThreshold\": 400" +
				 "    }" +
				 "}").replaceAll("\\s+", "");
		Assertions.assertEquals(expectedResult, exchange.getResponseBodyAsString());
	}

}

