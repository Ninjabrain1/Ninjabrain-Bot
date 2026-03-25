package ninjabrainbot.integrationtests.apiv1;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ninjabrainbot.integrationtests.IntegrationTestBuilder;
import ninjabrainbot.io.api.ApiV1HttpHandler;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StrongholdApiTests {

	@Test
	void stronghold() throws IOException {
		// Arrange
		TestHttpExchange exchange = new TestHttpExchange("/api/v1/stronghold");
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		IntegrationTestBuilder builder = new IntegrationTestBuilder().withProSettings();
		builder.preferences.sigmaAlt.set(0.005f);
		ApiV1HttpHandler apiV1HttpHandler = new ApiV1HttpHandler(builder.dataState, builder.domainModel, builder.createInformationMessageList(), builder.actionExecutor, executorService, null, null);

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

}

