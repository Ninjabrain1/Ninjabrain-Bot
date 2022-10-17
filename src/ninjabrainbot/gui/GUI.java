package ninjabrainbot.gui;

import ninjabrainbot.Main;
import ninjabrainbot.data.DataStateHandler;
import ninjabrainbot.data.IDataState;
import ninjabrainbot.gui.frames.NinjabrainBotFrame;
import ninjabrainbot.gui.frames.OptionsFrame;
import ninjabrainbot.gui.splash.Progress;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.io.AutoResetTimer;
import ninjabrainbot.io.ClipboardReader;
import ninjabrainbot.io.KeyboardListener;
import ninjabrainbot.io.OBSOverlay;
import ninjabrainbot.util.Profiler;

/**
 * Main class for the user interface.
 */
public class GUI {

	private ClipboardReader clipboardReader;
	private AutoResetTimer autoResetTimer;
	private OBSOverlay obsOverlay;

	private StyleManager styleManager;
	private NinjabrainBotFrame ninjabrainBotFrame;
	private OptionsFrame optionsFrame;

	private DataStateHandler dataStateHandler;
	private IDataState dataState;

	public GUI() {
		initDataState();
		initInputMethods();
		initUI();
		postInit();
	}

	private void initDataState() {
		Progress.setTask("Creating calculator data", 0.01f);
		Profiler.start("Init DataState");
		dataStateHandler = new DataStateHandler();
		dataState = dataStateHandler.getDataState();
		Profiler.stop();
	}

	private void initInputMethods() {
		Progress.setTask("Starting clipboard reader", 0.02f);
		Profiler.start("Init clipboard reader");
		clipboardReader = new ClipboardReader(dataStateHandler.getModificationLock());
		dataStateHandler.addThrowStream(clipboardReader.whenNewThrowInputed());
		dataStateHandler.addFossilStream(clipboardReader.whenNewFossilInputed());
		Thread clipboardThread = new Thread(clipboardReader, "Clipboard reader");
		KeyboardListener.init(clipboardReader);
		clipboardThread.start();

		Profiler.stopAndStart("Setup hotkeys");
		setupHotkeys();
		Profiler.stop();
	}

	private void initUI() {
		Progress.setTask("Loading themes", 0.15f);
		Profiler.start("Init StyleManager");
		styleManager = new StyleManager();

		Progress.setTask("Creating main window", 0.65f);
		Profiler.stopAndStart("Create frame");
		ninjabrainBotFrame = new NinjabrainBotFrame(styleManager, dataState, dataStateHandler);

		Progress.setTask("Creating settings window", 0.85f);
		Profiler.stopAndStart("Create settings window");
		optionsFrame = new OptionsFrame(styleManager);
		ninjabrainBotFrame.getSettingsButton().addActionListener(__ -> optionsFrame.toggleWindow(ninjabrainBotFrame));

		Progress.setTask("Settings fonts and colors", 0.99f);
		Profiler.stopAndStart("Init fonts, colors, bounds");
		styleManager.init();
		Profiler.stop();
	}

	private void postInit() {
		Progress.setTask("Finishing up gui", 1f);
		Profiler.start("Post init");
		ninjabrainBotFrame.checkIfOffScreen();
		autoResetTimer = new AutoResetTimer(dataState, dataStateHandler);
		obsOverlay = new OBSOverlay(ninjabrainBotFrame, dataStateHandler);
		ninjabrainBotFrame.setVisible(true);
		Runtime.getRuntime().addShutdownHook(onShutdown());
		Profiler.stop();
	}

	private void setupHotkeys() {
		Main.preferences.hotkeyReset.whenTriggered().subscribe(__ -> dataStateHandler.resetIfNotLocked());
		Main.preferences.hotkeyUndo.whenTriggered().subscribe(__ -> dataStateHandler.undoIfNotLocked());
		Main.preferences.hotkeyIncrement.whenTriggered().subscribe(__ -> dataStateHandler.changeLastAngleIfNotLocked(0.01));
		Main.preferences.hotkeyDecrement.whenTriggered().subscribe(__ -> dataStateHandler.changeLastAngleIfNotLocked(-0.01));
		Main.preferences.hotkeyAltStd.whenTriggered().subscribe(__ -> dataStateHandler.toggleAltStdOnLastThrowIfNotLocked());
		Main.preferences.hotkeyLock.whenTriggered().subscribe(__ -> dataStateHandler.toggleLocked());
	}

	private Thread onShutdown() {
		return new Thread("Shutdown") {
			@Override
			public void run() {
				Main.preferences.windowX.set(ninjabrainBotFrame.getX());
				Main.preferences.windowY.set(ninjabrainBotFrame.getY());
				obsOverlay.dispose();
				autoResetTimer.dispose();
				dataStateHandler.dispose();
			}
		};
	}

}
