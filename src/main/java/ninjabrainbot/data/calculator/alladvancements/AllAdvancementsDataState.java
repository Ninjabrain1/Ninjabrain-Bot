package ninjabrainbot.data.calculator.alladvancements;

import ninjabrainbot.data.calculator.common.StructurePosition;
import ninjabrainbot.data.calculator.stronghold.ChunkPrediction;
import ninjabrainbot.data.temp.DataComponent;
import ninjabrainbot.data.temp.IDomainModel;
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
