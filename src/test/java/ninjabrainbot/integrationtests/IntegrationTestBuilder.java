package ninjabrainbot.integrationtests;

import ninjabrainbot.data.DataState;
import ninjabrainbot.data.IDataState;
import ninjabrainbot.data.actions.ActionExecutor;
import ninjabrainbot.data.calculator.Calculator;
import ninjabrainbot.data.calculator.CalculatorSettings;
import ninjabrainbot.data.calculator.endereye.CoordinateInputSource;
import ninjabrainbot.data.calculator.endereye.EnderEyeThrowFactory;
import ninjabrainbot.data.calculator.endereye.IEnderEyeThrowFactory;
import ninjabrainbot.data.calculator.endereye.StandardDeviationHandler;
import ninjabrainbot.data.domainmodel.DomainModel;
import ninjabrainbot.data.input.ActiveInstanceInputHandler;
import ninjabrainbot.data.input.FossilInputHandler;
import ninjabrainbot.data.input.HotkeyInputHandler;
import ninjabrainbot.data.input.PlayerPositionInputHandler;
import ninjabrainbot.io.mcinstance.IMinecraftWorldFile;
import ninjabrainbot.io.preferences.HotkeyPreference;
import ninjabrainbot.io.preferences.MultipleChoicePreferenceDataTypes;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.io.preferences.UnsavedPreferences;
import ninjabrainbot.util.MockedClipboardReader;
import ninjabrainbot.util.MockedInstanceProvider;

public class IntegrationTestBuilder {

	public final NinjabrainBotPreferences preferences;
	public final DomainModel domainModel;

	private final ActionExecutor actionExecutor;
	private final StandardDeviationHandler standardDeviationHandler;
	private final IDataState dataState;

	private CoordinateInputSource coordinateInputSource;
	private MockedClipboardReader clipboardReader;
	private PlayerPositionInputHandler playerPositionInputHandler;
	private FossilInputHandler fossilInputHandler;
	private HotkeyInputHandler hotkeyInputHandler;
	private MockedInstanceProvider activeInstanceProvider;
	private ActiveInstanceInputHandler activeInstanceInputHandler;

	public IntegrationTestBuilder() {
		preferences = new NinjabrainBotPreferences(new UnsavedPreferences());
		domainModel = new DomainModel();
		actionExecutor = new ActionExecutor(domainModel);
		standardDeviationHandler = new StandardDeviationHandler(preferences);
		CalculatorSettings calculatorSettings = new CalculatorSettings(preferences);
		dataState = new DataState(new Calculator(calculatorSettings), domainModel);
	}

	public IntegrationTestBuilder withProSettings() {
		preferences.sigma.set(0.005f);
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

	public void triggerHotkey(HotkeyPreference hotkeyPreference) {
		if (hotkeyInputHandler == null)
			hotkeyInputHandler = new HotkeyInputHandler(preferences, domainModel, dataState, actionExecutor);
		hotkeyPreference.execute();
	}

	public void setActiveMinecraftWorld(IMinecraftWorldFile minecraftWorld) {
		if (activeInstanceProvider == null)
			activeInstanceProvider = new MockedInstanceProvider();
		if (activeInstanceInputHandler == null)
			activeInstanceInputHandler = new ActiveInstanceInputHandler(activeInstanceProvider, domainModel, dataState, actionExecutor, preferences);
		activeInstanceProvider.activeMinecraftWorld().set(minecraftWorld);
	}

	public IDataState getDataState() {
		return dataState;
	}

	private PlayerPositionInputHandler createPlayerPositionInputHandler() {
		IEnderEyeThrowFactory enderEyeThrowFactory = new EnderEyeThrowFactory(preferences, dataState.boatDataState(), standardDeviationHandler);
		return new PlayerPositionInputHandler(coordinateInputSource, dataState, actionExecutor, preferences, enderEyeThrowFactory);
	}

}
