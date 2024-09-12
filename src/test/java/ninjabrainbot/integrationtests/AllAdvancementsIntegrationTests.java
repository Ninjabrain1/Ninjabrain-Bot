package ninjabrainbot.integrationtests;

import ninjabrainbot.gui.mainwindow.main.MainTextAreaTestAdapter;
import ninjabrainbot.model.datastate.alladvancements.IAllAdvancementsDataState;
import ninjabrainbot.model.datastate.alladvancements.AllAdvancementsStructureType;
import ninjabrainbot.model.datastate.common.ResultType;
import ninjabrainbot.util.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class AllAdvancementsIntegrationTests {

	private static final String input1 =
			"/execute in minecraft:the_nether run tp @s 59.37 85.00 -2.89 -133.68 81.14," +
			"/execute in minecraft:overworld run tp @s 1477.68 70.00 -211.29 -103.76 -31.31," +
			"/execute in minecraft:overworld run tp @s 1478.10 70.00 -209.18 -103.92 -31.64," +
			"138,-25," +
			"/execute in minecraft:overworld run tp @s -214.50 71.00 185.50 0.00 0.00," +
			"-215, 185," +
			"/execute in minecraft:overworld run tp @s 1567.77 101.81 2166.25 -343.40 11.85," +
			"1538, 2265," +
			"/execute in minecraft:overworld run tp @s 3357.69 55.29 3693.87 -48.99 50.06," +
			"3357, 3693,";

	@ParameterizedTest
	@CsvSource({ input1 })
	void uiUpdatesAsExpected_TypicalAASpeedrun(String f3c1, String f3c2, String f3c3, int strongholdChunkX, int strongholdChunkZ,
											   String f3cSpawn, int spawnX, int spawnZ,
											   String f3cOutpost, int outpostX, int outpostZ,
											   String f3cMonument, int monumentX, int monumentZ) {
		IntegrationTestBuilder testBuilder = new IntegrationTestBuilder().withAllAdvancementsSettings().withProSettings();
		MainTextAreaTestAdapter mainTextArea = testBuilder.createMainTextArea();
		IAllAdvancementsDataState aaDataState = testBuilder.dataState.allAdvancementsDataState();

		doAnyPercentSplit(testBuilder, aaDataState, mainTextArea, f3c1, f3c2, f3c3, strongholdChunkX, strongholdChunkZ);

		testBuilder.enterEnd();
		TestUtils.awaitSwingEvents();
		Assertions.assertTrue(aaDataState.allAdvancementsModeEnabled().get());
		Assertions.assertSame(testBuilder.dataState.resultType().get(), ResultType.ALL_ADVANCEMENTS);
		mainTextArea.assertAllAdvancementsStructureCoordsAre(strongholdChunkX * 16 + 4, strongholdChunkZ * 16 + 4, AllAdvancementsStructureType.Stronghold);
		Assertions.assertTrue(mainTextArea.getAllAdvancementsStructurePanelCoordinates(AllAdvancementsStructureType.Spawn).isEmpty());
		Assertions.assertTrue(mainTextArea.getAllAdvancementsStructurePanelCoordinates(AllAdvancementsStructureType.Outpost).isEmpty());
		Assertions.assertTrue(mainTextArea.getAllAdvancementsStructurePanelCoordinates(AllAdvancementsStructureType.Monument).isEmpty());

		testBuilder.setClipboard(f3cSpawn);
		TestUtils.awaitSwingEvents();
		mainTextArea.assertAllAdvancementsStructureCoordsAre(spawnX, spawnZ, AllAdvancementsStructureType.Spawn);

		testBuilder.setClipboard(f3cOutpost);
		TestUtils.awaitSwingEvents();
		mainTextArea.assertAllAdvancementsStructureCoordsAre(outpostX, outpostZ, AllAdvancementsStructureType.Outpost);

		testBuilder.setClipboard(f3cMonument);
		TestUtils.awaitSwingEvents();
		mainTextArea.assertAllAdvancementsStructureCoordsAre(monumentX, monumentZ, AllAdvancementsStructureType.Monument);

		testBuilder.resetCalculator();
		TestUtils.awaitSwingEvents();
		Assertions.assertFalse(mainTextArea.isAllAdvancementsPanelVisible());
		Assertions.assertTrue(mainTextArea.getAllAdvancementsStructurePanelCoordinates(AllAdvancementsStructureType.Stronghold).isEmpty());
		Assertions.assertTrue(mainTextArea.getAllAdvancementsStructurePanelCoordinates(AllAdvancementsStructureType.Spawn).isEmpty());
		Assertions.assertTrue(mainTextArea.getAllAdvancementsStructurePanelCoordinates(AllAdvancementsStructureType.Outpost).isEmpty());
		Assertions.assertTrue(mainTextArea.getAllAdvancementsStructurePanelCoordinates(AllAdvancementsStructureType.Monument).isEmpty());
	}

	@ParameterizedTest
	@CsvSource({ input1 })
	void uiUpdatesAsExpected_HotkeysAreDisabledInAaMode(String f3c1, String f3c2, String f3c3, int strongholdChunkX, int strongholdChunkZ) {
		IntegrationTestBuilder testBuilder = new IntegrationTestBuilder().withAllAdvancementsSettings().withProSettings();
		MainTextAreaTestAdapter mainTextArea = testBuilder.createMainTextArea();
		IAllAdvancementsDataState aaDataState = testBuilder.dataState.allAdvancementsDataState();

		doAnyPercentSplit(testBuilder, aaDataState, mainTextArea, f3c1, f3c2, f3c3, strongholdChunkX, strongholdChunkZ);

		testBuilder.enterEnd();
		testBuilder.inputSubpixelCorrections(10);
		TestUtils.awaitSwingEvents();
		mainTextArea.assertAllAdvancementsStructureCoordsAre(strongholdChunkX * 16 + 4, strongholdChunkZ * 16 + 4, AllAdvancementsStructureType.Stronghold);
		testBuilder.inputSubpixelCorrections(-10);
		TestUtils.awaitSwingEvents();
		mainTextArea.assertAllAdvancementsStructureCoordsAre(strongholdChunkX * 16 + 4, strongholdChunkZ * 16 + 4, AllAdvancementsStructureType.Stronghold);
		testBuilder.inputStandardDeviationToggle();
		TestUtils.awaitSwingEvents();
		mainTextArea.assertAllAdvancementsStructureCoordsAre(strongholdChunkX * 16 + 4, strongholdChunkZ * 16 + 4, AllAdvancementsStructureType.Stronghold);
	}

	private void doAnyPercentSplit(IntegrationTestBuilder testBuilder, IAllAdvancementsDataState aaDataState, MainTextAreaTestAdapter mainTextArea,
								   String f3c1, String f3c2, String f3c3, int strongholdChunkX, int strongholdChunkZ) {
		testBuilder.enterNewWorld();
		Assertions.assertFalse(aaDataState.allAdvancementsModeEnabled().get());
		Assertions.assertSame(testBuilder.dataState.resultType().get(), ResultType.NONE);
		Assertions.assertTrue(mainTextArea.getDetailedTriangulationPanel_strongholdLocation(0).isEmpty());

		testBuilder.setClipboard(f3c1); // Blind
		TestUtils.awaitSwingEvents();
		Assertions.assertFalse(aaDataState.allAdvancementsModeEnabled().get());
		Assertions.assertSame(testBuilder.dataState.resultType().get(), ResultType.BLIND);
		Assertions.assertTrue(mainTextArea.isBlindPanelVisible());

		testBuilder.setClipboard(f3c2); // First eye
		testBuilder.setClipboard(f3c3); // Second eye
		TestUtils.awaitSwingEvents();
		Assertions.assertFalse(aaDataState.allAdvancementsModeEnabled().get());
		Assertions.assertSame(testBuilder.dataState.resultType().get(), ResultType.TRIANGULATION);
		mainTextArea.assertDetailedTriangulationTopPredictionIsEqualTo(strongholdChunkX, strongholdChunkZ);
	}

}
