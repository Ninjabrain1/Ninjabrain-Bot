package ninjabrainbot.data.alladvancements;

import ninjabrainbot.data.common.StructurePosition;
import ninjabrainbot.data.stronghold.ChunkPrediction;
import ninjabrainbot.event.IObservable;

public interface IAllAdvancementsDataState {

	IObservable<Boolean> allAdvancementsModeEnabled();

	IObservable<StructurePosition> strongholdPosition();

	IObservable<StructurePosition> spawnPosition();

	IObservable<StructurePosition> outpostPosition();

	IObservable<StructurePosition> monumentPosition();

}
