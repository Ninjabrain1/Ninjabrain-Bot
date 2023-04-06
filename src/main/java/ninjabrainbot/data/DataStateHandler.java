package ninjabrainbot.data;

import ninjabrainbot.data.alladvancements.AllAdvancementsDataState;
import ninjabrainbot.data.alladvancements.StructureType;
import ninjabrainbot.data.calculator.Calculator;
import ninjabrainbot.data.calculator.CalculatorSettings;
import ninjabrainbot.data.common.StructurePosition;
import ninjabrainbot.data.datalock.ILock;
import ninjabrainbot.data.datalock.IModificationLock;
import ninjabrainbot.data.datalock.ModificationLock;
import ninjabrainbot.data.divine.Fossil;
import ninjabrainbot.data.endereye.IThrow;
import ninjabrainbot.data.endereye.StandardStdProfile;
import ninjabrainbot.data.endereye.ThrowParser;
import ninjabrainbot.event.DisposeHandler;
import ninjabrainbot.event.IDisposable;
import ninjabrainbot.event.ISubscribable;
import ninjabrainbot.event.ObservableProperty;
import ninjabrainbot.io.IClipboardProvider;
import ninjabrainbot.io.mcinstance.IActiveInstanceProvider;
import ninjabrainbot.io.mcinstance.IMinecraftWorldFile;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;

public class DataStateHandler implements IDataStateHandler, IDisposable {

	private final NinjabrainBotPreferences preferences;
	private final StandardStdProfile stdProfile;

	private final IActiveInstanceProvider activeInstanceProvider;

	private final DataState dataState;
	private final ModificationLock modificationLock;
	private final ObservableProperty<IDataState> whenDataStateModified = new ObservableProperty<>();
	private final DataStateUndoHistory dataStateUndoHistory;

	private final DisposeHandler disposeHandler = new DisposeHandler();

	public DataStateHandler(NinjabrainBotPreferences preferences, IClipboardProvider clipboardProvider, IActiveInstanceProvider activeInstanceProvider) {
		this.preferences = preferences;
		this.activeInstanceProvider = activeInstanceProvider;
		this.stdProfile = new StandardStdProfile(preferences);
		modificationLock = new ModificationLock(this::afterDataStateModified);

		CalculatorSettings calculatorSettings = new CalculatorSettings(preferences);
		dataState = new DataState(new Calculator(calculatorSettings), modificationLock);
		dataStateUndoHistory = new DataStateUndoHistory(dataState.getUndoData(), 10);

		ThrowParser throwParser = new ThrowParser(clipboardProvider, preferences, modificationLock, dataState.boatDataState.boatAngle());
		addThrowStream(throwParser.whenNewThrowInputed());
		addFossilStream(throwParser.whenNewFossilInputed());

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
			dataState.setPlayerPosition(t);
			if (dataState.locked().get())
				return;
			if (t.isNether()) {
				return;
			}
			if (dataState.allAdvancementsDataState.allAdvancementsModeEnabled().get()) {
				tryAddAllAdvancementsStructure(t);
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

	private void tryAddAllAdvancementsStructure(IThrow t) {
		StructureType structureType = getAllAdvancementStructureTypeFromThrow(t);
		AllAdvancementsDataState allAdvancementsDataState = dataState.allAdvancementsDataState;
		switch (structureType) {
			case Spawn:
				if (allAdvancementsDataState.spawnPosition().get() == null)
					allAdvancementsDataState.setSpawnPosition(new StructurePosition((int) t.xInOverworld(), (int) t.zInOverworld(), dataState.playerPosition()));
				return;
			case Outpost:
				if (allAdvancementsDataState.outpostPosition().get() == null)
					allAdvancementsDataState.setOutpostPosition(getOutpostPosition(t));
				return;
			case Monument:
				if (allAdvancementsDataState.monumentPosition().get() == null)
					allAdvancementsDataState.setMonumentPosition(new StructurePosition((int) t.xInOverworld(), (int) t.zInOverworld(), dataState.playerPosition()));
		}
	}

	private StructureType getAllAdvancementStructureTypeFromThrow(IThrow t) {
		if (t.isNether())
			return StructureType.Unknown;

		if (Math.abs(t.xInOverworld()) <= 300 && Math.abs(t.zInOverworld()) <= 300)
			return StructureType.Spawn;

		if (t.yInPlayerDimension() < 63)
			return StructureType.Monument;

		return StructureType.Outpost;
	}

	private StructurePosition getOutpostPosition(IThrow t) {
		int averageOutpostY = 80;
		double deltaY = averageOutpostY - t.yInPlayerDimension();
		double horizontalDistance = deltaY / Math.tan(-t.beta() * Math.PI / 180.0);
		double deltaX = horizontalDistance * Math.sin(-t.alpha() * Math.PI / 180.0);
		double deltaZ = horizontalDistance * Math.cos(t.alpha() * Math.PI / 180.0);
		deltaX = Math.max(Math.min(deltaX, 350), -350);
		deltaZ = Math.max(Math.min(deltaZ, 350), -350);
		return new StructurePosition((int) (t.xInOverworld() + deltaX), (int) (t.zInOverworld() + deltaZ), dataState.playerPosition());
	}

	private synchronized void setFossil(Fossil f) {
		try (ILock lock = modificationLock.acquireWritePermission()) {
			dataState.setFossil(f);
		}
	}

	private synchronized void onCalculatorSettingsChanged() {
		CalculatorSettings calculatorSettings = new CalculatorSettings(preferences);
		try (ILock lock = modificationLock.acquireWritePermission()) {
			dataState.setCalculator(new Calculator(calculatorSettings));
		}
	}

	private synchronized void updateAllAdvancementsMode() {
		IMinecraftWorldFile world = activeInstanceProvider.activeMinecraftWorld().get();
		boolean allAdvancementsModeEnabled = false;
		if (world != null) {
			allAdvancementsModeEnabled = preferences.allAdvancements.get() && world.hasEnteredEnd();
		}

		try (ILock lock = modificationLock.acquireWritePermission()) {
			dataState.allAdvancementsDataState.setAllAdvancementsModeEnabled(allAdvancementsModeEnabled);
		}
	}

	private synchronized void setStdProfile(int profileNumber, double std) {
		try (ILock lock = modificationLock.acquireWritePermission()) {
			stdProfile.setStd(profileNumber, std);
		}
	}

	@Override
	public void addThrowStream(ISubscribable<IThrow> stream) {
		stream.subscribe(this::onNewThrow);
	}

	@Override
	public void addFossilStream(ISubscribable<Fossil> stream) {
		stream.subscribe(this::setFossil);
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
		disposeHandler.dispose();
	}

}
