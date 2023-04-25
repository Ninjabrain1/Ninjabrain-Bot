package ninjabrainbot.gui.mainwindow.main;

import ninjabrainbot.gui.mainwindow.alladvancements.StructurePanel;
import ninjabrainbot.model.datastate.alladvancements.StructureType;
import ninjabrainbot.util.I18n;
import org.junit.jupiter.api.Assertions;

public class MainTextAreaTestAdapter {

	private final MainTextArea mainTextArea;

	public MainTextAreaTestAdapter(MainTextArea mainTextArea) {
		this.mainTextArea = mainTextArea;
	}

	public String getDetailedTriangulationPanel_strongholdLocation(int row) {
		return mainTextArea.detailedTriangulation.getChunkPanels().iterator().next().getLocationText();
	}

	public String getDetailedTriangulationPanel_netherCoords(int row) {
		return mainTextArea.detailedTriangulation.getChunkPanels().iterator().next().getNetherText();
	}

	public void assertFailedResultIsShown() {
		Assertions.assertTrue(mainTextArea.basicTriangulation.isVisible());
		Assertions.assertEquals(I18n.get("could_not_determine"), mainTextArea.basicTriangulation.mainTextLabel.getText());
	}

	public void assertDetailedTriangulationTopPredictionIsEqualTo(int x, int z) {
		String expectedStrongholdChunkText = String.format("(%s, %s)", x, z);
		Assertions.assertTrue(mainTextArea.detailedTriangulation.isVisible(), "DetailedTriangulationPanel is not visible.");
		Assertions.assertEquals(expectedStrongholdChunkText, getDetailedTriangulationPanel_strongholdLocation(0));
	}

	public void assertDetailedTriangulationTopNetherCoordsIsEqualTo(int x, int z) {
		String expectedNetherText = String.format("(%s, %s)", x, z);
		Assertions.assertTrue(mainTextArea.detailedTriangulation.isVisible(), "DetailedTriangulationPanel is not visible.");
		Assertions.assertEquals(expectedNetherText, getDetailedTriangulationPanel_netherCoords(0));
	}

	public String getAllAdvancementsStructurePanelCoordinates(StructureType structureType) {
		switch (structureType) {
			case Stronghold:
				return ((StructurePanel) mainTextArea.allAdvancements.getComponent(1)).getLocationText();
			case Spawn:
				return ((StructurePanel) mainTextArea.allAdvancements.getComponent(2)).getLocationText();
			case Outpost:
				return ((StructurePanel) mainTextArea.allAdvancements.getComponent(3)).getLocationText();
			case Monument:
				return ((StructurePanel) mainTextArea.allAdvancements.getComponent(4)).getLocationText();
		}
		throw new RuntimeException("Unsupported structure type: " + structureType);
	}

	public void assertAllAdvancementsStructureCoordsAre(int x, int z, StructureType structureType) {
		String expectedStructureText = String.format("(%s, %s)", x, z);
		Assertions.assertTrue(mainTextArea.allAdvancements.isVisible(), "AllAdvancementsPanel is not visible.");
		Assertions.assertEquals(expectedStructureText, getAllAdvancementsStructurePanelCoordinates(structureType));
	}

	public boolean isBlindPanelVisible() {
		return mainTextArea.blind.isVisible();
	}

	public boolean isAllAdvancementsPanelVisible() {
		return mainTextArea.allAdvancements.isVisible();
	}

}
