package ninjabrainbot.data;

import ninjabrainbot.data.actions.ActionExecutor;
import ninjabrainbot.data.calculator.Calculator;
import ninjabrainbot.data.calculator.CalculatorSettings;
import ninjabrainbot.data.calculator.divine.Fossil;
import ninjabrainbot.data.calculator.endereye.IThrow;
import ninjabrainbot.data.calculator.endereye.StandardStdProfile;
import ninjabrainbot.data.calculator.endereye.ThrowParser;
import ninjabrainbot.data.input.FossilInputHandler;
import ninjabrainbot.data.input.ThrowInputHandler;
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

	private final DisposeHandler disposeHandler = new DisposeHandler();

	public DataStateHandler(NinjabrainBotPreferences preferences, IClipboardProvider clipboardProvider, IActiveInstanceProvider activeInstanceProvider) {
		this.preferences = preferences;
		this.activeInstanceProvider = activeInstanceProvider;
		this.stdProfile = new StandardStdProfile(preferences);

		DomainModel domainModel = new DomainModel();
		ActionExecutor actionExecutor = new ActionExecutor(domainModel);

		CalculatorSettings calculatorSettings = new CalculatorSettings(preferences);
		dataState = new DataState(new Calculator(calculatorSettings), domainModel);
//		dataStateUndoHistory = new DataStateUndoHistory(dataState.getUndoData(), 10);

		ThrowParser throwParser = new ThrowParser(clipboardProvider, preferences, stdProfile, dataState.boatDataState.boatAngle());
		disposeHandler.add(new ThrowInputHandler(throwParser.whenNewThrowInputted(), dataState, actionExecutor, preferences));
		disposeHandler.add(new FossilInputHandler(throwParser.whenNewFossilInputted(), dataState, actionExecutor));

		disposeHandler.add(activeInstanceProvider.activeMinecraftWorld().subscribe(__ -> resetIfNotLocked()));
		disposeHandler.add(activeInstanceProvider.whenActiveMinecraftWorldModified().subscribe(this::updateAllAdvancementsMode));

		disposeHandler.add(preferences.useAdvStatistics.whenModified().subscribe(this::onCalculatorSettingsChanged));
		disposeHandler.add(preferences.mcVersion.whenModified().subscribe(this::onCalculatorSettingsChanged));
		disposeHandler.add(preferences.allAdvancements.whenModified().subscribe(this::updateAllAdvancementsMode));
		disposeHandler.add(preferences.sigma.whenModified().subscribe(newStd -> setStdProfile(StandardStdProfile.NORMAL, newStd)));
		disposeHandler.add(preferences.sigmaAlt.whenModified().subscribe(newStd -> setStdProfile(StandardStdProfile.ALTERNATIVE, newStd)));
		disposeHandler.add(preferences.sigmaManual.whenModified().subscribe(newStd -> setStdProfile(StandardStdProfile.MANUAL, newStd)));
		disposeHandler.add(preferences.sigmaBoat.whenModified().subscribe(newStd -> setStdProfile(StandardStdProfile.BOAT, newStd)));
	}

	@Override
	public synchronized void reset() {
//		try (ILock lock = modificationLock.acquireWritePermission()) {
//			dataState.reset();
//		}
	}

	@Override
	public synchronized void resetIfNotLocked() {
//		if (!dataState.locked().get())
//			reset();
	}

	@Override
	public synchronized void undo() {
//		try (ILock lock = modificationLock.acquireWritePermission()) {
//			lock.setUndoAction();
//			dataState.setFromUndoData(dataStateUndoHistory.moveToPrevious());
//		}
	}

	@Override
	public synchronized void undoIfNotLocked() {
//		if (!dataState.locked().get())
//			undo();
	}

	@Override
	public synchronized void removeThrow(IThrow t) {
//		try (ILock lock = modificationLock.acquireWritePermission()) {
//			dataState.getThrowSet().remove(t);
//		}
	}

	@Override
	public synchronized void resetDivineContext() {
//		try (ILock lock = modificationLock.acquireWritePermission()) {
//			dataState.getDivineContext().resetFossil();
//		}
	}

	public synchronized void changeLastAngleIfNotLocked(boolean positive, NinjabrainBotPreferences preferences) {
//		if (!dataState.locked().get() && dataState.getThrowSet().size() != 0) {
//			IThrow last = dataState.getThrowSet().getLast();
//			if (last != null) {
//				try (ILock lock = modificationLock.acquireWritePermission()) {
//					last.addCorrection(positive, preferences);
//				}
//			}
//		}
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

	public synchronized void toggleEnteringBoatIfNotLocked() {
//		try (ILock lock = modificationLock.acquireWritePermission()) {
//			if (!dataState.locked().get()) {
//				dataState.boatDataState.toggleEnteringBoat();
//			}
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

	private synchronized void updateAllAdvancementsMode() {
//		IMinecraftWorldFile world = activeInstanceProvider.activeMinecraftWorld().get();
//		boolean allAdvancementsModeEnabled = false;
//		if (world != null) {
//			allAdvancementsModeEnabled = preferences.allAdvancements.get() && world.hasEnteredEnd();
//		}
//
//		try (ILock lock = modificationLock.acquireWritePermission()) {
//			dataState.allAdvancementsDataState.setAllAdvancementsModeEnabled(allAdvancementsModeEnabled);
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
