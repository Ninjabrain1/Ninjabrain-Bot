package ninjabrainbot.gui.mainwindow.eyethrows;

public class EnderEyePanelTestAdapter {

	private final EnderEyePanel enderEyePanel;

	public EnderEyePanelTestAdapter(EnderEyePanel enderEyePanel) {
		this.enderEyePanel = enderEyePanel;
	}

	public ThrowPanelTestAdapter getPanel(int index) {
		return new ThrowPanelTestAdapter(enderEyePanel.throwPanels[index]);
	}


}
