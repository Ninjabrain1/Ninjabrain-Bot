package ninjabrainbot.gui.frames;

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
import ninjabrainbot.data.DataState.BoatState;
import ninjabrainbot.data.IDataState;
import ninjabrainbot.data.IDataStateHandler;
import ninjabrainbot.event.IDisposable;
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
import ninjabrainbot.io.preferences.MultipleChoicePreferenceDataTypes.MainViewType;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.util.I18n;
import ninjabrainbot.util.Profiler;

public class NinjabrainBotFrame extends ThemedFrame implements IDisposable {

	private static final long serialVersionUID = -8033268694989543737L;

	private NinjabrainBotPreferences preferences;

	private ThemedLabel versiontextLabel;
	private NotificationsButton notificationsButton;
	private JButton settingsButton;
	private JLabel lockIcon;
	private JLabel boatIcon;

	private MainTextArea mainTextArea;
	private EnderEyePanel enderEyePanel;

	private static final String TITLE_TEXT = I18n.get("title");
	private static final String VERSION_TEXT = "v" + Main.VERSION;

	public NinjabrainBotFrame(StyleManager styleManager, NinjabrainBotPreferences preferences, IDataStateHandler dataStateHandler) {
		super(styleManager, preferences, TITLE_TEXT);
		this.preferences = preferences;
		Profiler.start("NinjabrainBotFrame");
		setLocation(preferences.windowX.get(), preferences.windowY.get()); // Set window position
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTranslucent(preferences.translucent.get());
		setAppIcon();

		createTitleBar(styleManager, dataStateHandler.getDataState());
		createComponents(styleManager, dataStateHandler);
		setupSubscriptions(styleManager, dataStateHandler.getDataState());
		Profiler.stop();
	}

	@Override
	public void updateBounds(StyleManager styleManager) {
		super.updateBounds(styleManager);
		int titlewidth = styleManager.getTextWidth(TITLE_TEXT, styleManager.fontSize(styleManager.size.TEXT_SIZE_TITLE_LARGE, false));
		int titlebarHeight = titlebarPanel.getPreferredSize().height;
		versiontextLabel.setBounds(titlewidth + (titlebarHeight - styleManager.size.TEXT_SIZE_TITLE_SMALL) / 2, (styleManager.size.TEXT_SIZE_TITLE_LARGE - styleManager.size.TEXT_SIZE_TITLE_SMALL) / 2, 70, titlebarHeight);
		int versionwidth = styleManager.getTextWidth(VERSION_TEXT, styleManager.fontSize(styleManager.size.TEXT_SIZE_TITLE_SMALL, false));
		lockIcon.setBounds(titlewidth + versionwidth + (titlebarHeight - styleManager.size.TEXT_SIZE_TITLE_SMALL) / 2, 0, titlebarHeight, titlebarHeight);
		int lockwidth = lockIcon.getWidth();
		boatIcon.setBounds(titlewidth + versionwidth + lockwidth + (titlebarHeight - styleManager.size.TEXT_SIZE_TITLE_SMALL) / 2, 0, titlebarHeight, titlebarHeight);
		// Frame size
		int extraWidth = preferences.showAngleUpdates.get() && preferences.view.get().equals(MainViewType.DETAILED) ? styleManager.size.ANGLE_COLUMN_WIDTH : 0;
		setSize(styleManager.size.WIDTH + extraWidth, getPreferredSize().height);
		setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), styleManager.size.WINDOW_ROUNDING, styleManager.size.WINDOW_ROUNDING));
	}

	public AbstractButton getSettingsButton() {
		return settingsButton;
	}

	public boolean isIdle() {
		return mainTextArea.isIdle();
	}

	private void setupSubscriptions(StyleManager styleManager, IDataState dataState) {
		// Settings
		sh.add(preferences.translucent.whenModified().subscribe(b -> setTranslucent(b)));
		sh.add(preferences.alwaysOnTop.whenModified().subscribe(b -> setAlwaysOnTop(b)));
		sh.add(preferences.hotkeyMinimize.whenTriggered().subscribe(__ -> toggleMinimized()));
		// Components bounds changed
		sh.add(mainTextArea.whenModified().subscribe(__ -> updateSize(styleManager)));
		sh.add(enderEyePanel.whenModified().subscribe(__ -> updateSize(styleManager)));
		// Lock
		sh.add(dataState.locked().subscribeEDT(b -> lockIcon.setVisible(b)));
		// Boat Icon
		sh.add(preferences.useTallRes.whenModified().subscribe(b -> boatIcon.setVisible(b && preferences.usePreciseAngle.get())));
		sh.add(preferences.usePreciseAngle.whenModified().subscribe(b -> boatIcon.setVisible(b)));
		sh.add(dataState.boatState().subscribeEDT(b -> boatIcon.setIcon(getBoatIcon(b))));
	}

	private void createTitleBar(StyleManager styleManager, IDataState dataState) {
		versiontextLabel = new ThemedLabel(styleManager, VERSION_TEXT) {
			private static final long serialVersionUID = 7210941876032010219L;

			@Override
			public int getTextSize(SizePreference p) {
				return p.TEXT_SIZE_TITLE_SMALL;
			}
		};
		versiontextLabel.setForegroundColor(styleManager.currentTheme.TEXT_COLOR_WEAK);
		lockIcon = new ThemedIcon(styleManager, new ImageIcon(Main.class.getResource("/lock_icon.png")));
		lockIcon.setVisible(dataState.locked().get());
		boatIcon = new ThemedIcon(styleManager, getBoatIcon(dataState.boatState().get()));
		boatIcon.setVisible(preferences.useTallRes.get() && preferences.usePreciseAngle.get());
		titlebarPanel.add(versiontextLabel);
		titlebarPanel.add(lockIcon);
		titlebarPanel.add(boatIcon);
		titlebarPanel.addButton(createMinimizeButton(styleManager));
		settingsButton = createSettingsButton(styleManager);
		titlebarPanel.addButton(settingsButton);
		notificationsButton = new NotificationsButton(styleManager, this, preferences);
		titlebarPanel.addButton(notificationsButton);
	}

	@Override
	protected void onExitButtonClicked() {
		System.exit(0);
	}

	private void createComponents(StyleManager styleManager, IDataStateHandler dataStateHandler) {
		IDataState dataState = dataStateHandler.getDataState();
		// Main text
		mainTextArea = new MainTextArea(styleManager, preferences, dataState);
		add(mainTextArea);
		// "Throws" text + buttons
		MainButtonPanel mainButtonPanel = new MainButtonPanel(styleManager, dataState, dataStateHandler);
		add(mainButtonPanel);
		// Throw panels
		enderEyePanel = new EnderEyePanel(styleManager, preferences, dataStateHandler, dataState.getDivineContext());
		add(enderEyePanel);
	}

	private FlatButton createMinimizeButton(StyleManager styleManager) {
		URL iconURL = Main.class.getResource("/minimize_icon.png");
		ImageIcon img = new ImageIcon(iconURL);
		FlatButton button = new TitleBarButton(styleManager, img);
		button.addActionListener(p -> setState(JFrame.ICONIFIED));
		return button;
	}

	private FlatButton createSettingsButton(StyleManager styleManager) {
		URL iconURL = Main.class.getResource("/settings_icon.png");
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
		int extraWidth = preferences.showAngleUpdates.get() && preferences.view.get().equals(MainViewType.DETAILED) ? styleManager.size.ANGLE_COLUMN_WIDTH : 0;
		setSize(styleManager.size.WIDTH + extraWidth, getPreferredSize().height);
		setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), styleManager.size.WINDOW_ROUNDING, styleManager.size.WINDOW_ROUNDING));
	}

	private void setAppIcon() {
		URL iconURL = Main.class.getResource("/icon.png");
		ImageIcon img = new ImageIcon(iconURL);
		setIconImage(img.getImage());
	}

	private ImageIcon getBoatIcon(BoatState boatState) {
		switch (boatState) {
			case ERROR:
				return new ImageIcon(Main.class.getResource("/boat_red.png"));
			case MEASURING:
				return new ImageIcon(Main.class.getResource("/boat_blue.png"));
			case VALID:
				return new ImageIcon(Main.class.getResource("/boat_green.png"));
			default:
				return new ImageIcon(Main.class.getResource("/boat_gray.png"));
		}
	}

	@Override
	public void dispose() {
		super.dispose();
		mainTextArea.dispose();
		enderEyePanel.dispose();
	}

}
