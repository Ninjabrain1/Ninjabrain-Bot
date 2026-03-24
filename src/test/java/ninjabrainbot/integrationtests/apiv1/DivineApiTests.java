package ninjabrainbot.integrationtests.apiv1;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ninjabrainbot.integrationtests.IntegrationTestBuilder;
import ninjabrainbot.io.api.ApiV1HttpHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DivineApiTests {

	@Test
	void divine() throws IOException {
		// Arrange
		TestHttpExchange exchange = new TestHttpExchange("/api/v1/divine");
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		IntegrationTestBuilder builder = new IntegrationTestBuilder();
		ApiV1HttpHandler apiV1HttpHandler = new ApiV1HttpHandler(builder.dataState, builder.domainModel, builder.createInformationMessageList(), builder.actionExecutor, executorService);

		builder.setClipboard("/setblock 5 73 0 minecraft:bone_block[axis=y]");

		// Act
		apiV1HttpHandler.handle(exchange);

		// Assert
		Assertions.assertEquals(HttpURLConnection.HTTP_OK, exchange.getResponseCode());

//		System.out.println(exchange.getResponseBodyAsString());
		String expectedResult =
				("{" +
				 "    \"isDivineModeEnabled\": true," +
				 "    \"divineResult\": {" +
				 "        \"formattedSafeCoords\": \"(-142, 213), (-113, -230), (255, 17)\"," +
				 "        \"formattedHighrollCoords\": \"(-106, 158), (-84, -170), (190, 12)\"," +
				 "        \"fossilXCoordinate\": 5" +
				 "    }" +
				 "}").replaceAll("\\s+", "");
		Assertions.assertEquals(expectedResult, exchange.getResponseBodyAsString().replaceAll("\\s+", ""));
	}

}

