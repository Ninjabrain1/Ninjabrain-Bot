package ninjabrainbot.gui.frames;

import javax.swing.BoxLayout;
import javax.swing.JFrame;

import ninjabrainbot.Main;
import ninjabrainbot.gui.components.ThemedLabel;
import ninjabrainbot.gui.panels.TitleBarPanel;
import ninjabrainbot.gui.style.SizePreference;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.util.IDisposable;
import ninjabrainbot.util.SubscriptionHandler;

public class ThemedFrame extends JFrame implements IDisposable {

	private static final long serialVersionUID = -9103006492414835286L;

	protected TitleBarPanel titlebarPanel;
	protected ThemedLabel titletextLabel;

	protected SubscriptionHandler sh = new SubscriptionHandler();

	public ThemedFrame(StyleManager styleManager, String title) {
		super(title);
		styleManager.registerThemedFrame(this);
		setUndecorated(true); // Remove borders
		setAlwaysOnTop(Main.preferences.alwaysOnTop.get()); // Always focused
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		titlebarPanel = new TitleBarPanel(styleManager, this);
		add(titlebarPanel);
		titletextLabel = new ThemedLabel(styleManager, title, true) {
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

	public void updateBounds(StyleManager styleManager) {
		int titlebarHeight = titlebarPanel.getPreferredSize().height;
		titletextLabel.setBounds((titlebarHeight - styleManager.size.TEXT_SIZE_TITLE_LARGE) / 2, 0, titletextLabel.getPreferredSize().width, titlebarHeight);
	}

	public void updateFontsAndColors(StyleManager styleManager) {
	}

	@Override
	public void dispose() {
		super.dispose();
		sh.dispose();
	}

}
