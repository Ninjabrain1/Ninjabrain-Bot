package ninjabrainbot.gui.frames;

import java.awt.GraphicsDevice;
import java.awt.GraphicsDevice.WindowTranslucency;
import java.awt.GraphicsEnvironment;
import java.awt.geom.RoundRectangle2D;
import java.net.URL;
import java.util.Objects;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import ninjabrainbot.Main;
import ninjabrainbot.event.IDisposable;
import ninjabrainbot.gui.buttons.FlatButton;
import ninjabrainbot.gui.buttons.NotificationsButton;
import ninjabrainbot.gui.buttons.TitleBarButton;
import ninjabrainbot.gui.components.RefreshWindowOnMonitorChangeListener;
import ninjabrainbot.gui.components.labels.ThemedIcon;
import ninjabrainbot.gui.components.labels.ThemedLabel;
import ninjabrainbot.gui.mainwindow.BoatIcon;
import ninjabrainbot.gui.mainwindow.eyethrows.EnderEyePanel;
import ninjabrainbot.gui.mainwindow.information.InformationListPanel;
import ninjabrainbot.gui.mainwindow.main.MainButtonPanel;
import ninjabrainbot.gui.mainwindow.main.MainTextArea;
import ninjabrainbot.gui.style.SizePreference;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.io.preferences.enums.MainViewType;
import ninjabrainbot.io.updatechecker.IUpdateChecker;
import ninjabrainbot.model.datastate.IDataState;
import ninjabrainbot.model.information.InformationMessageList;
import ninjabrainbot.model.input.IButtonInputHandler;
import ninjabrainbot.util.I18n;
import ninjabrainbot.util.Profiler;

public class NinjabrainBotFrame extends ThemedFrame implements IDisposable {

	private final NinjabrainBotPreferences preferences;

	private ThemedLabel versionTextLabel;
	private JButton settingsButton;
	private JLabel lockIcon;

	private MainTextArea mainTextArea;
	private InformationListPanel informationTextPanel;
	private EnderEyePanel enderEyePanel;

	private static final String TITLE_TEXT = I18n.get("title");
	private static final String VERSION_TEXT = "v" + Main.VERSION;

	private final StyleManager styleManager;

	public NinjabrainBotFrame(StyleManager styleManager, NinjabrainBotPreferences preferences, IUpdateChecker updateChecker, IDataState dataState, IButtonInputHandler buttonInputHandler, InformationMessageList informationMessageList) {
		super(styleManager, preferences, TITLE_TEXT);
		this.preferences = preferences;
		Profiler.start("NinjabrainBotFrame");
		setLocation(preferences.windowX.get(), preferences.windowY.get()); // Set window position
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTranslucent(preferences.translucent.get());
		setAppIcon();

		createTitleBar(styleManager, dataState, updateChecker);
		createComponents(styleManager, dataState, buttonInputHandler, informationMessageList);
		setupSubscriptions(styleManager, dataState);
		Profiler.stop();

		this.styleManager = styleManager;
	}

	@Override
	public void updateBounds(StyleManager styleManager) {
		super.updateBounds(styleManager);
		int titlewidth = styleManager.getTextWidth(TITLE_TEXT, styleManager.fontSize(styleManager.size.TEXT_SIZE_TITLE_LARGE, false));
		int titlebarHeight = titlebarPanel.getPreferredSize().height;
		versionTextLabel.setBounds(titlewidth + (titlebarHeight - styleManager.size.TEXT_SIZE_TITLE_SMALL) / 2, (styleManager.size.TEXT_SIZE_TITLE_LARGE - styleManager.size.TEXT_SIZE_TITLE_SMALL) / 2, 70, titlebarHeight);
		int versionwidth = styleManager.getTextWidth(VERSION_TEXT, styleManager.fontSize(styleManager.size.TEXT_SIZE_TITLE_SMALL, false));
		lockIcon.setBounds(titlewidth + versionwidth + (titlebarHeight - styleManager.size.TEXT_SIZE_TITLE_SMALL) / 2, 0, titlebarHeight, titlebarHeight);
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
		disposeHandler.add(preferences.translucent.whenModified().subscribeEDT(this::setTranslucent));
		disposeHandler.add(preferences.alwaysOnTop.whenModified().subscribeEDT(this::setAlwaysOnTop));
		disposeHandler.add(preferences.hotkeyMinimize.whenTriggered().subscribeEDT(__ -> toggleMinimized()));
		// Components bounds changed
		disposeHandler.add(mainTextArea.whenModified().subscribeEDT(__ -> updateSize(styleManager)));
		disposeHandler.add(informationTextPanel.whenModified().subscribeEDT(__ -> updateSize(styleManager)));
		disposeHandler.add(enderEyePanel.whenModified().subscribeEDT(__ -> updateSize(styleManager)));
		// Lock
		disposeHandler.add(dataState.locked().subscribeEDT(b -> lockIcon.setVisible(b)));
	}

	private void createTitleBar(StyleManager styleManager, IDataState dataState, IUpdateChecker updateChecker) {
		versionTextLabel = new ThemedLabel(styleManager, VERSION_TEXT) {
			@Override
			public int getTextSize(SizePreference p) {
				return p.TEXT_SIZE_TITLE_SMALL;
			}
		};
		versionTextLabel.setForegroundColor(styleManager.currentTheme.TEXT_COLOR_WEAK);
		lockIcon = new ThemedIcon(styleManager, new ImageIcon(Objects.requireNonNull(Main.class.getResource("/lock_icon.png"))));
		lockIcon.setVisible(dataState.locked().get());
		titlebarPanel.add(versionTextLabel);
		titlebarPanel.add(lockIcon);
		titlebarPanel.addButton(createMinimizeButton(styleManager));
		settingsButton = createSettingsButton(styleManager);
		titlebarPanel.addButton(settingsButton);
		NotificationsButton notificationsButton = new NotificationsButton(styleManager, this, preferences, updateChecker);
		titlebarPanel.addButton(notificationsButton);
	}

	@Override
	protected void onExitButtonClicked() {
		System.exit(0);
	}

	private void createComponents(StyleManager styleManager, IDataState dataState, IButtonInputHandler buttonInputHandler, InformationMessageList informationMessageList) {
		// Main text
		mainTextArea = new MainTextArea(styleManager, buttonInputHandler, preferences, dataState);
		add(mainTextArea);
		// Info and warnings
		informationTextPanel = new InformationListPanel(styleManager, informationMessageList);
		add(informationTextPanel);
		// "Throws" text + buttons
		MainButtonPanel mainButtonPanel = new MainButtonPanel(styleManager, buttonInputHandler);
		add(mainButtonPanel);
		// Throw panels
		enderEyePanel = new EnderEyePanel(styleManager, preferences, dataState, buttonInputHandler);
		add(enderEyePanel);
	}

	@Override
	public void validate() {
		super.validate();
		updateSize(styleManager);
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
		return new TitleBarButton(styleManager, img);
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
		ImageIcon img = new ImageIcon(Objects.requireNonNull(iconURL));
		setIconImage(img.getImage());
	}

	@Override
	public void dispose() {
		super.dispose();
		mainTextArea.dispose();
		enderEyePanel.dispose();
	}

}
