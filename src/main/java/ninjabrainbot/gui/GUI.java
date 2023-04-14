package ninjabrainbot.gui;

import ninjabrainbot.gui.frames.NinjabrainBotFrame;
import ninjabrainbot.gui.frames.OptionsFrame;
import ninjabrainbot.gui.splash.Progress;
import ninjabrainbot.gui.style.SizePreference;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.gui.style.theme.Theme;
import ninjabrainbot.io.AutoResetTimer;
import ninjabrainbot.io.ClipboardReader;
import ninjabrainbot.io.KeyboardListener;
import ninjabrainbot.io.OBSOverlay;
import ninjabrainbot.io.mcinstance.ActiveInstanceProviderFactory;
import ninjabrainbot.io.mcinstance.IActiveInstanceProvider;
import ninjabrainbot.io.preferences.NinjabrainBotOverlayImageWriter;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.model.datastate.DataStateHandler;
import ninjabrainbot.model.datastate.IDataState;
import ninjabrainbot.model.information.CombinedCertaintyInformationProvider;
import ninjabrainbot.model.information.InformationMessageList;
import ninjabrainbot.model.information.McVersionWarningProvider;
import ninjabrainbot.model.information.MismeasureWarningProvider;
import ninjabrainbot.model.information.NextThrowDirectionInformationProvider;
import ninjabrainbot.model.information.PortalLinkingWarningProvider;
import ninjabrainbot.util.Profiler;

/**
 * Main class for the user interface.
 */
public class GUI {

	private final NinjabrainBotPreferences preferences;

	private ClipboardReader clipboardReader;
	private IActiveInstanceProvider activeInstanceProvider;
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
		initInputMethods();
		initDataState();
		initDataProcessors();
		initUI();
		postInit();
	}

	private void initInputMethods() {
		Progress.setTask("Starting clipboard reader", 0.02f);
		Profiler.start("Init clipboard reader");
		clipboardReader = new ClipboardReader(preferences);
		KeyboardListener.init(clipboardReader, preferences.altClipboardReader);

		Progress.setTask("Starting instance listener", 0.03f);
		Profiler.start("Init instance listener");
		activeInstanceProvider = ActiveInstanceProviderFactory.createPlatformSpecificActiveInstanceProvider();

		Profiler.stop();
	}

	private void initDataState() {
		Progress.setTask("Creating calculator data", 0.08f);
		Profiler.start("Init DataState");
		dataStateHandler = new DataStateHandler(preferences, clipboardReader, activeInstanceProvider);
		dataState = dataStateHandler.getDataState();
		Profiler.stop();
	}

	private void initDataProcessors() {
		Progress.setTask("Initializing information message generators", 0.09f);
		Profiler.start("Init info message generators");
		informationMessageList = new InformationMessageList();
		informationMessageList.AddInformationMessageProvider(new McVersionWarningProvider(activeInstanceProvider, preferences));
		informationMessageList.AddInformationMessageProvider(new MismeasureWarningProvider(dataState, preferences));
		informationMessageList.AddInformationMessageProvider(new PortalLinkingWarningProvider(dataState, preferences));
		informationMessageList.AddInformationMessageProvider(new CombinedCertaintyInformationProvider(dataState, preferences));
		informationMessageList.AddInformationMessageProvider(new NextThrowDirectionInformationProvider(dataState, preferences));
		Profiler.stop();
	}

	private void initUI() {
		Progress.setTask("Loading themes", 0.30f);
		Profiler.start("Init StyleManager");
		Theme.loadThemes(preferences);
		styleManager = new StyleManager(Theme.get(preferences.theme.get()), SizePreference.get(preferences.size.get()));
		preferences.size.whenModified().subscribeEDT(size -> styleManager.setSizePreference(SizePreference.get(size)));
		preferences.theme.whenModified().subscribeEDT(theme_uid -> styleManager.currentTheme.setTheme(Theme.get(theme_uid)));

		Progress.setTask("Creating main window", 0.93f);
		Profiler.stopAndStart("Create frame");
		ninjabrainBotFrame = new NinjabrainBotFrame(styleManager, preferences, dataState, dataStateHandler.buttonInputHandler, informationMessageList);

		Progress.setTask("Creating settings window", 0.95f);
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

		new Thread(clipboardReader, "Clipboard reader").start();

		autoResetTimer = new AutoResetTimer(dataState, dataStateHandler.domainModel, dataStateHandler.actionExecutor);
		preferences.autoReset.whenModified().subscribeEDT(b -> autoResetTimer.setAutoResetEnabled(b));

		obsOverlay = new OBSOverlay(ninjabrainBotFrame, preferences, dataState, dataStateHandler.domainModel, new NinjabrainBotOverlayImageWriter());

		ninjabrainBotFrame.checkIfOffScreen();
		ninjabrainBotFrame.setVisible(true);

		Runtime.getRuntime().addShutdownHook(onShutdown());
		Profiler.stop();
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
				informationMessageList.dispose();
				dataStateHandler.dispose();
			}
		};
	}

}
