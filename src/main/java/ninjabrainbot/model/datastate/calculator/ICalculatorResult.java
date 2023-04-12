package ninjabrainbot.model.datastate.calculator;

import java.util.List;

import ninjabrainbot.model.datastate.stronghold.Chunk;
import ninjabrainbot.model.datastate.stronghold.ChunkPrediction;
import ninjabrainbot.event.IDisposable;

public interface ICalculatorResult extends IDisposable {

	ChunkPrediction getBestPrediction();

	List<ChunkPrediction> getTopPredictions();

	List<Chunk> getTopChunks();

	boolean success();

}
