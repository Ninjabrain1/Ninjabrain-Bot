package ninjabrainbot.gui;

import ninjabrainbot.data.DataStateHandler;
import ninjabrainbot.data.IDataState;
import ninjabrainbot.data.information.InformationMessageList;
import ninjabrainbot.data.information.McVersionWarningProvider;
import ninjabrainbot.gui.frames.NinjabrainBotFrame;
import ninjabrainbot.gui.frames.OptionsFrame;
import ninjabrainbot.gui.splash.Progress;
import ninjabrainbot.gui.style.SizePreference;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.gui.style.Theme;
import ninjabrainbot.io.ActiveInstanceListener;
import ninjabrainbot.io.AutoResetTimer;
import ninjabrainbot.io.ClipboardReader;
import ninjabrainbot.io.KeyboardListener;
import ninjabrainbot.io.OBSOverlay;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.util.Profiler;

/**
 * Main class for the user interface.
 */
public class GUI {

	private NinjabrainBotPreferences preferences;

	private ClipboardReader clipboardReader;
	private ActiveInstanceListener activeInstanceListener;
	private AutoResetTimer autoResetTimer;
	private OBSOverlay obsOverlay;

	private StyleManager styleManager;
	private NinjabrainBotFrame ninjabrainBotFrame;
	private OptionsFrame optionsFrame;

	private DataStateHandler dataStateHandler;
	private IDataState dataState;

	private InformationMessageList informationMessageList;

	public GUI(NinjabrainBotPreferences preferences) {
		this.preferences = preferences;
		initDataState();
		initInputMethods();
		initDataProcessors();
		initUI();
		postInit();
	}

	private void initDataState() {
		Progress.setTask("Creating calculator data", 0.01f);
		Profiler.start("Init DataState");
		dataStateHandler = new DataStateHandler(preferences);
		dataState = dataStateHandler.getDataState();
		Profiler.stop();
	}

	private void initInputMethods() {
		Progress.setTask("Starting clipboard reader", 0.02f);
		Profiler.start("Init clipboard reader");
		clipboardReader = new ClipboardReader(preferences, dataStateHandler.getModificationLock());
		dataStateHandler.addThrowStream(clipboardReader.whenNewThrowInputed());
		dataStateHandler.addFossilStream(clipboardReader.whenNewFossilInputed());
		Thread clipboardThread = new Thread(clipboardReader, "Clipboard reader");
		KeyboardListener.init(clipboardReader, preferences.altClipboardReader);
		clipboardThread.start();

		Progress.setTask("Starting instance listener", 0.03f);
		Profiler.start("Init instance listener");
		activeInstanceListener = new ActiveInstanceListener();
		activeInstanceListener.whenActiveMinecraftInstanceChanged().subscribe(__ -> dataStateHandler.resetIfNotLocked());
		Thread activeInstanceListenerThread = new Thread(activeInstanceListener, "Active instance listener");
		activeInstanceListenerThread.start();

		Profiler.stopAndStart("Setup hotkeys");
		setupHotkeys();
		Profiler.stop();
	}

	private void initDataProcessors() {
		Progress.setTask("Initializing information message generators", 0.04f);
		Profiler.start("Init info message generators");
		informationMessageList = new InformationMessageList();
		informationMessageList.AddInformationMessageProvider(new McVersionWarningProvider(activeInstanceListener, preferences));
		Profiler.stop();
	}

	private void initUI() {
		Progress.setTask("Loading themes", 0.15f);
		Profiler.start("Init StyleManager");
		Theme.loadThemes(preferences);
		styleManager = new StyleManager(Theme.get(preferences.theme.get()), SizePreference.get(preferences.size.get()));
		preferences.size.whenModified().subscribe(size -> styleManager.setSizePreference(SizePreference.get(size)));
		preferences.theme.whenModified().subscribe(theme_uid -> styleManager.currentTheme.setTheme(Theme.get(theme_uid)));

		Progress.setTask("Creating main window", 0.65f);
		Profiler.stopAndStart("Create frame");
		ninjabrainBotFrame = new NinjabrainBotFrame(styleManager, preferences, dataStateHandler, informationMessageList);

		Progress.setTask("Creating settings window", 0.85f);
		Profiler.stopAndStart("Create settings window");
		ninjabrainBotFrame.getSettingsButton().addActionListener(__ -> getOrCreateOptionsFrame().toggleWindow(ninjabrainBotFrame));

		Progress.setTask("Settings fonts and colors", 0.99f);
		Profiler.stopAndStart("Init fonts, colors, bounds");
		styleManager.init();
		Profiler.stop();
	}

	private void postInit() {
		Progress.setTask("Finishing up gui", 1f);
		Profiler.start("Post init");

		autoResetTimer = new AutoResetTimer(dataState, dataStateHandler);
		preferences.autoReset.whenModified().subscribe(b -> autoResetTimer.setAutoResetEnabled(b));

		obsOverlay = new OBSOverlay(ninjabrainBotFrame, preferences, dataStateHandler);

		ninjabrainBotFrame.checkIfOffScreen();
		ninjabrainBotFrame.setVisible(true);

		Runtime.getRuntime().addShutdownHook(onShutdown());
		Profiler.stop();
	}

	private void setupHotkeys() {
		preferences.hotkeyReset.whenTriggered().subscribe(__ -> dataStateHandler.resetIfNotLocked());
		preferences.hotkeyUndo.whenTriggered().subscribe(__ -> dataStateHandler.undoIfNotLocked());
		preferences.hotkeyIncrement.whenTriggered().subscribe(__ -> dataStateHandler.changeLastAngleIfNotLocked(0.01));
		preferences.hotkeyDecrement.whenTriggered().subscribe(__ -> dataStateHandler.changeLastAngleIfNotLocked(-0.01));
		preferences.hotkeyAltStd.whenTriggered().subscribe(__ -> dataStateHandler.toggleAltStdOnLastThrowIfNotLocked());
		preferences.hotkeyLock.whenTriggered().subscribe(__ -> dataStateHandler.toggleLocked());
	}

	private OptionsFrame getOrCreateOptionsFrame() {
		if (optionsFrame == null) {
			optionsFrame = new OptionsFrame(styleManager, preferences);
			styleManager.init();
		}
		return optionsFrame;
	}

	private Thread onShutdown() {
		return new Thread("Shutdown") {
			@Override
			public void run() {
				preferences.windowX.set(ninjabrainBotFrame.getX());
				preferences.windowY.set(ninjabrainBotFrame.getY());
				obsOverlay.dispose();
				autoResetTimer.dispose();
				dataStateHandler.dispose();
			}
		};
	}

}
