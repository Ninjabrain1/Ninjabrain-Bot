package ninjabrainbot.calculator;

import java.util.ArrayList;
import java.util.List;

public class CalculatorResult {
	
	Posterior posterior;
	ArrayList<Throw> eyeThrows;
	ChunkPrediction bestPrediction = new ChunkPrediction();
	private final List<ChunkPrediction> topPredictions = new ArrayList<>();
	private Throw playerPos;

	public CalculatorResult() {}
	
	public CalculatorResult(Posterior posterior, ArrayList<Throw> eyeThrows, Throw playerPos) {
		this.posterior = posterior;
		this.eyeThrows = new ArrayList<Throw>();
		this.playerPos = playerPos;
		for (Throw t : eyeThrows) {
			this.eyeThrows.add(t);
		}
		// Find chunk with largest posterior probability
		Chunk predictedChunk = posterior.getMostProbableChunk();
		bestPrediction = new ChunkPrediction(predictedChunk, playerPos);
	}
	
	public List<ChunkPrediction> getTopPredictions(int amount) {
		if (topPredictions.isEmpty()) {
			List<Chunk> topChunks = posterior.getChunks();
			topChunks.sort((a, b) -> -Double.compare(a.weight, b.weight));
			for (Chunk c : topChunks) {
				topPredictions.add(new ChunkPrediction(c, playerPos));
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
