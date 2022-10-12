package ninjabrainbot.gui.components;

import javax.swing.BoxLayout;
import javax.swing.JFrame;

import ninjabrainbot.Main;
import ninjabrainbot.gui.GUI;
import ninjabrainbot.gui.SizePreference;
import ninjabrainbot.util.IDisposable;
import ninjabrainbot.util.SubscriptionHandler;

public class ThemedFrame extends JFrame implements IDisposable {

	private static final long serialVersionUID = -9103006492414835286L;

	protected TitleBarPanel titlebarPanel;
	protected ThemedLabel titletextLabel;

	protected SubscriptionHandler sh = new SubscriptionHandler();
	
	public ThemedFrame(GUI gui, String title) {
		super(title);
		setUndecorated(true); // Remove borders
		setAlwaysOnTop(Main.preferences.alwaysOnTop.get()); // Always focused
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		titlebarPanel = new TitleBarPanel(gui, this);
		add(titlebarPanel);
		titletextLabel = new ThemedLabel(gui, title, true) {
			private static final long serialVersionUID = 1508931943984181857L;
			@Override
			public int getTextSize(SizePreference p) {
				return p.TEXT_SIZE_TITLE_LARGE;
			}
		};
		titlebarPanel.add(titletextLabel);
	}

	public TitleBarPanel getTitleBar() {
		return titlebarPanel;
	}
	
	public void updateBounds(GUI gui) {
		int titlebarHeight = titlebarPanel.getPreferredSize().height;
		titletextLabel.setBounds((titlebarHeight - gui.size.TEXT_SIZE_TITLE_LARGE) / 2, 0,
				titletextLabel.getPreferredSize().width, titlebarHeight);
	}

	@Override
	public void dispose() {
		super.dispose();
		sh.dispose();
	}

}
