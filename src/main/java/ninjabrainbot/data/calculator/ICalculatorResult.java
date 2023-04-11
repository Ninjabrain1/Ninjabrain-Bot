package ninjabrainbot.data.calculator;

import java.util.List;

import ninjabrainbot.data.calculator.stronghold.Chunk;
import ninjabrainbot.data.calculator.stronghold.ChunkPrediction;
import ninjabrainbot.event.IDisposable;

public interface ICalculatorResult extends IDisposable {

	ChunkPrediction getBestPrediction();

	List<ChunkPrediction> getTopPredictions();

	List<Chunk> getTopChunks();

	boolean success();

}
