package ninjabrainbot.calculator;

import ninjabrainbot.util.IDisposable;
import ninjabrainbot.util.ModifiableSet;

public interface ICalculatorResult extends IDisposable {
	
	public ChunkPrediction getBestPrediction();
	
	public ModifiableSet<ChunkPrediction> getTopPredictions();
	
	public double[] getAngleErrors();
	
	public boolean success();
	
}
