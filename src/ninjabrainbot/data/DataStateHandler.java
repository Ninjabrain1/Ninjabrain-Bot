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
import ninjabrainbot.event.IDisposable;
import ninjabrainbot.event.ISubscribable;
import ninjabrainbot.event.ObservableProperty;
import ninjabrainbot.event.SubscriptionHandler;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.io.preferences.MultipleChoicePreferenceDataTypes.McVersion;

public class DataStateHandler implements IDataStateHandler, IDisposable {

	private final StandardStdProfile stdProfile;
	private final CalculatorSettings calculatorSettings;

	private DataState dataState;
	private ModificationLock modificationLock;
	private ObservableProperty<IDataState> whenDataStateModified = new ObservableProperty<IDataState>();

	private SubscriptionHandler sh = new SubscriptionHandler();

	public DataStateHandler(NinjabrainBotPreferences preferences) {
		this.stdProfile = new StandardStdProfile(preferences);
		modificationLock = new ModificationLock(() -> whenDataStateModified.notifySubscribers(dataState));

		calculatorSettings = new CalculatorSettings();
		calculatorSettings.useAdvStatistics = preferences.useAdvStatistics.get();
		calculatorSettings.version = preferences.mcVersion.get();
		dataState = new DataState(new Calculator(calculatorSettings), modificationLock);

		sh.add(preferences.useAdvStatistics.whenModified().subscribe(newValue -> onUseAdvStatisticsChanged(newValue)));
		sh.add(preferences.mcVersion.whenModified().subscribe(newValue -> onMcVersionChanged(newValue)));
	}

	public synchronized void reset() {
		try (ILock lock = modificationLock.acquireWritePermission()) {
			dataState.reset();
		}
	}

	public synchronized void resetIfNotLocked() {
		if (!dataState.locked().get())
			reset();
	}

	public synchronized void undo() {
		try (ILock lock = modificationLock.acquireWritePermission()) {

		}
	}

	public synchronized void undoIfNotLocked() {
		if (!dataState.locked().get())
			undo();
	}

	@Override
	public void removeThrow(IThrow t) {
		try (ILock lock = modificationLock.acquireWritePermission()) {
			dataState.getThrowSet().remove(t);
		}
	}

	public synchronized void changeLastAngleIfNotLocked(double delta) {
		if (!dataState.locked().get() && dataState.getThrowSet().size() != 0) {
			IThrow last = dataState.getThrowSet().getLast();
			if (last != null) {
				try (ILock lock = modificationLock.acquireWritePermission()) {
					last.addCorrection(delta);
				}
			}
		}
	}

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

	public synchronized void setFossil(Fossil f) {
		try (ILock lock = modificationLock.acquireWritePermission()) {
			dataState.setFossil(f);
		}
	}

	public synchronized void toggleLocked() {
		try (ILock lock = modificationLock.acquireWritePermission()) {
			dataState.toggleLocked();
		}
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
			if (!t.lookingBelowHorizon()) {
				t.setStdProfile(stdProfile);
				dataState.getThrowSet().add(t);
			}
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
