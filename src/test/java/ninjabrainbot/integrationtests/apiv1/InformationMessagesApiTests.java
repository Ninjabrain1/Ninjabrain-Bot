package ninjabrainbot.integrationtests.apiv1;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ninjabrainbot.integrationtests.IntegrationTestBuilder;
import ninjabrainbot.io.api.ApiV1HttpHandler;
import ninjabrainbot.io.mcinstance.MinecraftInstance;
import ninjabrainbot.io.mcinstance.MinecraftWorldFile;
import ninjabrainbot.io.preferences.enums.McVersion;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class InformationMessagesApiTests {

	@Test
	void informationMessages() throws IOException {
		// Arrange
		TestHttpExchange exchange = new TestHttpExchange("/api/v1/information-messages");
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		IntegrationTestBuilder builder = new IntegrationTestBuilder()
				.withProSettings()
				.withAllInformationMessagesSettings()
				.withMcVersionSetting(McVersion.PRE_119);
		ApiV1HttpHandler apiV1HttpHandler = new ApiV1HttpHandler(builder.dataState, builder.domainModel, builder.createInformationMessageList(), builder.actionExecutor, executorService);

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

}

