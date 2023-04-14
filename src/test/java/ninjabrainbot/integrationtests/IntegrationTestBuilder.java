package ninjabrainbot.integrationtests;

import ninjabrainbot.event.DisposeHandler;
import ninjabrainbot.gui.frames.NinjabrainBotFrame;
import ninjabrainbot.gui.mainwindow.BoatIcon;
import ninjabrainbot.gui.mainwindow.main.MainTextArea;
import ninjabrainbot.gui.mainwindow.main.MainTextAreaTestAdapter;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.io.mcinstance.IMinecraftWorldFile;
import ninjabrainbot.io.preferences.HotkeyPreference;
import ninjabrainbot.io.preferences.MultipleChoicePreferenceDataTypes;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.io.preferences.UnsavedPreferences;
import ninjabrainbot.model.actions.ActionExecutor;
import ninjabrainbot.model.datastate.DataState;
import ninjabrainbot.model.datastate.IDataState;
import ninjabrainbot.model.datastate.endereye.CoordinateInputSource;
import ninjabrainbot.model.datastate.endereye.EnderEyeThrowFactory;
import ninjabrainbot.model.datastate.endereye.IEnderEyeThrowFactory;
import ninjabrainbot.model.datastate.endereye.StandardDeviationHandler;
import ninjabrainbot.model.domainmodel.DomainModel;
import ninjabrainbot.model.environmentstate.EnvironmentState;
import ninjabrainbot.model.environmentstate.IEnvironmentState;
import ninjabrainbot.model.information.InformationMessageList;
import ninjabrainbot.model.input.ActiveInstanceInputHandler;
import ninjabrainbot.model.input.ButtonInputHandler;
import ninjabrainbot.model.input.FossilInputHandler;
import ninjabrainbot.model.input.HotkeyInputHandler;
import ninjabrainbot.model.input.PlayerPositionInputHandler;
import ninjabrainbot.util.Assert;
import ninjabrainbot.util.FakeUpdateChecker;
import ninjabrainbot.util.MockedClipboardReader;
import ninjabrainbot.util.MockedInstanceProvider;
import ninjabrainbot.util.TestTheme2;
import ninjabrainbot.util.TestUtils;

public class IntegrationTestBuilder {

	public final NinjabrainBotPreferences preferences;
	public final DomainModel domainModel;

	public final ActionExecutor actionExecutor;
	public final StandardDeviationHandler standardDeviationHandler;
	public final IEnvironmentState environmentState;
	public final IDataState dataState;

	private CoordinateInputSource coordinateInputSource;
	private MockedClipboardReader clipboardReader;
	private MockedInstanceProvider activeInstanceProvider;

	private PlayerPositionInputHandler playerPositionInputHandler;
	private FossilInputHandler fossilInputHandler;
	private HotkeyInputHandler hotkeyInputHandler;
	private ButtonInputHandler buttonInputHandler;
	private ActiveInstanceInputHandler activeInstanceInputHandler;

	private StyleManager styleManager;

	public IntegrationTestBuilder() {
		preferences = new NinjabrainBotPreferences(new UnsavedPreferences());
		domainModel = new DomainModel();
		actionExecutor = new ActionExecutor(domainModel);
		standardDeviationHandler = new StandardDeviationHandler(preferences);
		environmentState = new EnvironmentState(domainModel, preferences);
		dataState = new DataState(domainModel, environmentState);
	}

	public IntegrationTestBuilder withProSettings() {
		preferences.sigma.set(0.005f);
		preferences.view.set(MultipleChoicePreferenceDataTypes.MainViewType.DETAILED);
		preferences.strongholdDisplayType.set(MultipleChoicePreferenceDataTypes.StrongholdDisplayType.CHUNK);
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
		preferences.view.set(MultipleChoicePreferenceDataTypes.MainViewType.DETAILED);
		preferences.strongholdDisplayType.set(MultipleChoicePreferenceDataTypes.StrongholdDisplayType.CHUNK);
		return this;
	}

	public void setClipboard(String clipboardString) {
		if (clipboardReader == null)
			clipboardReader = new MockedClipboardReader();
		if (coordinateInputSource == null)
			coordinateInputSource = new CoordinateInputSource(clipboardReader);
		if (playerPositionInputHandler == null)
			playerPositionInputHandler = createPlayerPositionInputHandler();
		if (fossilInputHandler == null)
			fossilInputHandler = new FossilInputHandler(coordinateInputSource, dataState, actionExecutor);
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

	public void triggerHotkey(HotkeyPreference hotkeyPreference) {
		if (hotkeyInputHandler == null)
			hotkeyInputHandler = new HotkeyInputHandler(preferences, domainModel, dataState, actionExecutor);
		hotkeyPreference.execute();
	}

	public void setActiveMinecraftWorld(IMinecraftWorldFile minecraftWorld) {
		if (activeInstanceProvider == null)
			activeInstanceProvider = new MockedInstanceProvider();
		if (activeInstanceInputHandler == null)
			activeInstanceInputHandler = new ActiveInstanceInputHandler(activeInstanceProvider, domainModel, dataState, environmentState, actionExecutor, preferences);
		activeInstanceProvider.activeMinecraftWorld().set(minecraftWorld);
	}

	public MainTextAreaTestAdapter createMainTextArea() {
		if (styleManager == null)
			styleManager = TestUtils.createStyleManager();
		if (buttonInputHandler == null)
			buttonInputHandler = new ButtonInputHandler(domainModel, dataState, actionExecutor);
		return new MainTextAreaTestAdapter(new MainTextArea(styleManager, buttonInputHandler, preferences, dataState));
	}

	public NinjabrainBotFrame createNinjabrainBotFrame() {
		if (styleManager == null)
			styleManager = TestUtils.createStyleManager();
		if (buttonInputHandler == null)
			buttonInputHandler = new ButtonInputHandler(domainModel, dataState, actionExecutor);
		NinjabrainBotFrame frame = new NinjabrainBotFrame(styleManager, preferences, new FakeUpdateChecker(), dataState, buttonInputHandler, new InformationMessageList());
		styleManager.init();
		return frame;
	}

	public BoatIcon createBoatIcon() {
		if (styleManager == null)
			styleManager = TestUtils.createStyleManager();
		return new BoatIcon(styleManager, dataState.boatDataState().boatState(), preferences, new DisposeHandler());
	}

	public void swapTheme() {
		Assert.isNotNull(styleManager, "Create something that uses a StyleManager first!");
		styleManager.currentTheme.setTheme(new TestTheme2());
	}

	public void addDummyEnderEyeThrow(){
		TestUtils.addDummyEnderEyeThrow(domainModel, dataState);
	}

	private PlayerPositionInputHandler createPlayerPositionInputHandler() {
		IEnderEyeThrowFactory enderEyeThrowFactory = new EnderEyeThrowFactory(preferences, dataState.boatDataState(), standardDeviationHandler);
		return new PlayerPositionInputHandler(coordinateInputSource, dataState, actionExecutor, preferences, enderEyeThrowFactory);
	}

}
