package ninjabrainbot.data;

import ninjabrainbot.data.actions.ActionExecutor;
import ninjabrainbot.data.actions.IActionExecutor;
import ninjabrainbot.data.calculator.Calculator;
import ninjabrainbot.data.calculator.CalculatorSettings;
import ninjabrainbot.data.calculator.endereye.CoordinateInputSource;
import ninjabrainbot.data.calculator.endereye.EnderEyeThrowFactory;
import ninjabrainbot.data.calculator.endereye.IEnderEyeThrowFactory;
import ninjabrainbot.data.calculator.endereye.StandardDeviationHandler;
import ninjabrainbot.data.domainmodel.DomainModel;
import ninjabrainbot.data.input.ActiveInstanceInputHandler;
import ninjabrainbot.data.input.ButtonInputHandler;
import ninjabrainbot.data.input.FossilInputHandler;
import ninjabrainbot.data.input.HotkeyInputHandler;
import ninjabrainbot.data.input.IButtonInputHandler;
import ninjabrainbot.data.input.PlayerPositionInputHandler;
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

	private void afterDataStateModified(boolean wasUndoAction) {
//		if (!wasUndoAction) {
//			dataStateUndoHistory.addNewUndoData(dataState.getUndoData());
//		}
//		whenDataStateModified.notifySubscribers(dataState);
	}

	private synchronized void onCalculatorSettingsChanged() {
//		CalculatorSettings calculatorSettings = new CalculatorSettings(preferences);
//		try (ILock lock = modificationLock.acquireWritePermission()) {
//			dataState.setCalculator(new Calculator(calculatorSettings));
//		}
	}


	private synchronized void setStdProfile(int profileNumber, double std) {
//		try (ILock lock = modificationLock.acquireWritePermission()) {
//			stdProfile.setStd(profileNumber, std);
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
