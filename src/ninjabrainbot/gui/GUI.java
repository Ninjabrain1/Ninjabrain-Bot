package ninjabrainbot.gui;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javax.swing.JFrame;
import javax.swing.Timer;

import ninjabrainbot.Main;
import ninjabrainbot.calculator.Calculator;
import ninjabrainbot.calculator.DataState;
import ninjabrainbot.calculator.DataStateHandler;
import ninjabrainbot.calculator.IDataState;
import ninjabrainbot.calculator.IDataStateHandler;
import ninjabrainbot.calculator.StandardStdProfile;
import ninjabrainbot.gui.components.NinjabrainBotFrame;
import ninjabrainbot.io.AutoResetTimer;
import ninjabrainbot.io.ClipboardReader;
import ninjabrainbot.io.KeyboardListener;
import ninjabrainbot.util.Profiler;

/**
 * Main class for the user interface.
 */
public class GUI {

	private ClipboardReader clipboardReader;
	private Timer autoResetTimer;

	private StyleManager styleManager;
	private NinjabrainBotFrame ninjabrainBotFrame;
	private OptionsFrame optionsFrame;

	private IDataState dataState;
	private IDataStateHandler dataStateHandler;

	public GUI() {
		initInputMethods();
		initDataState();
		initWindows();
		postInit();
	}

	private void initInputMethods() {
		clipboardReader = new ClipboardReader();
		Thread clipboardThread = new Thread(clipboardReader);
		KeyboardListener.init(clipboardReader);
		clipboardThread.start();

		setupHotkeys();
		setupSettingsSubscriptions();
	}

	private void initDataState() {
		dataState = new DataState(new Calculator(), clipboardReader.whenNewThrowInputed(), clipboardReader.whenNewFossilInputed(), new StandardStdProfile());
		dataStateHandler = new DataStateHandler(dataState);
	}

	private void initWindows() {
		styleManager = new StyleManager();
		Profiler.stopAndStart("Create frame");
		ninjabrainBotFrame = new NinjabrainBotFrame(styleManager, dataState, dataStateHandler);

		Profiler.stopAndStart("Create settings window");
		optionsFrame = new OptionsFrame(styleManager);
		ninjabrainBotFrame.getSettingsButton().addActionListener(__ -> optionsFrame.toggleWindow(ninjabrainBotFrame));

		Profiler.stopAndStart("Init fonts, colors, bounds");
		styleManager.init();
		ninjabrainBotFrame.setVisible(true);
	}

	private void postInit() {
		checkIfOffScreen(ninjabrainBotFrame);
		autoResetTimer = new AutoResetTimer(dataState, dataStateHandler);
		Runtime.getRuntime().addShutdownHook(onShutdown());
	}

	private void setupHotkeys() {
		Main.preferences.hotkeyReset.whenTriggered().subscribe(__ -> dataStateHandler.resetIfNotLocked());
		Main.preferences.hotkeyUndo.whenTriggered().subscribe(__ -> dataStateHandler.undoIfNotLocked());
		Main.preferences.hotkeyIncrement.whenTriggered().subscribe(__ -> dataStateHandler.changeLastAngleIfNotLocked(0.01));
		Main.preferences.hotkeyDecrement.whenTriggered().subscribe(__ -> dataStateHandler.changeLastAngleIfNotLocked(-0.01));
		Main.preferences.hotkeyAltStd.whenTriggered().subscribe(__ -> dataStateHandler.toggleAltStdOnLastThrowIfNotLocked());
		Main.preferences.hotkeyLock.whenTriggered().subscribe(__ -> dataState.toggleLocked());
	}

	private void setupSettingsSubscriptions() {
		Main.preferences.autoReset.whenModified().subscribe(b -> onAutoResetEnabledChanged(b));
	}

	private void onAutoResetEnabledChanged(boolean b) {
		if (b)
			autoResetTimer.start();
		else
			autoResetTimer.stop();
	}

	private void checkIfOffScreen(JFrame frame) {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		for (GraphicsDevice gd : ge.getScreenDevices()) {
			if (gd.getDefaultConfiguration().getBounds().contains(frame.getBounds())) {
				return;
			}
		}
		frame.setLocation(100, 100);
	}

	private Thread onShutdown() {
		return new Thread() {
			@Override
			public void run() {
				Main.preferences.windowX.set(ninjabrainBotFrame.getX());
				Main.preferences.windowY.set(ninjabrainBotFrame.getY());
//				clearOBSOverlay(); TODO
			}
		};
	}

}
