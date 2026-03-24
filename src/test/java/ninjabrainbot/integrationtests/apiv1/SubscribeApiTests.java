package ninjabrainbot.integrationtests.apiv1;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import ninjabrainbot.integrationtests.IntegrationTestBuilder;
import ninjabrainbot.io.api.ApiV1HttpHandler;
import ninjabrainbot.model.datastate.common.ResultType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SubscribeApiTests {

	@Test
	void subscribe_pushesNewDataWhenModified() throws IOException, InterruptedException {
		// Arrange
		TestHttpExchange exchange = new TestHttpExchange("/api/v1/boat/events");
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		IntegrationTestBuilder builder = new IntegrationTestBuilder().withBoatSettings();
		ApiV1HttpHandler apiV1HttpHandler = new ApiV1HttpHandler(builder.dataState, builder.domainModel, builder.createInformationMessageList(), builder.actionExecutor, executorService);

		// Act 1
		apiV1HttpHandler.handle(exchange);

		// Assert 1
		Assertions.assertEquals(HttpURLConnection.HTTP_OK, exchange.getResponseCode());
		Assertions.assertEquals("data: {\"boatState\":\"NONE\"}\n\n", exchange.getResponseBodyAsString());

		// Act 2
		builder.triggerHotkey(builder.preferences.hotkeyBoat);
		executorService.shutdown();
		executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

		// Assert 2
		String expectedResult = "data: {\"boatState\":\"NONE\"}\n" +
								"\n" +
								"data: {\"boatState\":\"MEASURING\"}\n\n";
		Assertions.assertEquals(expectedResult, exchange.getResponseBodyAsString());
	}

	@Test
	void subscribe_doesNotPushNewDataWhenIrrelevantDataIsModified() throws IOException, InterruptedException {
		// Arrange
		TestHttpExchange exchange = new TestHttpExchange("/api/v1/boat/events");
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		IntegrationTestBuilder builder = new IntegrationTestBuilder().withBoatSettings();
		ApiV1HttpHandler apiV1HttpHandler = new ApiV1HttpHandler(builder.dataState, builder.domainModel, builder.createInformationMessageList(), builder.actionExecutor, executorService);

		// Act 1
		apiV1HttpHandler.handle(exchange);

		// Assert 1
		Assertions.assertEquals(exchange.getResponseCode(), HttpURLConnection.HTTP_OK);
		Assertions.assertEquals("data: {\"boatState\":\"NONE\"}\n\n", exchange.getResponseBodyAsString());
		Assertions.assertEquals(ResultType.NONE, builder.dataState.resultType().get());

		// Act 2
		builder.setClipboard("/execute in minecraft:overworld run tp @s -14.76 65.00 46.01 193.84 -31.23");
		executorService.shutdown();
		executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

		// Assert 2
		Assertions.assertEquals("data: {\"boatState\":\"NONE\"}\n\n", exchange.getResponseBodyAsString());
		Assertions.assertEquals(ResultType.TRIANGULATION, builder.dataState.resultType().get());
	}

	/**
	 * Important test because information messages are updated after domain model, so need to make sure the changes happen before API updates.
	 */
	@Test
	void subscribeInformationMessage_includesAllMessages() throws IOException, InterruptedException {
		// Arrange
		TestHttpExchange exchange = new TestHttpExchange("/api/v1/information-messages/events");
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		IntegrationTestBuilder builder = new IntegrationTestBuilder()
				.withProSettings()
				.withAllInformationMessagesSettings();
		ApiV1HttpHandler apiV1HttpHandler = new ApiV1HttpHandler(builder.dataState, builder.domainModel, builder.createInformationMessageList(), builder.actionExecutor, executorService);

		// Act 1
		apiV1HttpHandler.handle(exchange);

		// Assert 1
		Assertions.assertEquals(HttpURLConnection.HTTP_OK, exchange.getResponseCode());
		Assertions.assertEquals("data: {\"informationMessages\":[]}\n\n", exchange.getResponseBodyAsString());

		// Act 2
		builder.setClipboard("/execute in minecraft:overworld run tp @s 2408 65.00 9 0.02 -31.87");
		executorService.shutdown();
		executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

		// Assert 2
		String expectedResult =
				("data: {\"informationMessages\":[]}\n" +
				 "\n" +
				 "data: {\n" +
				 "   \"informationMessages\":[\n" +
				 "      {\n" +
				 "         \"severity\":\"WARNING\",\n" +
				 "         \"type\":\"MISMEASURE\",\n" +
				 "         \"message\":\"Detected unusually large errors, you probably mismeasured or your standard deviation is too low.\"\n" +
				 "      },\n" +
				 "      {\n" +
				 "         \"severity\":\"WARNING\",\n" +
				 "         \"type\":\"PORTAL_LINKING\",\n" +
				 "         \"message\":\"You might not be able to nether travel into the stronghold due to portal linking.\"\n" +
				 "      },\n" +
				 "      {\n" +
				 "         \"severity\":\"INFO\",\n" +
				 "         \"type\":\"COMBINED_CERTAINTY\",\n" +
				 "         \"message\":\"Nether coords (300, 3) have <span style=\\\"color:#00CE29;\\\">86.0%<\\/span> chance to hit the stronghold (it is between the top 2 offsets).\"\n" +
				 "      },\n" +
				 "      {\n" +
				 "         \"severity\":\"INFO\",\n" +
				 "         \"type\":\"NEXT_THROW_DIRECTION\",\n" +
				 "         \"message\":\"Go left 1 blocks, or right 1 blocks, for ~95% certainty after next measurement.\"\n" +
				 "      }\n" +
				 "   ]\n" +
				 "}").replaceAll("\\s+", "");
		Assertions.assertEquals(expectedResult, exchange.getResponseBodyAsString().replaceAll("\\s+", ""));
	}

}

