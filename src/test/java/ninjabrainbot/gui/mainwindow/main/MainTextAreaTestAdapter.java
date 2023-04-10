package ninjabrainbot.gui.mainwindow.main;

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

}
