package ninjabrainbot.model.datastate.alladvancements;

import ninjabrainbot.model.datastate.common.StructurePosition;
import ninjabrainbot.model.datastate.stronghold.ChunkPrediction;
import ninjabrainbot.model.domainmodel.DataComponent;
import ninjabrainbot.model.domainmodel.IDataComponent;
import ninjabrainbot.model.domainmodel.IDomainModel;
import ninjabrainbot.event.IObservable;

public class AllAdvancementsDataState implements IAllAdvancementsDataState {

	private final IObservable<ChunkPrediction> currentStrongholdPrediction;

	private final DataComponent<Boolean> allAdvancementsModeEnabled;
	private final DataComponent<StructurePosition> strongholdPosition;
	private final DataComponent<StructurePosition> spawnPosition;
	private final DataComponent<StructurePosition> outpostPosition;
	private final DataComponent<StructurePosition> monumentPosition;

	public AllAdvancementsDataState(IObservable<ChunkPrediction> currentStrongholdPrediction, IDomainModel domainModel) {
		this.currentStrongholdPrediction = currentStrongholdPrediction;
		allAdvancementsModeEnabled = new DataComponent<>(domainModel, false);
		strongholdPosition = new DataComponent<>(domainModel);
		spawnPosition = new DataComponent<>(domainModel);
		outpostPosition = new DataComponent<>(domainModel);
		monumentPosition = new DataComponent<>(domainModel);
	}

	public void setAllAdvancementsModeEnabled(boolean enabled) {
		if (enabled) {
			strongholdPosition.set(currentStrongholdPrediction.get());
		}
		allAdvancementsModeEnabled.set(enabled);
	}

	@Override
	public IDataComponent<Boolean> allAdvancementsModeEnabled() {
		return allAdvancementsModeEnabled;
	}

	@Override
	public IDataComponent<StructurePosition> strongholdPosition() {
		return strongholdPosition;
	}

	@Override
	public IDataComponent<StructurePosition> spawnPosition() {
		return spawnPosition;
	}

	@Override
	public IDataComponent<StructurePosition> outpostPosition() {
		return outpostPosition;
	}

	@Override
	public IDataComponent<StructurePosition> monumentPosition() {
		return monumentPosition;
	}

}
