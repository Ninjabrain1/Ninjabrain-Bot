package ninjabrainbot.model.datastate.calculator;

import java.util.List;

import ninjabrainbot.event.IDisposable;
import ninjabrainbot.model.datastate.stronghold.Chunk;
import ninjabrainbot.model.datastate.stronghold.ChunkPrediction;

public interface ICalculatorResult extends IDisposable {

	ChunkPrediction getBestPrediction();

	List<ChunkPrediction> getTopPredictions();

	List<Chunk> getTopChunks();

	boolean success();

}
