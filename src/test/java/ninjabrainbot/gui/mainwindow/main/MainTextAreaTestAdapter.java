package ninjabrainbot.gui.mainwindow.main;

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

	public void assertDetailedTriangulationTopPredictionIsEqualTo(int x, int z) {
		String expectedStrongholdChunkText = String.format("(%s, %s)", x, z);
		Assertions.assertEquals(expectedStrongholdChunkText, getDetailedTriangulationPanel_strongholdLocation(0));
	}

	public void assertDetailedTriangulationTopNetherCoordsIsEqualTo(int x, int z) {
		String expectedNetherText = String.format("(%s, %s)", x, z);
		Assertions.assertEquals(expectedNetherText, getDetailedTriangulationPanel_netherCoords(0));
	}

}
