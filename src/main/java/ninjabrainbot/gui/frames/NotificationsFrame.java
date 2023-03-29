package ninjabrainbot.gui.frames;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.geom.RoundRectangle2D;
import java.net.URL;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import ninjabrainbot.gui.buttons.FlatButton;
import ninjabrainbot.gui.components.labels.ThemedLabel;
import ninjabrainbot.gui.style.SizePreference;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.io.VersionURL;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.util.I18n;

public class NotificationsFrame extends ThemedFrame {

	static final int PADDING = 6;

	VersionURL url;

	JPanel mainPanel;
	ThemedLabel label;

	public NotificationsFrame(StyleManager styleManager, NinjabrainBotPreferences preferences) {
		super(styleManager, preferences, I18n.get("notificationsframe.new_version_available"));
		mainPanel = new JPanel();
		mainPanel.setOpaque(false);
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.setBorder(new EmptyBorder(PADDING - 3, PADDING, PADDING, PADDING));
		add(mainPanel);
		label = new ThemedLabel(styleManager, "") {
			@Override
			public int getTextSize(SizePreference p) {
				return p.TEXT_SIZE_SMALL;
			}
		};
		label.setVerticalAlignment(SwingConstants.TOP);
		mainPanel.add(label);
		mainPanel.add(Box.createVerticalStrut(PADDING));
		FlatButton downloadButton = new FlatButton(styleManager, I18n.get("notificationsframe.download_button"));
		downloadButton.addActionListener(p -> openURL());
		mainPanel.add(downloadButton);
		mainPanel.add(Box.createVerticalStrut(PADDING));
		FlatButton changelogButton = new FlatButton(styleManager, I18n.get("notificationsframe.changelog_button"));
		changelogButton.addActionListener(p -> openReleasePage());
		mainPanel.add(changelogButton);

		// Subscriptions
		disposeHandler.add(preferences.alwaysOnTop.whenModified().subscribe(b -> setAlwaysOnTop(b)));
	}

	@Override
	public void updateBounds(StyleManager styleManager) {
		super.updateBounds(styleManager);
		label.setMaximumSize(new Dimension(styleManager.size.WIDTH, Integer.MAX_VALUE));
		setSize(styleManager.size.WIDTH * 7 / 8, styleManager.size.WIDTH * 5 / 8);
		setShape(new RoundRectangle2D.Double(0, 0, styleManager.size.WIDTH * 7 / 8, styleManager.size.WIDTH * 5 / 8, styleManager.size.WINDOW_ROUNDING, styleManager.size.WINDOW_ROUNDING));
	}

	@Override
	protected void onExitButtonClicked() {
		setVisible(false);
	}

	private void openURL() {
		try {
			Desktop.getDesktop().browse(new URL(url.url).toURI());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void openReleasePage() {
		try {
			Desktop.getDesktop().browse(new URL(url.html_url).toURI());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setURL(VersionURL url) {
		this.url = url;
		label.setText("<html>" + I18n.get("notificationsframe.update_text", url.tag) + "</html>");
//		updateBounds(gui);
	}

	public Object getURL() {
		return url;
	}

}
