package ninjabrainbot.integrationtests;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpPrincipal;
import ninjabrainbot.Main;
import ninjabrainbot.io.api.ApiV1HttpHandler;
import ninjabrainbot.io.mcinstance.MinecraftInstance;
import ninjabrainbot.io.mcinstance.MinecraftWorldFile;
import ninjabrainbot.io.preferences.enums.McVersion;
import ninjabrainbot.model.datastate.common.ResultType;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ApiV1IntegrationTests {

	@Test
	void stronghold() throws IOException {
		// Arrange
		TestHttpExchange exchange = new TestHttpExchange("/api/v1/stronghold");
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		IntegrationTestBuilder builder = new IntegrationTestBuilder().withProSettings();
		builder.preferences.sigmaAlt.set(0.005f);
		ApiV1HttpHandler apiV1HttpHandler = new ApiV1HttpHandler(builder.dataState, builder.domainModel, builder.createInformationMessageList(), executorService);

		builder.setClipboard("/execute in minecraft:overworld run tp @s 1213.26 71.00 -318.63 -45.53 -31.39");
		builder.setClipboard("/execute in minecraft:overworld run tp @s 1212.65 69.00 -318.01 -45.53 -31.52");
		builder.inputStandardDeviationToggle();

		// Act
		apiV1HttpHandler.handle(exchange);

		// Assert
		Assertions.assertEquals(HttpURLConnection.HTTP_OK, exchange.getResponseCode());

		JSONObject root = new JSONObject(exchange.getResponseBodyAsString());

		JSONArray eyeThrows = root.getJSONArray("eyeThrows");
		Assertions.assertEquals(2, eyeThrows.length());

		JSONObject eyeThrow1 = eyeThrows.getJSONObject(0);
		Assertions.assertEquals(1213.26, eyeThrow1.getDouble("xInOverworld"));
		Assertions.assertEquals(-45.529992377906794, eyeThrow1.getDouble("angleWithoutCorrection"));
		Assertions.assertEquals(-318.63, eyeThrow1.getDouble("zInOverworld"));
		Assertions.assertEquals(-45.529992377906794, eyeThrow1.getDouble("angle"));
		Assertions.assertEquals(0, eyeThrow1.getDouble("correction"));
		Assertions.assertEquals(0, eyeThrow1.getDouble("correctionIncrements"));
		Assertions.assertEquals(-0.01593748756458524, eyeThrow1.getDouble("error"));
		Assertions.assertEquals("NORMAL", eyeThrow1.getString("type"));

		JSONObject eyeThrow2 = eyeThrows.getJSONObject(1);
		Assertions.assertEquals(1212.65, eyeThrow2.getDouble("xInOverworld"));
		Assertions.assertEquals(-45.529992377906794, eyeThrow2.getDouble("angleWithoutCorrection"));
		Assertions.assertEquals(-318.01, eyeThrow2.getDouble("zInOverworld"));
		Assertions.assertEquals(-45.529992377906794, eyeThrow2.getDouble("angle"));
		Assertions.assertEquals(0, eyeThrow2.getDouble("correction"));
		Assertions.assertEquals(0, eyeThrow2.getDouble("correctionIncrements"));
		Assertions.assertEquals(0.015504548661041895, eyeThrow2.getDouble("error"));
		Assertions.assertEquals("NORMAL_WITH_ALT_STD", eyeThrow2.getString("type"));

		Assertions.assertEquals("TRIANGULATION", root.getString("resultType"));

		JSONObject playerPosition = root.getJSONObject("playerPosition");
		Assertions.assertEquals(1212.65, playerPosition.getDouble("xInOverworld"));
		Assertions.assertTrue(playerPosition.getBoolean("isInOverworld"));
		Assertions.assertFalse(playerPosition.getBoolean("isInNether"));
		Assertions.assertEquals(-45.53, playerPosition.getDouble("horizontalAngle"));
		Assertions.assertEquals(-318.01, playerPosition.getDouble("zInOverworld"));

		JSONArray predictions = root.getJSONArray("predictions");
		Assertions.assertEquals(5, predictions.length());

		JSONObject prediction = predictions.getJSONObject(0);
		Assertions.assertEquals(1579.2960908582024, prediction.getDouble("overworldDistance"));
		Assertions.assertEquals(0.8287273194603837, prediction.getDouble("certainty"));
		Assertions.assertEquals(146, prediction.getInt("chunkX"));
		Assertions.assertEquals(49, prediction.getInt("chunkZ"));
	}

	@Test
	void allAdvancements() throws IOException {
		// Arrange
		TestHttpExchange exchange = new TestHttpExchange("/api/v1/all-advancements");
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		IntegrationTestBuilder builder = new IntegrationTestBuilder().withAllAdvancementsSettings();
		ApiV1HttpHandler apiV1HttpHandler = new ApiV1HttpHandler(builder.dataState, builder.domainModel, builder.createInformationMessageList(), executorService);

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

	@Test
	void blind() throws IOException {
		// Arrange
		TestHttpExchange exchange = new TestHttpExchange("/api/v1/blind");
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		IntegrationTestBuilder builder = new IntegrationTestBuilder();
		ApiV1HttpHandler apiV1HttpHandler = new ApiV1HttpHandler(builder.dataState, builder.domainModel, builder.createInformationMessageList(), executorService);

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

	@Test
	void divine() throws IOException {
		// Arrange
		TestHttpExchange exchange = new TestHttpExchange("/api/v1/divine");
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		IntegrationTestBuilder builder = new IntegrationTestBuilder();
		ApiV1HttpHandler apiV1HttpHandler = new ApiV1HttpHandler(builder.dataState, builder.domainModel, builder.createInformationMessageList(), executorService);

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

	@Test
	void boat() throws IOException {
		// Arrange
		TestHttpExchange exchange = new TestHttpExchange("/api/v1/boat");
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		IntegrationTestBuilder builder = new IntegrationTestBuilder().withBoatSettings();
		ApiV1HttpHandler apiV1HttpHandler = new ApiV1HttpHandler(builder.dataState, builder.domainModel, builder.createInformationMessageList(), executorService);

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

	@Test
	void informationMessages() throws IOException {
		// Arrange
		TestHttpExchange exchange = new TestHttpExchange("/api/v1/information-messages");
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		IntegrationTestBuilder builder = new IntegrationTestBuilder()
				.withProSettings()
				.withAllInformationMessagesSettings()
				.withMcVersionSetting(McVersion.PRE_119);
		ApiV1HttpHandler apiV1HttpHandler = new ApiV1HttpHandler(builder.dataState, builder.domainModel, builder.createInformationMessageList(), executorService);

		builder.setActiveMinecraftWorld(new MinecraftWorldFile(new MinecraftInstance("directory"), "worldName"), McVersion.POST_119);
		builder.setClipboard("/execute in minecraft:overworld run tp @s 2408 65.00 8 0 -31.87");
		builder.setClipboard("/execute in minecraft:overworld run tp @s 2408 65.00 9 0.02 -31.87");

		// Act
		apiV1HttpHandler.handle(exchange);

		// Assert
		Assertions.assertEquals(HttpURLConnection.HTTP_OK, exchange.getResponseCode());

		System.out.println(exchange.getResponseBodyAsString());
		String expectedResult =
				("{\n" +
				 "   \"informationMessages\":[\n" +
				 "      {\n" +
				 "         \"severity\":\"WARNING\",\n" +
				 "         \"type\":\"MC_VERSION\",\n" +
				 "         \"message\":\"Detected wrong Minecraft version, make sure the correct version is chosen in the settings.\"\n" +
				 "      },\n" +
				 "      {\n" +
				 "         \"severity\":\"WARNING\",\n" +
				 "         \"type\":\"PORTAL_LINKING\",\n" +
				 "         \"message\":\"You might not be able to nether travel into the stronghold due to portal linking.\"\n" +
				 "      },\n" +
				 "      {\n" +
				 "         \"severity\":\"WARNING\",\n" +
				 "         \"type\":\"MISMEASURE\",\n" +
				 "         \"message\":\"Detected unusually large errors, you probably mismeasured or your standard deviation is too low.\"\n" +
				 "      },\n" +
				 "      {\n" +
				 "         \"severity\":\"INFO\",\n" +
				 "         \"type\":\"COMBINED_CERTAINTY\",\n" +
				 "         \"message\":\"Nether coords (300, 3) have <span style=\\\"color:#00CE29;\\\">86.1%<\\/span> chance to hit the stronghold (it is between the top 2 offsets).\"\n" +
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

	@Test
	void version() throws IOException {
		// Arrange
		TestHttpExchange exchange = new TestHttpExchange("/api/v1/version");
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		IntegrationTestBuilder builder = new IntegrationTestBuilder().withBoatSettings();
		ApiV1HttpHandler apiV1HttpHandler = new ApiV1HttpHandler(builder.dataState, builder.domainModel, builder.createInformationMessageList(), executorService);

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

	@Test
	void ping() throws IOException {
		// Arrange
		TestHttpExchange exchange = new TestHttpExchange("/api/v1/ping");
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		IntegrationTestBuilder builder = new IntegrationTestBuilder().withBoatSettings();
		ApiV1HttpHandler apiV1HttpHandler = new ApiV1HttpHandler(builder.dataState, builder.domainModel, builder.createInformationMessageList(), executorService);

		// Act
		apiV1HttpHandler.handle(exchange);

		// Assert
//		System.out.println(exchange.getResponseBodyAsString());
		Assertions.assertEquals(HttpURLConnection.HTTP_OK, exchange.getResponseCode());

		String expectedResult = "Ninjabrain Bot HTTP server is active!";
		Assertions.assertEquals(expectedResult, exchange.getResponseBodyAsString());
	}

	@Test
	void subscribe_pushesNewDataWhenModified() throws IOException, InterruptedException {
		// Arrange
		TestHttpExchange exchange = new TestHttpExchange("/api/v1/boat/events");
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		IntegrationTestBuilder builder = new IntegrationTestBuilder().withBoatSettings();
		ApiV1HttpHandler apiV1HttpHandler = new ApiV1HttpHandler(builder.dataState, builder.domainModel, builder.createInformationMessageList(), executorService);

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
		ApiV1HttpHandler apiV1HttpHandler = new ApiV1HttpHandler(builder.dataState, builder.domainModel, builder.createInformationMessageList(), executorService);

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
		ApiV1HttpHandler apiV1HttpHandler = new ApiV1HttpHandler(builder.dataState, builder.domainModel, builder.createInformationMessageList(), executorService);

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

	private static class TestHttpExchange extends HttpExchange {

		private final String endpoint;
		public OutputStream outputStream = new ByteArrayOutputStream();
		private int responseCode = 0;

		public TestHttpExchange(String endpoint) {
			this.endpoint = endpoint;
		}

		public String getResponseBodyAsString() {
			return outputStream.toString();
		}

		@Override
		public Headers getRequestHeaders() {
			return null;
		}

		@Override
		public Headers getResponseHeaders() {
			return new Headers();
		}

		@Override
		public URI getRequestURI() {
			try {
				return new URI(endpoint);
			} catch (URISyntaxException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public String getRequestMethod() {
			return null;
		}

		@Override
		public HttpContext getHttpContext() {
			return null;
		}

		@Override
		public void close() {

		}

		@Override
		public InputStream getRequestBody() {
			return null;
		}

		@Override
		public OutputStream getResponseBody() {
			return outputStream;
		}

		@Override
		public void sendResponseHeaders(int rCode, long responseLength) throws IOException {
			responseCode = rCode;
		}

		@Override
		public InetSocketAddress getRemoteAddress() {
			return null;
		}

		@Override
		public int getResponseCode() {
			return responseCode;
		}

		@Override
		public InetSocketAddress getLocalAddress() {
			return null;
		}

		@Override
		public String getProtocol() {
			return null;
		}

		@Override
		public Object getAttribute(String name) {
			return null;
		}

		@Override
		public void setAttribute(String name, Object value) {

		}

		@Override
		public void setStreams(InputStream i, OutputStream o) {

		}

		@Override
		public HttpPrincipal getPrincipal() {
			return null;
		}
	}

}
