package ninjabrainbot.data.calculator;

import java.util.List;

import ninjabrainbot.data.calculator.stronghold.Chunk;
import ninjabrainbot.data.calculator.stronghold.ChunkPrediction;
import ninjabrainbot.event.IDisposable;

public interface ICalculatorResult extends IDisposable {

	public ChunkPrediction getBestPrediction();

	public List<ChunkPrediction> getTopPredictions();

	public List<Chunk> getTopChunks();

	public boolean success();

}
