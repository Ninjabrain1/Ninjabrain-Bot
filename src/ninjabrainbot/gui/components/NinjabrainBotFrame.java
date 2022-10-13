package ninjabrainbot.gui.components;

import java.awt.Color;
import java.awt.GraphicsDevice;
import java.awt.GraphicsDevice.WindowTranslucency;
import java.awt.GraphicsEnvironment;
import java.awt.geom.RoundRectangle2D;
import java.net.URL;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import ninjabrainbot.Main;
import ninjabrainbot.calculator.IDataState;
import ninjabrainbot.calculator.IDataStateHandler;
import ninjabrainbot.gui.GUI;
import ninjabrainbot.gui.SizePreference;
import ninjabrainbot.gui.Theme;
import ninjabrainbot.io.NinjabrainBotPreferences;
import ninjabrainbot.util.I18n;
import ninjabrainbot.util.IDisposable;

public class NinjabrainBotFrame extends ThemedFrame implements IDisposable {

	private static final long serialVersionUID = -8033268694989543737L;
	
	private JLabel versiontextLabel;
	private NotificationsButton notificationsButton;
	private JButton settingsButton;
	private JLabel lockIcon;

	private MainTextArea mainTextArea;
	private EnderEyePanel enderEyePanel;
	
	public static final String TITLE_TEXT = I18n.get("title");
	public static final String VERSION_TEXT =  "v" + Main.VERSION;
	
	public NinjabrainBotFrame(GUI gui, IDataState dataState, IDataStateHandler dataStateHandler) {
		super(gui, TITLE_TEXT);
		setLocation(Main.preferences.windowX.get(), Main.preferences.windowY.get()); // Set window position
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTranslucent(Main.preferences.translucent.get());

		createTitleBar(gui);
		createComponents(gui, dataState, dataStateHandler);
		setupSubscriptions(gui, dataState);
	}
	
	@Override
	public void updateBounds(GUI gui) {
		super.updateBounds(gui);
		int titlewidth = gui.getTextWidth(TITLE_TEXT, gui.fontSize(gui.size.TEXT_SIZE_TITLE_LARGE, false));
		int titlebarHeight = titlebarPanel.getPreferredSize().height;
		versiontextLabel.setBounds(titlewidth + (titlebarHeight - gui.size.TEXT_SIZE_TITLE_SMALL)/2, (gui.size.TEXT_SIZE_TITLE_LARGE - gui.size.TEXT_SIZE_TITLE_SMALL)/2, 70, titlebarHeight);
		int versionwidth = gui.getTextWidth(VERSION_TEXT, gui.fontSize(gui.size.TEXT_SIZE_TITLE_SMALL, false));
		lockIcon.setBounds(titlewidth + versionwidth + (titlebarHeight - gui.size.TEXT_SIZE_TITLE_SMALL)/2, 0, titlebarHeight, titlebarHeight);
	}
	
	public AbstractButton getSettingsButton() {
		return settingsButton;
	}
	
	private void setupSubscriptions(GUI gui, IDataState dataState) {
		// Settings
		sh.add(Main.preferences.translucent.whenModified().subscribe(b -> setTranslucent(b)));
		sh.add(Main.preferences.alwaysOnTop.whenModified().subscribe(b -> setAlwaysOnTop(b)));
		sh.add(Main.preferences.hotkeyMinimize.whenTriggered().subscribe(__ -> toggleMinimized()));
		// Components bounds changed
		sh.add(mainTextArea.whenSizeModified.subscribe(__ -> updateSize(gui)));
		sh.add(enderEyePanel.whenSizeModified.subscribe(__ -> updateSize(gui)));
		sh.add(dataState.whenLockedChanged().subscribe(b -> lockIcon.setVisible(b)));
	}

	private void createTitleBar(GUI gui) {
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
		lockIcon = new ThemedIcon(gui, new ImageIcon(Main.class.getResource("/resources/lock_icon.png")));
		lockIcon.setVisible(false);
		titlebarPanel.add(versiontextLabel);
		titlebarPanel.add(lockIcon);
		titlebarPanel.addButton(createExitButton(gui));
		titlebarPanel.addButton(createMinimizeButton(gui));
		settingsButton = createSettingsButton(gui);
		titlebarPanel.addButton(settingsButton);
		notificationsButton = new NotificationsButton(gui);
		titlebarPanel.addButton(notificationsButton);
	}

	private void createComponents(GUI gui, IDataState dataState, IDataStateHandler dataHandler) {
		// Main text
		mainTextArea = new MainTextArea(gui, dataState);
		add(mainTextArea);
		// "Throws" text + buttons
		MainButtonPanel mainButtonPanel = new MainButtonPanel(gui, dataState, dataHandler);
		add(mainButtonPanel);
		// Throw panels
		enderEyePanel = new EnderEyePanel(gui, dataState.getThrowSet(), dataState.getDivineContext());
		add(enderEyePanel);
	}
	
	private FlatButton createExitButton(GUI gui) {
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

	private FlatButton createMinimizeButton(GUI gui) {
		URL iconURL = Main.class.getResource("/resources/minimize_icon.png");
		ImageIcon img = new ImageIcon(iconURL);
		FlatButton button = new TitleBarButton(gui, img);
		button.addActionListener(p -> setState(JFrame.ICONIFIED));
		return button;
	}
	
	private FlatButton createSettingsButton(GUI gui) {
		URL iconURL = Main.class.getResource("/resources/settings_icon.png");
		ImageIcon img = new ImageIcon(iconURL);
		FlatButton button = new TitleBarButton(gui, img);
		return button;
	}
	
	private void toggleMinimized() {
		int state = getState();
		if (state == JFrame.ICONIFIED) {
			setState(JFrame.NORMAL);
		} else {
			setState(JFrame.ICONIFIED);
		}
	}
	
	private void setTranslucent(boolean t) {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();
		if (gd.isWindowTranslucencySupported(WindowTranslucency.TRANSLUCENT)) {
			setOpacity(t ? 0.75f : 1.0f);
		}
	}
	
	private void updateSize(GUI gui) {
		int extraWidth = Main.preferences.showAngleUpdates.get()
				&& Main.preferences.view.get().equals(NinjabrainBotPreferences.DETAILED) ? gui.size.ANGLE_COLUMN_WIDTH : 0;
		setSize(gui.size.WIDTH + extraWidth, getPreferredSize().height);
		setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), gui.size.WINDOW_ROUNDING,
				gui.size.WINDOW_ROUNDING));
	}
	
	@Override
	public void dispose() {
		super.dispose();
		mainTextArea.dispose();
		enderEyePanel.dispose();
	}
	
}
