package ninjabrainbot.gui;

import ninjabrainbot.event.DisposeHandler;
import ninjabrainbot.gui.frames.NinjabrainBotFrame;
import ninjabrainbot.gui.frames.OptionsFrame;
import ninjabrainbot.gui.splash.Progress;
import ninjabrainbot.gui.style.SizePreference;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.gui.style.theme.Theme;
import ninjabrainbot.io.AutoResetTimer;
import ninjabrainbot.io.ClipboardReader;
import ninjabrainbot.io.GithubUpdateChecker;
import ninjabrainbot.io.KeyboardListener;
import ninjabrainbot.io.OBSOverlay;
import ninjabrainbot.io.mcinstance.ActiveInstanceProviderFactory;
import ninjabrainbot.io.mcinstance.IActiveInstanceProvider;
import ninjabrainbot.io.preferences.NinjabrainBotOverlayImageWriter;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.model.actions.ActionExecutor;
import ninjabrainbot.model.actions.IActionExecutor;
import ninjabrainbot.model.datastate.DataState;
import ninjabrainbot.model.datastate.endereye.CoordinateInputSource;
import ninjabrainbot.model.datastate.endereye.EnderEyeThrowFactory;
import ninjabrainbot.model.datastate.endereye.IEnderEyeThrowFactory;
import ninjabrainbot.model.domainmodel.DomainModel;
import ninjabrainbot.model.environmentstate.EnvironmentState;
import ninjabrainbot.model.information.CombinedCertaintyInformationProvider;
import ninjabrainbot.model.information.InformationMessageList;
import ninjabrainbot.model.information.McVersionWarningProvider;
import ninjabrainbot.model.information.MismeasureWarningProvider;
import ninjabrainbot.model.information.NextThrowDirectionInformationProvider;
import ninjabrainbot.model.information.PortalLinkingWarningProvider;
import ninjabrainbot.model.input.ActiveInstanceInputHandler;
import ninjabrainbot.model.input.ButtonInputHandler;
import ninjabrainbot.model.input.FossilInputHandler;
import ninjabrainbot.model.input.HotkeyInputHandler;
import ninjabrainbot.model.input.IButtonInputHandler;
import ninjabrainbot.model.input.PlayerPositionInputHandler;
import ninjabrainbot.util.Profiler;

/**
 * Main class for the user interface.
 */
public class GUI {

	private final NinjabrainBotPreferences preferences;

	private ClipboardReader clipboardReader;
	private IActiveInstanceProvider activeInstanceProvider;
	private AutoResetTimer autoResetTimer;

	private StyleManager styleManager;
	private NinjabrainBotFrame ninjabrainBotFrame;
	private OptionsFrame optionsFrame;

	private DomainModel domainModel;
	private IActionExecutor actionExecutor;
	private EnvironmentState environmentState;
	private DataState dataState;

	public IButtonInputHandler buttonInputHandler;

	private InformationMessageList informationMessageList;

	private OBSOverlay obsOverlay;

	private final DisposeHandler disposeHandler = new DisposeHandler();

	public GUI(NinjabrainBotPreferences preferences) {
		this.preferences = preferences;
		initInputMethods();
		initDataState();
		initDataProcessors();
		initInputHandlers();
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
		Progress.setTask("Creating calculator data", 0.07f);
		Profiler.start("Init DataState");
		domainModel = disposeHandler.add(new DomainModel());
		actionExecutor = new ActionExecutor(domainModel);
		environmentState = disposeHandler.add(new EnvironmentState(domainModel, preferences));
		dataState = disposeHandler.add(new DataState(domainModel, environmentState));
		Profiler.stop();
	}

	private void initInputHandlers() {
		Progress.setTask("Initializing input handlers", 0.08f);
		CoordinateInputSource coordinateInputSource = disposeHandler.add(new CoordinateInputSource(clipboardReader));
		IEnderEyeThrowFactory enderEyeThrowFactory = new EnderEyeThrowFactory(preferences, dataState.boatDataState());
		disposeHandler.add(new PlayerPositionInputHandler(coordinateInputSource, dataState, actionExecutor, preferences, enderEyeThrowFactory));
		disposeHandler.add(new FossilInputHandler(coordinateInputSource, dataState, actionExecutor));
		disposeHandler.add(new ActiveInstanceInputHandler(activeInstanceProvider, domainModel, dataState, environmentState, actionExecutor, preferences));
		disposeHandler.add(new HotkeyInputHandler(preferences, domainModel, dataState, actionExecutor));
		buttonInputHandler = new ButtonInputHandler(domainModel, dataState, actionExecutor);
	}

	private void initDataProcessors() {
		Progress.setTask("Initializing information message generators", 0.09f);
		Profiler.start("Init info message generators");
		informationMessageList = new InformationMessageList();
		informationMessageList.AddInformationMessageProvider(new McVersionWarningProvider(activeInstanceProvider, preferences));
		informationMessageList.AddInformationMessageProvider(new MismeasureWarningProvider(dataState, environmentState, preferences));
		informationMessageList.AddInformationMessageProvider(new PortalLinkingWarningProvider(dataState, preferences));
		informationMessageList.AddInformationMessageProvider(new CombinedCertaintyInformationProvider(dataState, preferences));
		informationMessageList.AddInformationMessageProvider(new NextThrowDirectionInformationProvider(dataState, environmentState, preferences));
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
		ninjabrainBotFrame = new NinjabrainBotFrame(styleManager, preferences, new GithubUpdateChecker(), dataState, buttonInputHandler, informationMessageList);

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

		autoResetTimer = new AutoResetTimer(dataState, domainModel, actionExecutor);
		preferences.autoReset.whenModified().subscribeEDT(b -> autoResetTimer.setAutoResetEnabled(b));

		obsOverlay = new OBSOverlay(ninjabrainBotFrame, preferences, dataState, domainModel, new NinjabrainBotOverlayImageWriter(), 1000);

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
				disposeHandler.dispose();
				obsOverlay.dispose();
				autoResetTimer.dispose();
				informationMessageList.dispose();
			}
		};
	}

}
