package ninjabrainbot.calculator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class Posterior {
	
	IPrior prior;
	ArrayList<Chunk> chunks;
	double sigma;
	
	public Posterior(double sigma, List<Throw> eyeThrows) {
		this.sigma = sigma;
		prior = new RayApproximatedPrior(eyeThrows.get(0));
		chunks = new ArrayList<Chunk>();
		double px = eyeThrows.get(0).x;
		double pz = eyeThrows.get(0).z;
		double maxDist = StrongholdConstants.getMaxDistance(pz, pz) / 16.0;
		double maxDist2 = maxDist * maxDist;
		for (Chunk c : prior.getChunks()) {
			Chunk clone = c.clone();
			double dx = clone.x - px / 16.0;
			double dz = clone.z - pz / 16.0;
			if (dx * dx + dz * dz > maxDist2) {
				clone.weight = 0;
			}
			chunks.add(clone);
		}
		for (Throw t : eyeThrows) {
			condition(t);
		}
	}
	
	public void condition(Throw t) {
		// Update weights
		chunks.forEach((chunk) -> updateConditionalProbability(chunk, t));
		// Normalize
		double weightSum = 0.0;
		for (Chunk chunk : chunks) {
			weightSum += chunk.weight;
		}
		final double totalWeight = weightSum;
		chunks.forEach((chunk) -> chunk.weight /= totalWeight);
	}
	
	/**
	 * Returns the closest distance to any chunk that has a stronghold 
	 * with probability greater than the given tolerance.
	 */
	public double getMinDistance(double tolerance, Throw position) {
		return getClosestPossibleChunk(tolerance, position).getDistance(position);
	}
	
	/**
	 * Returns the closest chunk that has a stronghold 
	 * with probability greater than the given tolerance.
	 */
	public Chunk getClosestPossibleChunk(double tolerance, Throw position) {
		Chunk closest = null;
		double minDist = Double.POSITIVE_INFINITY;
		for (Chunk c : chunks) {
			if (c.weight > tolerance) {
				double dist = c.getDistance(position);
				if (dist < minDist) {
					minDist = dist;
					closest = c;
				}
			}
		}
		return closest;
	}
	
	public Chunk getMostProbableChunk() {
		Optional<Chunk> prediction = getChunks().stream().max(Comparator.comparingDouble((chunk) -> chunk.weight));
		try {
			return prediction.get();
		} catch (Exception e) {
			return new Chunk(0, 0);
		}
	}
	
	private void updateConditionalProbability(Chunk chunk, Throw t) {
		double deltax = chunk.x * 16 + 8 - t.x;
		double deltaz = chunk.z * 16 + 8 - t.z;
		double gamma = -180 / Math.PI * Math.atan2(deltax, deltaz); // mod 360 necessary?
		double delta = Math.abs((gamma - t.alpha) % 360.0);
		delta = Math.min(delta, 360.0 - delta);
		chunk.weight *= Math.exp(-delta * delta / (2 * sigma * sigma));
	}
	
	public List<Chunk> getChunks() {
		return chunks;
	}
	
}
