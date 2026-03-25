package ninjabrainbot.integrationtests.apiv1;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ninjabrainbot.integrationtests.IntegrationTestBuilder;
import ninjabrainbot.io.api.ApiV1HttpHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AllAdvancementsApiTests {

	@Test
	void allAdvancements() throws IOException {
		// Arrange
		TestHttpExchange exchange = new TestHttpExchange("/api/v1/all-advancements");
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		IntegrationTestBuilder builder = new IntegrationTestBuilder().withAllAdvancementsSettings();
		ApiV1HttpHandler apiV1HttpHandler = new ApiV1HttpHandler(builder.dataState, builder.domainModel, builder.createInformationMessageList(), builder.actionExecutor, executorService, null, null);

		builder.setClipboard("/execute in minecraft:overworld run tp @s 1477.68 70.00 -211.29 -103.76 -31.31");
		builder.enterNewWorld();
		builder.enterEnd();
		builder.setClipboard("/execute in minecraft:overworld run tp @s -214.50 71.00 185.50 0.00 0.00");
		builder.setClipboard("/execute in minecraft:overworld run tp @s 3357.69 55.29 3693.87 -48.99 50.06");

		// Act
		apiV1HttpHandler.handle(exchange);

		// Assert
		Assertions.assertEquals(HttpURLConnection.HTTP_OK, exchange.getResponseCode());

		System.out.println(exchange.getResponseBodyAsString());
		String expectedResult =
				("{\n" +
				 "   \"generalLocation\":{\n" +
				 "      \n" +
				 "   },\n" +
				 "   \"spawn\":{\n" +
				 "      \"overworldDistance\":5007,\n" +
				 "      \"xInOverworld\":-215,\n" +
				 "      \"zInOverworld\":185,\n" +
				 "      \"travelAngle\":134.48365647425098\n" +
				 "   },\n" +
				 "   \"cityQuery\":{\n" +
				 "      \n" +
				 "   },\n" +
				 "   \"monument\":{\n" +
				 "      \"overworldDistance\":1,\n" +
				 "      \"xInOverworld\":3357,\n" +
				 "      \"zInOverworld\":3693,\n" +
				 "      \"travelAngle\":141.5819446551723\n" +
				 "   },\n" +
				 "   \"shulkerTransport\":{\n" +
				 "      \n" +
				 "   },\n" +
				 "   \"stronghold\":{\n" +
				 "      \"overworldDistance\":4247,\n" +
				 "      \"xInOverworld\":2212,\n" +
				 "      \"zInOverworld\":-396,\n" +
				 "      \"travelAngle\":164.35091545590393\n" +
				 "   },\n" +
				 "   \"deepDark\":{\n" +
				 "      \n" +
				 "   },\n" +
				 "   \"isAllAdvancementsModeEnabled\":true,\n" +
				 "   \"outpost\":{\n" +
				 "      \n" +
				 "   }\n" +
				 "}").replaceAll("\\s+", "");
		Assertions.assertEquals(expectedResult, exchange.getResponseBodyAsString());
	}

}

