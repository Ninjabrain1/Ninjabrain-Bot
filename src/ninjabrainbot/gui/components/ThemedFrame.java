package ninjabrainbot.gui.components;

import javax.swing.JFrame;

import ninjabrainbot.Main;
import ninjabrainbot.gui.GUI;
import ninjabrainbot.gui.TextSizePreference;

public class ThemedFrame extends JFrame {

	private static final long serialVersionUID = -9103006492414835286L;

	protected TitleBarPanel titlebarPanel;
	protected ThemedLabel titletextLabel;
	
	public ThemedFrame(GUI gui, String title) {
		super(title);
		setUndecorated(true); // Remove borders
		setAlwaysOnTop(Main.preferences.alwaysOnTop.get()); // Always focused
		setLayout(null);
		titlebarPanel = new TitleBarPanel(gui, this);
		titlebarPanel.setLayout(null);
		add(titlebarPanel);
		titletextLabel = new ThemedLabel(gui, title, true) {
			private static final long serialVersionUID = 1508931943984181857L;
			@Override
			public int getTextSize(TextSizePreference p) {
				return p.TITLE_BAR_TEXT_SIZE;
			}
		};
		titlebarPanel.add(titletextLabel);
	}
	
	public TitleBarPanel getTitleBar() {
		return titlebarPanel;
	}
	
	public void updateBounds(GUI gui) {
		titlebarPanel.setBounds(0, 0, getWidth(), GUI.TITLE_BAR_HEIGHT);
		titletextLabel.setBounds((GUI.TITLE_BAR_HEIGHT - gui.textSize.TITLE_BAR_TEXT_SIZE)/2, 0, 150, GUI.TITLE_BAR_HEIGHT);
	}
	
}
