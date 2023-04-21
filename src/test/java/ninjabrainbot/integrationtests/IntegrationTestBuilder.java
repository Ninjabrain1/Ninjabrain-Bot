package ninjabrainbot.integrationtests;

import ninjabrainbot.event.DisposeHandler;
import ninjabrainbot.gui.frames.NinjabrainBotFrame;
import ninjabrainbot.gui.mainwindow.BoatIcon;
import ninjabrainbot.gui.mainwindow.main.MainTextArea;
import ninjabrainbot.gui.mainwindow.main.MainTextAreaTestAdapter;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.io.mcinstance.IMinecraftWorldFile;
import ninjabrainbot.io.mcinstance.MinecraftInstance;
import ninjabrainbot.io.preferences.HotkeyPreference;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.io.preferences.UnsavedPreferences;
import ninjabrainbot.io.preferences.enums.AllAdvancementsToggleType;
import ninjabrainbot.io.preferences.enums.MainViewType;
import ninjabrainbot.io.preferences.enums.StrongholdDisplayType;
import ninjabrainbot.model.ModelState;
import ninjabrainbot.model.actions.IActionExecutor;
import ninjabrainbot.model.actions.common.ResetAction;
import ninjabrainbot.model.datastate.IDataState;
import ninjabrainbot.model.datastate.common.IDetailedPlayerPosition;
import ninjabrainbot.model.datastate.divine.Fossil;
import ninjabrainbot.model.datastate.endereye.CoordinateInputSource;
import ninjabrainbot.model.datastate.endereye.EnderEyeThrowFactory;
import ninjabrainbot.model.datastate.endereye.IEnderEyeThrowFactory;
import ninjabrainbot.model.domainmodel.IDomainModel;
import ninjabrainbot.model.environmentstate.IEnvironmentState;
import ninjabrainbot.model.information.InformationMessageList;
import ninjabrainbot.model.input.ActiveInstanceInputHandler;
import ninjabrainbot.model.input.ButtonInputHandler;
import ninjabrainbot.model.input.FossilInputHandler;
import ninjabrainbot.model.input.HotkeyInputHandler;
import ninjabrainbot.model.input.PlayerPositionInputHandler;
import ninjabrainbot.util.Assert;
import ninjabrainbot.util.FakeCoordinateInputSource;
import ninjabrainbot.util.FakeMinecraftWorldFile;
import ninjabrainbot.util.FakeUpdateChecker;
import ninjabrainbot.util.MockedClipboardReader;
import ninjabrainbot.util.MockedInstanceProvider;
import ninjabrainbot.util.TestTheme2;
import ninjabrainbot.util.TestUtils;

public class IntegrationTestBuilder {

	public final NinjabrainBotPreferences preferences;
	public final IDomainModel domainModel;

	public final IActionExecutor actionExecutor;
	public final IEnvironmentState environmentState;
	public final IDataState dataState;

	private CoordinateInputSource coordinateInputSource;
	private FakeCoordinateInputSource fakeCoordinateInputSource;
	private MockedClipboardReader clipboardReader;
	private MockedInstanceProvider activeInstanceProvider;

	private PlayerPositionInputHandler playerPositionInputHandler;
	private PlayerPositionInputHandler fakePlayerPositionInputHandler;
	private FossilInputHandler fossilInputHandler;
	private FossilInputHandler fakeFossilInputHandler;
	private HotkeyInputHandler hotkeyInputHandler;
	private ButtonInputHandler buttonInputHandler;
	private ActiveInstanceInputHandler activeInstanceInputHandler;

	private StyleManager styleManager;

	public IntegrationTestBuilder() {
		preferences = new NinjabrainBotPreferences(new UnsavedPreferences());
		ModelState modelState = new ModelState(preferences);
		domainModel = modelState.domainModel;
		actionExecutor = modelState.actionExecutor;
		environmentState = modelState.environmentState;
		dataState = modelState.dataState;
	}

	public IntegrationTestBuilder withProSettings() {
		preferences.sigma.set(0.005f);
		preferences.view.set(MainViewType.DETAILED);
		preferences.strongholdDisplayType.set(StrongholdDisplayType.CHUNK);
		preferences.useAltStd.set(true);
		return this;
	}

	public IntegrationTestBuilder withAllAdvancementsSettings() {
		preferences.allAdvancements.set(true);
		preferences.allAdvancementsToggleType.set(AllAdvancementsToggleType.Automatic);
		return this;
	}

	public IntegrationTestBuilder withObsOverlaySettings() {
		preferences.overlayHideDelay.set(10);
		preferences.overlayAutoHide.set(true);
		preferences.overlayHideWhenLocked.set(true);
		preferences.useOverlay.set(true);
		return this;
	}

	public IntegrationTestBuilder withBoatSettings() {
		preferences.sigmaBoat.set(0.001f);
		preferences.sensitivity.set(0.204225346f);
		preferences.resolutionHeight.set(16384);
		preferences.useTallRes.set(true);
		preferences.usePreciseAngle.set(true);
		preferences.view.set(MainViewType.DETAILED);
		preferences.strongholdDisplayType.set(StrongholdDisplayType.CHUNK);
		return this;
	}

	public void setClipboard(String clipboardString) {
		if (clipboardReader == null) clipboardReader = new MockedClipboardReader();
		if (coordinateInputSource == null) coordinateInputSource = new CoordinateInputSource(clipboardReader);
		if (playerPositionInputHandler == null) playerPositionInputHandler = createPlayerPositionInputHandler();
		if (fossilInputHandler == null) fossilInputHandler = new FossilInputHandler(coordinateInputSource, dataState, actionExecutor);
		clipboardReader.setClipboard(clipboardString);
	}

	public void inputSubpixelCorrections(int correctionCount) {
		for (int i = 0; i < correctionCount; i++) {
			triggerHotkey(preferences.hotkeyIncrement);
		}
		for (int i = 0; i < -correctionCount; i++) {
			triggerHotkey(preferences.hotkeyDecrement);
		}
	}

	public void inputStandardDeviationToggle() {
		triggerHotkey(preferences.hotkeyAltStd);
	}

	public void triggerHotkey(HotkeyPreference hotkeyPreference) {
		if (hotkeyInputHandler == null) hotkeyInputHandler = new HotkeyInputHandler(preferences, domainModel, dataState, actionExecutor);
		hotkeyPreference.execute();
	}

	public void setActiveMinecraftWorld(IMinecraftWorldFile minecraftWorld) {
		if (activeInstanceProvider == null) activeInstanceProvider = new MockedInstanceProvider();
		if (activeInstanceInputHandler == null) activeInstanceInputHandler = new ActiveInstanceInputHandler(activeInstanceProvider, domainModel, dataState, environmentState, actionExecutor, preferences);
		activeInstanceProvider.activeMinecraftWorld().set(minecraftWorld);
	}

	public MainTextAreaTestAdapter createMainTextArea() {
		if (styleManager == null) styleManager = TestUtils.createStyleManager();
		if (buttonInputHandler == null) buttonInputHandler = new ButtonInputHandler(domainModel, dataState, actionExecutor);
		return new MainTextAreaTestAdapter(new MainTextArea(styleManager, buttonInputHandler, preferences, dataState));
	}

	public NinjabrainBotFrame createNinjabrainBotFrame() {
		if (styleManager == null) styleManager = TestUtils.createStyleManager();
		if (buttonInputHandler == null) buttonInputHandler = new ButtonInputHandler(domainModel, dataState, actionExecutor);
		NinjabrainBotFrame frame = new NinjabrainBotFrame(styleManager, preferences, new FakeUpdateChecker(), dataState, buttonInputHandler, new InformationMessageList());
		styleManager.init();
		return frame;
	}

	public BoatIcon createBoatIcon() {
		if (styleManager == null) styleManager = TestUtils.createStyleManager();
		return new BoatIcon(styleManager, dataState.boatDataState().boatState(), preferences, new DisposeHandler());
	}

	public void swapTheme() {
		Assert.isNotNull(styleManager, "Create something that uses a StyleManager first!");
		styleManager.currentTheme.setTheme(new TestTheme2());
	}

	public void addDummyEnderEyeThrow() {
		TestUtils.addDummyEnderEyeThrow(domainModel, dataState);
	}

	public void inputDetailedPlayerPosition(IDetailedPlayerPosition detailedPlayerPosition) {
		if (fakePlayerPositionInputHandler == null)
			fakePlayerPositionInputHandler = createFakePlayerPositionInputHandler();
		fakeCoordinateInputSource.whenNewDetailedPlayerPositionInputted.notifySubscribers(detailedPlayerPosition);
	}

	public void inputFossil(Fossil fossil) {
		if (fakeCoordinateInputSource == null)
			fakeCoordinateInputSource = new FakeCoordinateInputSource();
		if (fakeFossilInputHandler == null)
			fakeFossilInputHandler = new FossilInputHandler(fakeCoordinateInputSource, dataState, actionExecutor);
		fakeCoordinateInputSource.whenNewFossilInputted.notifySubscribers(fossil);
	}

	public void resetCalculator() {
		actionExecutor.executeImmediately(new ResetAction(domainModel));
	}

	public void enterNewWorld() {
		setActiveMinecraftWorld(new FakeMinecraftWorldFile(new MinecraftInstance("panda"), "gargamel", false));
	}

	public void enterEnd() {
		if (activeInstanceProvider.activeMinecraftWorld().get() == null)
			setActiveMinecraftWorld(new FakeMinecraftWorldFile(new MinecraftInstance("instance 1"), "world1", false));
		var world = activeInstanceProvider.activeMinecraftWorld().get();
		setActiveMinecraftWorld(new FakeMinecraftWorldFile(world.minecraftInstance(), world.name(), true));
	}

	private PlayerPositionInputHandler createPlayerPositionInputHandler() {
		if (coordinateInputSource == null)
			coordinateInputSource = new CoordinateInputSource(clipboardReader);
		IEnderEyeThrowFactory enderEyeThrowFactory = new EnderEyeThrowFactory(preferences, dataState.boatDataState());
		return new PlayerPositionInputHandler(coordinateInputSource, dataState, actionExecutor, preferences, enderEyeThrowFactory);
	}

	private PlayerPositionInputHandler createFakePlayerPositionInputHandler() {
		if (fakeCoordinateInputSource == null)
			fakeCoordinateInputSource = new FakeCoordinateInputSource();
		IEnderEyeThrowFactory enderEyeThrowFactory = new EnderEyeThrowFactory(preferences, dataState.boatDataState());
		return new PlayerPositionInputHandler(fakeCoordinateInputSource, dataState, actionExecutor, preferences, enderEyeThrowFactory);
	}

}
