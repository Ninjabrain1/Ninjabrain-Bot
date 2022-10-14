package ninjabrainbot.calculator;

import java.util.List;

import ninjabrainbot.util.IDisposable;

public interface ICalculatorResult extends IDisposable {
	
	public ChunkPrediction getBestPrediction();
	
	public List<ChunkPrediction> getTopPredictions();
	
	public boolean success();
	
}
