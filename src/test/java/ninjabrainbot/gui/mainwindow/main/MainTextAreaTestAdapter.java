package ninjabrainbot.gui.mainwindow.main;

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
		Assertions.assertEquals(I18n.get("could_not_determine"), mainTextArea.basicTriangulation.maintextLabel.getText());
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

}
