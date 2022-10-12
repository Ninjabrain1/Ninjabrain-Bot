package ninjabrainbot.calculator;

import java.util.List;

import ninjabrainbot.calculator.statistics.Posterior;
import ninjabrainbot.util.IObservable;
import ninjabrainbot.util.ISet;
import ninjabrainbot.util.ModifiableSet;

public class CalculatorResult implements ICalculatorResult {
	
	private final ChunkPrediction bestPrediction;
	private final ModifiableSet<ChunkPrediction> topPredictions;
	private final double[] angleErrors;

	public CalculatorResult() {
		bestPrediction = new ChunkPrediction();
		topPredictions = new ModifiableSet<ChunkPrediction>();
		angleErrors = null;
	}
	
	public CalculatorResult(Posterior posterior, ISet<IThrow> eyeThrows, IObservable<IThrow> playerPos, int numPredictions) {
		// Find chunk with largest posterior probability
		Chunk predictedChunk = posterior.getMostProbableChunk();
		bestPrediction = new ChunkPrediction(predictedChunk, playerPos);
		angleErrors = bestPrediction.getAngleErrors(eyeThrows);
		topPredictions = createTopPredictions(posterior, playerPos, numPredictions);
	}
	
	public ChunkPrediction getBestPrediction() {
		return bestPrediction;
	}
	
	public double[] getAngleErrors() {
		return angleErrors;
	}
	
	@Override
	public ModifiableSet<ChunkPrediction> getTopPredictions() {
		return topPredictions;
	}
	
	public boolean success() {
		return bestPrediction.success;
	}
	
	private ModifiableSet<ChunkPrediction> createTopPredictions(Posterior posterior, IObservable<IThrow> playerPos, int amount) {
		ModifiableSet<ChunkPrediction> topPredictions = new ModifiableSet<ChunkPrediction>(); 
		List<Chunk> topChunks = posterior.getChunks();
		topChunks.sort((a, b) -> -Double.compare(a.weight, b.weight));
		for (Chunk c : topChunks) {
			topPredictions.add(new ChunkPrediction(c, playerPos));
			if (topPredictions.size() >= amount)
				break;
		}
		return topPredictions;
	}

	@Override
	public void dispose() {
		bestPrediction.dispose();
		topPredictions.dispose();
	}
	
}
