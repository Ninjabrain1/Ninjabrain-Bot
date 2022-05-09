package ninjabrainbot.calculator;

import java.util.ArrayList;
import java.util.List;

public class CalculatorResult {
	
	Posterior posterior;
	ArrayList<Throw> eyeThrows;
	ChunkPrediction bestPrediction = new ChunkPrediction();
	private final List<ChunkPrediction> topPredictions = new ArrayList<>();

	public CalculatorResult() {}
	
	public CalculatorResult(Posterior posterior, ArrayList<Throw> eyeThrows) {
		this.posterior = posterior;
		this.eyeThrows = new ArrayList<Throw>();
		for (Throw t : eyeThrows) {
			this.eyeThrows.add(t);
		}
		// Find chunk with largest posterior probability
		Chunk predictedChunk = posterior.getMostProbableChunk();
		bestPrediction = new ChunkPrediction(predictedChunk, eyeThrows.get(eyeThrows.size() - 1));
	}
	
	public List<ChunkPrediction> getTopPredictions(int amount) {
		if (topPredictions.isEmpty()) {
			List<Chunk> topChunks = posterior.getChunks();
			topChunks.sort((a, b) -> -Double.compare(a.weight, b.weight));
			for (Chunk c : topChunks) {
				topPredictions.add(new ChunkPrediction(c, eyeThrows.get(eyeThrows.size() - 1)));
				if (topPredictions.size() >= amount)
					break;
			}
		}
		return topPredictions;
	}
	
	public ChunkPrediction getBestPrediction() {
		return bestPrediction;
	}
	
	public double[] getAngleErrors() {
		return bestPrediction.getAngleErrors(eyeThrows);
	}
	
	public boolean success() {
		return bestPrediction.success;
	}
	
}
