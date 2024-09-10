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
import ninjabrainbot.io.KeyboardListener;
import ninjabrainbot.io.api.NinjabrainBotHttpServer;
import ninjabrainbot.io.mcinstance.ActiveInstanceProviderFactory;
import ninjabrainbot.io.mcinstance.IActiveInstanceProvider;
import ninjabrainbot.io.overlay.NinjabrainBotOverlayImageWriter;
import ninjabrainbot.io.overlay.OBSOverlay;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.io.savestate.TempFileAccessor;
import ninjabrainbot.io.updatechecker.GithubUpdateChecker;
import ninjabrainbot.model.ModelState;
import ninjabrainbot.model.actions.IActionExecutor;
import ninjabrainbot.model.datastate.IDataState;
import ninjabrainbot.model.datastate.calibrator.CalibratorFactory;
import ninjabrainbot.model.datastate.endereye.CoordinateInputSource;
import ninjabrainbot.model.datastate.endereye.EnderEyeThrowFactory;
import ninjabrainbot.model.datastate.endereye.IEnderEyeThrowFactory;
import ninjabrainbot.model.domainmodel.DomainModelImportExportService;
import ninjabrainbot.model.domainmodel.IDomainModel;
import ninjabrainbot.model.environmentstate.IEnvironmentState;
import ninjabrainbot.model.information.CombinedCertaintyInformationProvider;
import ninjabrainbot.model.information.InformationMessageList;
import ninjabrainbot.model.information.McVersionWarningProvider;
import ninjabrainbot.model.information.MismeasureWarningProvider;
import ninjabrainbot.model.information.NextThrowDirectionInformationProvider;
import ninjabrainbot.model.information.PortalLinkingWarningProvider;
import ninjabrainbot.model.input.ActiveInstanceInputHandler;
import ninjabrainbot.model.input.ButtonInputHandler;
import ninjabrainbot.model.input.F3ILocationInputHandler;
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

	private IDomainModel domainModel;
	private IActionExecutor actionExecutor;
	private IEnvironmentState environmentState;
	private IDataState dataState;
	private DomainModelImportExportService domainModelImportExportService;

	private CoordinateInputSource coordinateInputSource;
	private IButtonInputHandler buttonInputHandler;

	private InformationMessageList informationMessageList;

	private OBSOverlay obsOverlay;
	private NinjabrainBotHttpServer ninjabrainBotHttpServer;

	private final DisposeHandler disposeHandler = new DisposeHandler();

	public GUI(NinjabrainBotPreferences preferences) {
		this.preferences = preferences;
		initInputMethods();
		initModel();
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

	private void initModel() {
		Progress.setTask("Creating calculator data", 0.07f);
		Profiler.start("Init DataState");
		ModelState modelState = disposeHandler.add(new ModelState(preferences));
		domainModel = modelState.domainModel;
		actionExecutor = modelState.actionExecutor;
		environmentState = modelState.environmentState;
		dataState = modelState.dataState;
		domainModelImportExportService = new DomainModelImportExportService(domainModel, new TempFileAccessor("NinjabrainBot-save-state.txt"), preferences);
		domainModelImportExportService.triggerDeserialization();
		domainModel.deleteHistory();
		Profiler.stop();
	}

	private void initInputHandlers() {
		Progress.setTask("Initializing input handlers", 0.08f);
		coordinateInputSource = disposeHandler.add(new CoordinateInputSource(clipboardReader));
		IEnderEyeThrowFactory enderEyeThrowFactory = new EnderEyeThrowFactory(preferences, dataState.boatDataState());
		disposeHandler.add(new PlayerPositionInputHandler(coordinateInputSource, dataState, actionExecutor, preferences, enderEyeThrowFactory));
		disposeHandler.add(new F3ILocationInputHandler(coordinateInputSource, dataState, actionExecutor, preferences));
		disposeHandler.add(new ActiveInstanceInputHandler(activeInstanceProvider, domainModel, dataState, actionExecutor, preferences));
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

		autoResetTimer = new AutoResetTimer(dataState, domainModel, actionExecutor, preferences);

		obsOverlay = new OBSOverlay(ninjabrainBotFrame, preferences, dataState, domainModel, new NinjabrainBotOverlayImageWriter(), 1000);
		ninjabrainBotHttpServer = new NinjabrainBotHttpServer(dataState, domainModel, preferences);

		ninjabrainBotFrame.checkIfOffScreen();
		ninjabrainBotFrame.setVisible(true);

		Runtime.getRuntime().addShutdownHook(onShutdown());
		Profiler.stop();
	}

	private OptionsFrame getOrCreateOptionsFrame() {
		if (optionsFrame == null) {
			optionsFrame = new OptionsFrame(styleManager, preferences, new CalibratorFactory(environmentState.calculatorSettings(), coordinateInputSource, preferences), activeInstanceProvider, actionExecutor);
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
				domainModelImportExportService.onShutdown();
				disposeHandler.dispose();
				obsOverlay.dispose();
				autoResetTimer.dispose();
				informationMessageList.dispose();
				ninjabrainBotHttpServer.dispose();
			}
		};
	}

}
