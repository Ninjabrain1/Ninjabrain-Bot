package ninjabrainbot.data;

import ninjabrainbot.data.blind.BlindPosition;
import ninjabrainbot.data.calculator.Calculator;
import ninjabrainbot.data.calculator.CalculatorSettings;
import ninjabrainbot.data.datalock.ILock;
import ninjabrainbot.data.datalock.IModificationLock;
import ninjabrainbot.data.datalock.ModificationLock;
import ninjabrainbot.data.divine.Fossil;
import ninjabrainbot.data.endereye.IThrow;
import ninjabrainbot.data.endereye.StandardStdProfile;
import ninjabrainbot.data.endereye.ThrowParser;
import ninjabrainbot.event.IDisposable;
import ninjabrainbot.event.ISubscribable;
import ninjabrainbot.event.ObservableProperty;
import ninjabrainbot.event.SubscriptionHandler;
import ninjabrainbot.io.IClipboardProvider;
import ninjabrainbot.io.mcinstance.IActiveInstanceProvider;
import ninjabrainbot.io.mcinstance.IMinecraftWorldFile;
import ninjabrainbot.io.preferences.MultipleChoicePreferenceDataTypes.McVersion;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;

public class DataStateHandler implements IDataStateHandler, IDisposable {

	private final NinjabrainBotPreferences preferences;
	private final StandardStdProfile stdProfile;
	private final CalculatorSettings calculatorSettings;

	private final IActiveInstanceProvider activeInstanceProvider;

	private DataState dataState;
	private ModificationLock modificationLock;
	private ObservableProperty<IDataState> whenDataStateModified = new ObservableProperty<IDataState>();
	private DataStateUndoHistory dataStateUndoHistory;

	private SubscriptionHandler sh = new SubscriptionHandler();

	public DataStateHandler(NinjabrainBotPreferences preferences, IClipboardProvider clipboardProvider, IActiveInstanceProvider activeInstanceProvider) {
		this.preferences = preferences;
		this.activeInstanceProvider = activeInstanceProvider;
		this.stdProfile = new StandardStdProfile(preferences);
		modificationLock = new ModificationLock(wasUndoAction -> afterDataStateModified(wasUndoAction));

		calculatorSettings = new CalculatorSettings();
		calculatorSettings.useAdvStatistics = preferences.useAdvStatistics.get();
		calculatorSettings.version = preferences.mcVersion.get();
		dataState = new DataState(new Calculator(calculatorSettings), modificationLock);
		dataStateUndoHistory = new DataStateUndoHistory(dataState.getUndoData(), 10);

		ThrowParser throwParser = new ThrowParser(clipboardProvider, preferences, modificationLock, dataState.boatDataState.boatAngle());
		addThrowStream(throwParser.whenNewThrowInputed());
		addFossilStream(throwParser.whenNewFossilInputed());

		activeInstanceProvider.activeMinecraftWorld().subscribe(__ -> resetIfNotLocked());
		activeInstanceProvider.whenActiveMinecraftWorldModified().subscribe(__ -> updateAllAdvancementsMode());

		sh.add(preferences.useAdvStatistics.whenModified().subscribe(newValue -> onUseAdvStatisticsChanged(newValue)));
		sh.add(preferences.mcVersion.whenModified().subscribe(newValue -> onMcVersionChanged(newValue)));
		sh.add(preferences.allAdvancements.whenModified().subscribe(__ -> updateAllAdvancementsMode()));
	}

	@Override
	public synchronized void reset() {
		try (ILock lock = modificationLock.acquireWritePermission()) {
			dataState.reset();
		}
	}

	@Override
	public synchronized void resetIfNotLocked() {
		if (!dataState.locked().get())
			reset();
	}

	@Override
	public synchronized void undo() {
		try (ILock lock = modificationLock.acquireWritePermission()) {
			lock.setUndoAction();
			dataState.setFromUndoData(dataStateUndoHistory.moveToPrevious());
		}
	}

	@Override
	public synchronized void undoIfNotLocked() {
		if (!dataState.locked().get())
			undo();
	}

	@Override
	public synchronized void removeThrow(IThrow t) {
		try (ILock lock = modificationLock.acquireWritePermission()) {
			dataState.getThrowSet().remove(t);
		}
	}

	@Override
	public synchronized void resetDivineContext() {
		try (ILock lock = modificationLock.acquireWritePermission()) {
			dataState.getDivineContext().resetFossil();
		}
	}

	public synchronized void changeLastAngleIfNotLocked(boolean positive, NinjabrainBotPreferences preferences) {
		if (!dataState.locked().get() && dataState.getThrowSet().size() != 0) {
			IThrow last = dataState.getThrowSet().getLast();
			if (last != null) {
				try (ILock lock = modificationLock.acquireWritePermission()) {
					last.addCorrection(positive, preferences);
				}
			}
		}
	}

	@Override
	public synchronized void toggleAltStdOnLastThrowIfNotLocked() {
		try (ILock lock = modificationLock.acquireWritePermission()) {
			if (!dataState.locked().get() && dataState.getThrowSet().size() != 0) {
				IThrow last = dataState.getThrowSet().getLast();
				int stdProfile = last.getStdProfileNumber();
				switch (stdProfile) {
				case StandardStdProfile.NORMAL:
					last.setStdProfileNumber(StandardStdProfile.ALTERNATIVE);
					break;
				case StandardStdProfile.ALTERNATIVE:
					last.setStdProfileNumber(StandardStdProfile.NORMAL);
					break;
				case StandardStdProfile.MANUAL:
					break;
				}
			}
		}
	}

	public synchronized void toggleLocked() {
		try (ILock lock = modificationLock.acquireWritePermission()) {
			dataState.toggleLocked();
		}
	}

	public synchronized void toggleEnteringBoatIfNotLocked() {
		try (ILock lock = modificationLock.acquireWritePermission()) {
			if (!dataState.locked().get()) {
				dataState.boatDataState.toggleEnteringBoat();
			}
		}
	}

	private void afterDataStateModified(boolean wasUndoAction) {
		if (!wasUndoAction) {
			dataStateUndoHistory.addNewUndoData(dataState.getUndoData());
		}
		whenDataStateModified.notifySubscribers(dataState);
	}

	private synchronized void onNewThrow(IThrow t) {
		try (ILock lock = modificationLock.acquireWritePermission()) {
			dataState.setPlayerPos(t);
			if (dataState.locked().get())
				return;
			if (t.isNether()) {
				if (dataState.getThrowSet().size() == 0)
					dataState.setBlindPosition(new BlindPosition(t));
				return;
			}
			if (dataState.boatDataState.enteringBoat().get()) {
				dataState.boatDataState.setBoatAngle(t.rawAlpha(), preferences.boatErrorLimit.get());
				return;
			}
			if (!t.lookingBelowHorizon()) {
				t.setStdProfile(stdProfile);
				dataState.getThrowSet().add(t);
			}
		}
	}

	private synchronized void setFossil(Fossil f) {
		try (ILock lock = modificationLock.acquireWritePermission()) {
			dataState.setFossil(f);
		}
	}

	private synchronized void onUseAdvStatisticsChanged(boolean newValue) {
		calculatorSettings.useAdvStatistics = newValue;
		try (ILock lock = modificationLock.acquireWritePermission()) {
			dataState.recalculateStronghold();
		}
	}

	private synchronized void onMcVersionChanged(McVersion newValue) {
		calculatorSettings.version = newValue;
		try (ILock lock = modificationLock.acquireWritePermission()) {
			dataState.recalculateStronghold();
		}
	}

	private synchronized void updateAllAdvancementsMode() {
		IMinecraftWorldFile world = activeInstanceProvider.activeMinecraftWorld().get();
		boolean allAdvancementsModeEnabled = false;
		if (world != null) {
			allAdvancementsModeEnabled = preferences.allAdvancements.get() && world.hasEnteredEnd();
		}

		try (ILock lock = modificationLock.acquireWritePermission()) {
			dataState.setAllAdvancementsMode(allAdvancementsModeEnabled);
		}
	}

	@Override
	public void addThrowStream(ISubscribable<IThrow> stream) {
		stream.subscribe(t -> onNewThrow(t));
	}

	@Override
	public void addFossilStream(ISubscribable<Fossil> stream) {
		stream.subscribe(f -> setFossil(f));
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
	public IModificationLock getModificationLock() {
		return modificationLock;
	}

	@Override
	public void dispose() {
		sh.dispose();
	}

}
