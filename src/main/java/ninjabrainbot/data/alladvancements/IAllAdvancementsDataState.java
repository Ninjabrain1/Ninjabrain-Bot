package ninjabrainbot.data.alladvancements;

import ninjabrainbot.data.stronghold.ChunkPrediction;
import ninjabrainbot.event.IObservable;

public interface IAllAdvancementsDataState {

	IObservable<Boolean> allAdvancementsModeEnabled();

	IObservable<ChunkPrediction> getStrongholdChunk();

}
