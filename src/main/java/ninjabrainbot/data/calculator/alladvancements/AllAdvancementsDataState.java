package ninjabrainbot.data.calculator.alladvancements;

import ninjabrainbot.data.calculator.common.StructurePosition;
import ninjabrainbot.data.datalock.IModificationLock;
import ninjabrainbot.data.datalock.LockableField;
import ninjabrainbot.data.calculator.stronghold.ChunkPrediction;
import ninjabrainbot.event.IObservable;
import ninjabrainbot.event.ObservableField;

public class AllAdvancementsDataState implements IAllAdvancementsDataState {

	private final IObservable<ChunkPrediction> currentStrongholdPrediction;

	private final ObservableField<Boolean> allAdvancementsModeEnabled;
	private final ObservableField<StructurePosition> strongholdPosition;
	private final ObservableField<StructurePosition> spawnPosition;
	private final ObservableField<StructurePosition> outpostPosition;
	private final ObservableField<StructurePosition> monumentPosition;

	public AllAdvancementsDataState(IObservable<ChunkPrediction> currentStrongholdPrediction, IModificationLock modificationLock) {
		this.currentStrongholdPrediction = currentStrongholdPrediction;
		allAdvancementsModeEnabled = new LockableField<>(false, modificationLock);
		strongholdPosition = new LockableField<>(null, modificationLock);
		spawnPosition = new LockableField<>(null, modificationLock);
		outpostPosition = new LockableField<>(null, modificationLock);
		monumentPosition = new LockableField<>(null, modificationLock);
	}

	public void setAllAdvancementsModeEnabled(boolean enabled) {
		if (enabled) {
			strongholdPosition.set(currentStrongholdPrediction.get());
		}
		allAdvancementsModeEnabled.set(enabled);
	}

	public void reset() {
		allAdvancementsModeEnabled.set(false);
		strongholdPosition.set(null);
		spawnPosition.set(null);
		outpostPosition.set(null);
		monumentPosition.set(null);
	}

	public void setSpawnPosition(StructurePosition structurePosition) {
		spawnPosition.set(structurePosition);
	}

	public void setMonumentPosition(StructurePosition structurePosition) {
		monumentPosition.set(structurePosition);
	}

	public void setOutpostPosition(StructurePosition structurePosition) {
		outpostPosition.set(structurePosition);
	}

	@Override
	public IObservable<Boolean> allAdvancementsModeEnabled() {
		return allAdvancementsModeEnabled;
	}

	@Override
	public IObservable<StructurePosition> strongholdPosition() {
		return strongholdPosition;
	}

	@Override
	public IObservable<StructurePosition> spawnPosition() {
		return spawnPosition;
	}

	@Override
	public IObservable<StructurePosition> outpostPosition() {
		return outpostPosition;
	}

	@Override
	public IObservable<StructurePosition> monumentPosition() {
		return monumentPosition;
	}

}
