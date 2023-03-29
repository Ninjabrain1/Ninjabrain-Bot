package ninjabrainbot.data.alladvancements;

import ninjabrainbot.data.common.StructurePosition;
import ninjabrainbot.data.datalock.IModificationLock;
import ninjabrainbot.data.datalock.LockableField;
import ninjabrainbot.data.stronghold.ChunkPrediction;
import ninjabrainbot.event.IObservable;
import ninjabrainbot.event.ObservableField;

public class AllAdvancementsDataState implements IAllAdvancementsDataState {

	private final IObservable<ChunkPrediction> currentStrongholdPrediction;

	private final ObservableField<Boolean> allAdvancementsModeEnabled;
	private final ObservableField<ChunkPrediction> strongholdChunk;
	private final ObservableField<StructurePosition> outpostPosition;
	private final ObservableField<StructurePosition> monumentPosition;

	public AllAdvancementsDataState(IObservable<ChunkPrediction> currentStrongholdPrediction, IModificationLock modificationLock) {
		this.currentStrongholdPrediction = currentStrongholdPrediction;
		allAdvancementsModeEnabled = new LockableField<>(false, modificationLock);
		strongholdChunk = new LockableField<>(null, modificationLock);
		outpostPosition = new LockableField<>(null, modificationLock);
		monumentPosition = new LockableField<>(null, modificationLock);
	}

	public void setAllAdvancementsModeEnabled(boolean enabled) {
		if (enabled) {
			strongholdChunk.set(currentStrongholdPrediction.get());
		}
		allAdvancementsModeEnabled.set(enabled);
	}

	public void reset() {
		allAdvancementsModeEnabled.set(false);
		strongholdChunk.set(null);
	}

	@Override
	public IObservable<Boolean> allAdvancementsModeEnabled() {
		return allAdvancementsModeEnabled;
	}

	@Override
	public IObservable<ChunkPrediction> getStrongholdChunk() {
		return strongholdChunk;
	}

}
