package ninjabrainbot.data;

import ninjabrainbot.data.actions.ActionExecutor;
import ninjabrainbot.data.actions.IActionExecutor;
import ninjabrainbot.data.calculator.Calculator;
import ninjabrainbot.data.calculator.CalculatorSettings;
import ninjabrainbot.data.calculator.divine.Fossil;
import ninjabrainbot.data.calculator.endereye.StandardStdProfile;
import ninjabrainbot.data.calculator.endereye.CoordinateInputParser;
import ninjabrainbot.data.input.ActiveInstanceInputHandler;
import ninjabrainbot.data.input.ButtonInputHandler;
import ninjabrainbot.data.input.FossilInputHandler;
import ninjabrainbot.data.input.HotkeyInputHandler;
import ninjabrainbot.data.input.IButtonInputHandler;
import ninjabrainbot.data.input.PlayerPositionInputHandler;
import ninjabrainbot.data.temp.DomainModel;
import ninjabrainbot.event.DisposeHandler;
import ninjabrainbot.event.IDisposable;
import ninjabrainbot.event.ISubscribable;
import ninjabrainbot.event.ObservableProperty;
import ninjabrainbot.io.IClipboardProvider;
import ninjabrainbot.io.mcinstance.IActiveInstanceProvider;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;

public class DataStateHandler implements IDataStateHandler, IDisposable {

	private final NinjabrainBotPreferences preferences;
	private final StandardStdProfile stdProfile;

	private final IActiveInstanceProvider activeInstanceProvider;

	private final DataState dataState;
	private final ObservableProperty<IDataState> whenDataStateModified = new ObservableProperty<>();

	public final DomainModel domainModel;
	public final IActionExecutor actionExecutor;
	public final IButtonInputHandler buttonInputHandler;

	private final DisposeHandler disposeHandler = new DisposeHandler();

	public DataStateHandler(NinjabrainBotPreferences preferences, IClipboardProvider clipboardProvider, IActiveInstanceProvider activeInstanceProvider) {
		this.preferences = preferences;
		this.activeInstanceProvider = activeInstanceProvider;
		this.stdProfile = new StandardStdProfile(preferences);

		domainModel = new DomainModel();
		actionExecutor = new ActionExecutor(domainModel);

		CalculatorSettings calculatorSettings = new CalculatorSettings(preferences);
		dataState = new DataState(new Calculator(calculatorSettings), domainModel);
//		dataStateUndoHistory = new DataStateUndoHistory(dataState.getUndoData(), 10);

		CoordinateInputParser coordinateInputParser = new CoordinateInputParser(clipboardProvider, preferences, stdProfile, dataState.boatDataState.boatAngle());
		disposeHandler.add(new PlayerPositionInputHandler(coordinateInputParser, dataState, actionExecutor, preferences));
		disposeHandler.add(new FossilInputHandler(coordinateInputParser, dataState, actionExecutor));
		disposeHandler.add(new ActiveInstanceInputHandler(activeInstanceProvider, domainModel, dataState, actionExecutor, preferences));
		disposeHandler.add(new HotkeyInputHandler(preferences, domainModel, dataState, actionExecutor));

		buttonInputHandler = new ButtonInputHandler(domainModel, dataState, actionExecutor);

		disposeHandler.add(preferences.useAdvStatistics.whenModified().subscribe(this::onCalculatorSettingsChanged));
		disposeHandler.add(preferences.mcVersion.whenModified().subscribe(this::onCalculatorSettingsChanged));
		disposeHandler.add(preferences.sigma.whenModified().subscribe(newStd -> setStdProfile(StandardStdProfile.NORMAL, newStd)));
		disposeHandler.add(preferences.sigmaAlt.whenModified().subscribe(newStd -> setStdProfile(StandardStdProfile.ALTERNATIVE, newStd)));
		disposeHandler.add(preferences.sigmaManual.whenModified().subscribe(newStd -> setStdProfile(StandardStdProfile.MANUAL, newStd)));
		disposeHandler.add(preferences.sigmaBoat.whenModified().subscribe(newStd -> setStdProfile(StandardStdProfile.BOAT, newStd)));
	}

	@Override
	public synchronized void toggleAltStdOnLastThrowIfNotLocked() {
//		try (ILock lock = modificationLock.acquireWritePermission()) {
//			if (!dataState.locked().get() && dataState.getThrowSet().size() != 0) {
//				IThrow last = dataState.getThrowSet().getLast();
//				int stdProfile = last.getStdProfileNumber();
//				switch (stdProfile) {
//					case StandardStdProfile.NORMAL:
//						last.setStdProfileNumber(StandardStdProfile.ALTERNATIVE);
//						break;
//					case StandardStdProfile.ALTERNATIVE:
//						last.setStdProfileNumber(StandardStdProfile.NORMAL);
//						break;
//					case StandardStdProfile.MANUAL:
//						break;
//				}
//			}
//		}
	}

	public synchronized void toggleLocked() {
//		try (ILock lock = modificationLock.acquireWritePermission()) {
//			dataState.toggleLocked();
//		}
	}

	private void afterDataStateModified(boolean wasUndoAction) {
//		if (!wasUndoAction) {
//			dataStateUndoHistory.addNewUndoData(dataState.getUndoData());
//		}
//		whenDataStateModified.notifySubscribers(dataState);
	}

	private synchronized void setFossil(Fossil f) {
		dataState.getDivineContext().fossil().set(f);
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
