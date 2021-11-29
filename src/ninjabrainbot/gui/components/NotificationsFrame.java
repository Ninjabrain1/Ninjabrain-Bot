package ninjabrainbot.gui.components;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.geom.RoundRectangle2D;
import java.net.URL;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import ninjabrainbot.Main;
import ninjabrainbot.gui.GUI;
import ninjabrainbot.gui.Theme;
import ninjabrainbot.io.VersionURL;

public class NotificationsFrame extends ThemedFrame {	
	
	private static final long serialVersionUID = 1767257205939839293L;
	
	static final int PADDING = 6;
	
	GUI gui;
	VersionURL url;
	
	JPanel mainPanel;
	ThemedLabel label;

	public NotificationsFrame(GUI gui) {
		super(gui, "New version available");
		this.gui = gui;
		titlebarPanel.addButton(getExitButton());
		mainPanel = new JPanel();
		mainPanel.setOpaque(false);
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.setBorder(new EmptyBorder(PADDING - 3, PADDING, PADDING, PADDING));
		add(mainPanel);
		label = new ThemedLabel(gui, "");
		mainPanel.add(label);
		mainPanel.add(new ThemedLabel(gui, "<html>This notification can be disabled in the settings menu.</html>"));
		mainPanel.add(Box.createVerticalStrut(PADDING));
		FlatButton downloadButton = new FlatButton(gui, "Download .jar");
		downloadButton.addActionListener(p -> openURL());
		mainPanel.add(downloadButton);
		mainPanel.add(Box.createVerticalStrut(PADDING));
		FlatButton changelogButton = new FlatButton(gui, "Changelog (opens browser)");
		changelogButton.addActionListener(p -> openReleasePage());
		mainPanel.add(changelogButton);
	}
	
	@Override
	public void updateBounds(GUI gui) {
		int width = 240;
		int height = 200;
		setShape(new RoundRectangle2D.Double(0, 0, width, height, GUI.WINDOW_ROUNDING, GUI.WINDOW_ROUNDING));
		setSize(width, height);
		titlebarPanel.setBounds(0, 0, getWidth(), GUI.TITLE_BAR_HEIGHT);
		titletextLabel.setBounds((GUI.TITLE_BAR_HEIGHT - gui.textSize.TITLE_BAR_TEXT_SIZE)/2, 0, 200, GUI.TITLE_BAR_HEIGHT);
		mainPanel.setBounds(0, GUI.TITLE_BAR_HEIGHT, width, height - GUI.TITLE_BAR_HEIGHT);
	}
	
	public void updateFontsAndColors() {
		getContentPane().setBackground(gui.theme.COLOR_NEUTRAL);
		setBackground(gui.theme.COLOR_NEUTRAL);
	}
	
	private FlatButton getExitButton() {
		URL iconURL = Main.class.getResource("/resources/exit_icon.png");
		ImageIcon img = new ImageIcon(iconURL);
		FlatButton button = new TitleBarButton(gui, img) {
			private static final long serialVersionUID = 4380111129291481489L;
			@Override
			public Color getHoverColor(Theme theme) {
				return theme.COLOR_EXIT_BUTTON_HOVER;
			}
		};
		button.addActionListener(p -> setVisible(false));
		return button;
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
		label.setText("Version " + url.tag + " is available.");
	}

	public Object getURL() {
		return url;
	}
	
}
