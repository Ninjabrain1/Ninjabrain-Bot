package ninjabrainbot.gui.components;

import java.awt.Color;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import ninjabrainbot.Main;
import ninjabrainbot.gui.GUI;
import ninjabrainbot.gui.NotificationsFrame;
import ninjabrainbot.gui.SizePreference;
import ninjabrainbot.gui.Theme;
import ninjabrainbot.io.VersionURL;

public class NinjabrainBotFrame extends ThemedFrame {

	private static final long serialVersionUID = -8033268694989543737L;
	
	private JLabel versiontextLabel;
	private NotificationsButton notificationsButton;

	public static final String TITLE_TEXT = "Ninjabrain Bot ";
	public static final String VERSION_TEXT =  "v" + Main.VERSION;

	public NinjabrainBotFrame(GUI gui) {
		super(gui, TITLE_TEXT);
		setLocation(Main.preferences.windowX.get(), Main.preferences.windowY.get()); // Set window position
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Create title bar
		versiontextLabel = new ThemedLabel(gui, VERSION_TEXT) {
			private static final long serialVersionUID = 7210941876032010219L;
			@Override
			public int getTextSize(SizePreference p) {
				return p.TEXT_SIZE_TITLE_SMALL;
			}
			@Override
			public Color getForegroundColor(Theme theme) {
				return theme.TEXT_COLOR_WEAK;
			}
		};
		titlebarPanel.add(versiontextLabel);
		titlebarPanel.addButton(getExitButton(gui));
		titlebarPanel.addButton(getMinimizeButton(gui));
		titlebarPanel.addButton(getSettingsButton(gui));
		notificationsButton = new NotificationsButton(gui);
		titlebarPanel.addButton(notificationsButton);
		// Shutdown hook
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				Main.preferences.windowX.set(getX());
				Main.preferences.windowY.set(getY());
			}
		});
	}
	
	@Override
	public void updateBounds(GUI gui) {
		super.updateBounds(gui);
		int titlewidth = gui.getTextWidth(TITLE_TEXT, gui.fontSize(gui.size.TEXT_SIZE_TITLE_LARGE, false));
		int titlebarHeight = titlebarPanel.getPreferredSize().height;
		versiontextLabel.setBounds(titlewidth + (titlebarHeight - gui.size.TEXT_SIZE_TITLE_SMALL)/2, (gui.size.TEXT_SIZE_TITLE_LARGE - gui.size.TEXT_SIZE_TITLE_SMALL)/2, 50, titlebarHeight);
	}
	
	private FlatButton getExitButton(GUI gui) {
		URL iconURL = Main.class.getResource("/resources/exit_icon.png");
		ImageIcon img = new ImageIcon(iconURL);
		FlatButton button = new TitleBarButton(gui, img) {
			private static final long serialVersionUID = -5122431392273627666L;
			@Override
			public Color getHoverColor(Theme theme) {
				return theme.COLOR_EXIT_BUTTON_HOVER;
			}
		};
		button.addActionListener(p -> System.exit(0));
		return button;
	}

	private FlatButton getMinimizeButton(GUI gui) {
		URL iconURL = Main.class.getResource("/resources/minimize_icon.png");
		ImageIcon img = new ImageIcon(iconURL);
		FlatButton button = new TitleBarButton(gui, img);
		button.addActionListener(p -> setState(JFrame.ICONIFIED));
		return button;
	}
	
	private FlatButton getSettingsButton(GUI gui) {
		URL iconURL = Main.class.getResource("/resources/settings_icon.png");
		ImageIcon img = new ImageIcon(iconURL);
		FlatButton button = new TitleBarButton(gui, img);
		button.addActionListener(p -> gui.toggleOptionsWindow());
		return button;
	}
	
	public NotificationsButton getNotificationsButton() {
		return notificationsButton;
	}
	
	public NotificationsFrame getNotificationsFrame() {
		return notificationsButton.getNotificationsFrame();
	}

	public void setURL(VersionURL url) {
		notificationsButton.setURL(url);
	}
	
}
