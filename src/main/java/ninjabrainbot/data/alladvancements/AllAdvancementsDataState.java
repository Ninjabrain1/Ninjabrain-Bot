package ninjabrainbot.data.alladvancements;

import ninjabrainbot.data.datalock.IModificationLock;
import ninjabrainbot.data.datalock.LockableField;
import ninjabrainbot.data.stronghold.ChunkPrediction;
import ninjabrainbot.event.IObservable;
import ninjabrainbot.event.ObservableField;

public class AllAdvancementsDataState implements IAllAdvancementsDataState {

	private final IObservable<ChunkPrediction> currentStrongholdPrediction;

	private final ObservableField<Boolean> allAdvancementsModeEnabled;
	private final ObservableField<ChunkPrediction> strongholdChunk;

	public AllAdvancementsDataState(IObservable<ChunkPrediction> currentStrongholdPrediction, IModificationLock modificationLock) {
		this.currentStrongholdPrediction = currentStrongholdPrediction;
		allAdvancementsModeEnabled = new LockableField<Boolean>(false, modificationLock);
		strongholdChunk = new LockableField<ChunkPrediction>(null, modificationLock);
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
