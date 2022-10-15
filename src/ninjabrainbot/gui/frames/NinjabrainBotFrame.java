package ninjabrainbot.gui.frames;

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
import ninjabrainbot.data.IDataState;
import ninjabrainbot.data.IDataStateHandler;
import ninjabrainbot.gui.buttons.FlatButton;
import ninjabrainbot.gui.buttons.NotificationsButton;
import ninjabrainbot.gui.buttons.TitleBarButton;
import ninjabrainbot.gui.components.ThemedIcon;
import ninjabrainbot.gui.components.ThemedLabel;
import ninjabrainbot.gui.panels.main.EnderEyePanel;
import ninjabrainbot.gui.panels.main.MainButtonPanel;
import ninjabrainbot.gui.panels.main.MainTextArea;
import ninjabrainbot.gui.style.SizePreference;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.gui.style.Theme;
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
	public static final String VERSION_TEXT = "v" + Main.VERSION;

	public NinjabrainBotFrame(StyleManager styleManager, IDataState dataState, IDataStateHandler dataStateHandler) {
		super(styleManager, TITLE_TEXT);
		setLocation(Main.preferences.windowX.get(), Main.preferences.windowY.get()); // Set window position
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTranslucent(Main.preferences.translucent.get());
		setAppIcon();

		createTitleBar(styleManager);
		createComponents(styleManager, dataState, dataStateHandler);
		setupSubscriptions(styleManager, dataState);
	}

	@Override
	public void updateBounds(StyleManager styleManager) {
		super.updateBounds(styleManager);
		int titlewidth = styleManager.getTextWidth(TITLE_TEXT, styleManager.fontSize(styleManager.size.TEXT_SIZE_TITLE_LARGE, false));
		int titlebarHeight = titlebarPanel.getPreferredSize().height;
		versiontextLabel.setBounds(titlewidth + (titlebarHeight - styleManager.size.TEXT_SIZE_TITLE_SMALL) / 2, (styleManager.size.TEXT_SIZE_TITLE_LARGE - styleManager.size.TEXT_SIZE_TITLE_SMALL) / 2,
				70, titlebarHeight);
		int versionwidth = styleManager.getTextWidth(VERSION_TEXT, styleManager.fontSize(styleManager.size.TEXT_SIZE_TITLE_SMALL, false));
		lockIcon.setBounds(titlewidth + versionwidth + (titlebarHeight - styleManager.size.TEXT_SIZE_TITLE_SMALL) / 2, 0, titlebarHeight, titlebarHeight);
		// Frame size
		int extraWidth = Main.preferences.showAngleUpdates.get() && Main.preferences.view.get().equals(NinjabrainBotPreferences.DETAILED) ? styleManager.size.ANGLE_COLUMN_WIDTH : 0;
		setSize(styleManager.size.WIDTH + extraWidth, getPreferredSize().height);
		setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), styleManager.size.WINDOW_ROUNDING, styleManager.size.WINDOW_ROUNDING));
	}

	@Override
	public void updateFontsAndColors(StyleManager styleManager) {
		getContentPane().setBackground(styleManager.theme.COLOR_NEUTRAL);
		setBackground(styleManager.theme.COLOR_NEUTRAL);
	}

	public AbstractButton getSettingsButton() {
		return settingsButton;
	}

	private void setupSubscriptions(StyleManager styleManager, IDataState dataState) {
		// Settings
		sh.add(Main.preferences.translucent.whenModified().subscribe(b -> setTranslucent(b)));
		sh.add(Main.preferences.alwaysOnTop.whenModified().subscribe(b -> setAlwaysOnTop(b)));
		sh.add(Main.preferences.hotkeyMinimize.whenTriggered().subscribe(__ -> toggleMinimized()));
		// Components bounds changed
		sh.add(mainTextArea.whenModified().subscribe(__ -> updateSize(styleManager)));
		sh.add(enderEyePanel.whenModified().subscribe(__ -> updateSize(styleManager)));
		sh.add(dataState.locked().subscribeEDT(b -> lockIcon.setVisible(b)));
	}

	private void createTitleBar(StyleManager styleManager) {
		versiontextLabel = new ThemedLabel(styleManager, VERSION_TEXT) {
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
		lockIcon = new ThemedIcon(styleManager, new ImageIcon(Main.class.getResource("/resources/lock_icon.png")));
		lockIcon.setVisible(false);
		titlebarPanel.add(versiontextLabel);
		titlebarPanel.add(lockIcon);
		titlebarPanel.addButton(createExitButton(styleManager));
		titlebarPanel.addButton(createMinimizeButton(styleManager));
		settingsButton = createSettingsButton(styleManager);
		titlebarPanel.addButton(settingsButton);
		notificationsButton = new NotificationsButton(styleManager, this);
		titlebarPanel.addButton(notificationsButton);
	}

	private void createComponents(StyleManager styleManager, IDataState dataState, IDataStateHandler dataHandler) {
		// Main text
		mainTextArea = new MainTextArea(styleManager, dataState);
		add(mainTextArea);
		// "Throws" text + buttons
		MainButtonPanel mainButtonPanel = new MainButtonPanel(styleManager, dataState, dataHandler);
		add(mainButtonPanel);
		// Throw panels
		enderEyePanel = new EnderEyePanel(styleManager, dataState.getThrowSet(), dataState.getDivineContext());
		add(enderEyePanel);
	}

	private FlatButton createExitButton(StyleManager styleManager) {
		URL iconURL = Main.class.getResource("/resources/exit_icon.png");
		ImageIcon img = new ImageIcon(iconURL);
		FlatButton button = new TitleBarButton(styleManager, img) {
			private static final long serialVersionUID = -5122431392273627666L;

			@Override
			public Color getHoverColor(Theme theme) {
				return theme.COLOR_EXIT_BUTTON_HOVER;
			}
		};
		button.addActionListener(p -> System.exit(0));
		return button;
	}

	private FlatButton createMinimizeButton(StyleManager styleManager) {
		URL iconURL = Main.class.getResource("/resources/minimize_icon.png");
		ImageIcon img = new ImageIcon(iconURL);
		FlatButton button = new TitleBarButton(styleManager, img);
		button.addActionListener(p -> setState(JFrame.ICONIFIED));
		return button;
	}

	private FlatButton createSettingsButton(StyleManager styleManager) {
		URL iconURL = Main.class.getResource("/resources/settings_icon.png");
		ImageIcon img = new ImageIcon(iconURL);
		FlatButton button = new TitleBarButton(styleManager, img);
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

	private void updateSize(StyleManager styleManager) {
		int extraWidth = Main.preferences.showAngleUpdates.get() && Main.preferences.view.get().equals(NinjabrainBotPreferences.DETAILED) ? styleManager.size.ANGLE_COLUMN_WIDTH : 0;
		setSize(styleManager.size.WIDTH + extraWidth, getPreferredSize().height);
		setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), styleManager.size.WINDOW_ROUNDING, styleManager.size.WINDOW_ROUNDING));
	}

	private void setAppIcon() {
		URL iconURL = Main.class.getResource("/resources/icon.png");
		ImageIcon img = new ImageIcon(iconURL);
		setIconImage(img.getImage());
	}

	@Override
	public void dispose() {
		super.dispose();
		mainTextArea.dispose();
		enderEyePanel.dispose();
	}

}
