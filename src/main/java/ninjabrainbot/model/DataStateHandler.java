package ninjabrainbot.model;

import ninjabrainbot.model.actions.ActionExecutor;
import ninjabrainbot.model.actions.IActionExecutor;
import ninjabrainbot.model.datastate.Calculator;
import ninjabrainbot.model.datastate.CalculatorSettings;
import ninjabrainbot.model.datastate.endereye.CoordinateInputSource;
import ninjabrainbot.model.datastate.endereye.EnderEyeThrowFactory;
import ninjabrainbot.model.datastate.endereye.IEnderEyeThrowFactory;
import ninjabrainbot.model.datastate.endereye.StandardDeviationHandler;
import ninjabrainbot.model.domainmodel.DomainModel;
import ninjabrainbot.model.input.ActiveInstanceInputHandler;
import ninjabrainbot.model.input.ButtonInputHandler;
import ninjabrainbot.model.input.FossilInputHandler;
import ninjabrainbot.model.input.HotkeyInputHandler;
import ninjabrainbot.model.input.IButtonInputHandler;
import ninjabrainbot.model.input.PlayerPositionInputHandler;
import ninjabrainbot.event.DisposeHandler;
import ninjabrainbot.event.IDisposable;
import ninjabrainbot.event.ISubscribable;
import ninjabrainbot.event.ObservableProperty;
import ninjabrainbot.io.IClipboardProvider;
import ninjabrainbot.io.mcinstance.IActiveInstanceProvider;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;

public class DataStateHandler implements IDataStateHandler, IDisposable {

	private final DataState dataState;
	private final ObservableProperty<IDataState> whenDataStateModified = new ObservableProperty<>();

	public final DomainModel domainModel;
	public final IActionExecutor actionExecutor;
	public final IButtonInputHandler buttonInputHandler;

	private final DisposeHandler disposeHandler = new DisposeHandler();

	public DataStateHandler(NinjabrainBotPreferences preferences, IClipboardProvider clipboardProvider, IActiveInstanceProvider activeInstanceProvider) {
		domainModel = new DomainModel();
		actionExecutor = new ActionExecutor(domainModel);

		StandardDeviationHandler standardDeviationHandler = disposeHandler.add(new StandardDeviationHandler(preferences));
		CalculatorSettings calculatorSettings = new CalculatorSettings(preferences);
		dataState = new DataState(new Calculator(calculatorSettings), domainModel);

		CoordinateInputSource coordinateInputSource = new CoordinateInputSource(clipboardProvider);
		IEnderEyeThrowFactory enderEyeThrowFactory = new EnderEyeThrowFactory(preferences, dataState.boatDataState, standardDeviationHandler);
		disposeHandler.add(new PlayerPositionInputHandler(coordinateInputSource, dataState, actionExecutor, preferences, enderEyeThrowFactory));
		disposeHandler.add(new FossilInputHandler(coordinateInputSource, dataState, actionExecutor));
		disposeHandler.add(new ActiveInstanceInputHandler(activeInstanceProvider, domainModel, dataState, actionExecutor, preferences));
		disposeHandler.add(new HotkeyInputHandler(preferences, domainModel, dataState, actionExecutor));

		buttonInputHandler = new ButtonInputHandler(domainModel, dataState, actionExecutor);

		disposeHandler.add(preferences.useAdvStatistics.whenModified().subscribe(this::onCalculatorSettingsChanged));
		disposeHandler.add(preferences.mcVersion.whenModified().subscribe(this::onCalculatorSettingsChanged));
	}

	private synchronized void onCalculatorSettingsChanged() {
//		CalculatorSettings calculatorSettings = new CalculatorSettings(preferences);
//		try (ILock lock = modificationLock.acquireWritePermission()) {
//			dataState.setCalculator(new Calculator(calculatorSettings));
//		}
	}

	@Override
	public IDataState getDataState() {
		return dataState;
	}

	@Override
	public ISubscribable<IDataState> whenDataStateModified() {
		return whenDataStateModified;
	}

	@Override
	public void dispose() {
		disposeHandler.dispose();
	}

}
