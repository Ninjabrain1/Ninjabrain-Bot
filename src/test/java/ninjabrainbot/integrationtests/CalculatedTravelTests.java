package ninjabrainbot.integrationtests;

import ninjabrainbot.data.DataStateHandler;
import ninjabrainbot.data.IDataState;
import ninjabrainbot.data.IDataStateHandler;
import ninjabrainbot.gui.mainwindow.main.MainTextArea;
import ninjabrainbot.gui.mainwindow.main.MainTextAreaTestAdapter;
import ninjabrainbot.io.IClipboardProvider;
import ninjabrainbot.io.preferences.MultipleChoicePreferenceDataTypes;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.io.preferences.UnsavedPreferences;
import ninjabrainbot.util.MockedClipboardReader;
import ninjabrainbot.util.MockedInstanceProvider;
import ninjabrainbot.util.TestF3C;
import ninjabrainbot.util.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class CalculatedTravelTests {

	private static final String input1 =
			"/execute in minecraft:overworld run tp @s 199.50 71.00 -63.50 60.23 -31.39," +
					"/execute in minecraft:overworld run tp @s 196.49 69.00 -69.35 59.99 -31.52," +
					"-78,47";
	private static final String input2 =
			"/execute in minecraft:overworld run tp @s 809.90 69.00 -2091.99 91.72 -31.27," +
					"/execute in minecraft:overworld run tp @s 812.25 63.09 -2100.82 91.48 -31.60," +
					"-74,-135";
	private static final String input3 =
			"/execute in minecraft:overworld run tp @s 4798.40 63.00 -307.89 174.14 -31.39," +
					"/execute in minecraft:overworld run tp @s 4786.13 64.00 -309.25 174.56 -31.60," +
					"289,-121";

	@ParameterizedTest
	@CsvSource({ input1, input2, input3 })
	public void twoEyeCalculatedTravelEyeSpies(String f3c1, String f3c2, int strongholdChunkX, int strongholdChunkZ) {
		// Arrange
		NinjabrainBotPreferences preferences = new NinjabrainBotPreferences(new UnsavedPreferences());
		preferences.sigma.set(0.005f);
		preferences.view.set(MultipleChoicePreferenceDataTypes.MainViewType.DETAILED);
		preferences.strongholdDisplayType.set(MultipleChoicePreferenceDataTypes.StrongholdDisplayType.CHUNK);
		MockedClipboardReader clipboardReader = new MockedClipboardReader();

		IDataState dataState = createDataState(preferences, clipboardReader);
		MainTextArea mainTextArea = new MainTextArea(TestUtils.createStyleManager(), preferences, dataState);
		MainTextAreaTestAdapter mainTextAreaTestAdapter = new MainTextAreaTestAdapter(mainTextArea);

		// Act
		clipboardReader.setClipboard(f3c1);
		clipboardReader.setClipboard(f3c2);

		// Assert
		TestUtils.awaitSwingEvents();
		String expectedStrongholdChunkText = String.format("(%s, %s)", strongholdChunkX, strongholdChunkZ);
		Assertions.assertEquals(expectedStrongholdChunkText, mainTextAreaTestAdapter.getDetailedTriangulationPanel_strongholdLocation(0));
		String expectedNetherText = String.format("(%s, %s)", strongholdChunkX * 2, strongholdChunkZ * 2);
		Assertions.assertEquals(expectedNetherText, mainTextAreaTestAdapter.getDetailedTriangulationPanel_netherCoords(0));
	}

	private static void inputF3C(String eye, MockedClipboardReader clipboardProvider, NinjabrainBotPreferences preferences) {
		TestF3C f3c = new TestF3C(eye);
		clipboardProvider.setClipboard(f3c.F3C);
		f3c.executeSubpixelCorrectionHotkeys(preferences);
	}

	private static IDataState createDataState(NinjabrainBotPreferences preferences, IClipboardProvider clipboardReader) {
		MockedInstanceProvider instanceProvider = new MockedInstanceProvider();
		IDataStateHandler dataStateHandler = new DataStateHandler(preferences, clipboardReader, instanceProvider);
		return dataStateHandler.getDataState();
	}

	private static String testInput(String fc31, String f3c2, int strongholdChunkX, int strongholdChunkZ) {
		return fc31 + "," + f3c2 + "," + strongholdChunkX + "," + strongholdChunkZ;
	}

}
