package ninjabrainbot.gui.mainwindow.eyethrows;

public class ThrowPanelTestAdapter {

	private final ThrowPanel throwPanel;

	public ThrowPanelTestAdapter(ThrowPanel throwPanel) {
		this.throwPanel = throwPanel;
	}

	public float getX(){
		return Float.parseFloat(throwPanel.x.getText());
	}

	public float getZ(){
		return Float.parseFloat(throwPanel.z.getText());
	}

	public float getAngle(){
		return Float.parseFloat(throwPanel.alpha.getText());
	}

}
