package ninjabrainbot.calculator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import ninjabrainbot.Main;
import ninjabrainbot.util.Coords;
import ninjabrainbot.util.Profiler;

public class Posterior {
	
	IPrior prior;
	ArrayList<Chunk> chunks;
	double sigma, sigmaAlt, sigmaManual;
	
	public Posterior(double sigma, double sigmaAlt, double sigmaManual, List<Throw> eyeThrows, DivineContext divineContext) {
		Profiler.clear();
		Profiler.start("Calculate posterior");
		this.sigma = sigma;
		this.sigmaAlt = sigmaAlt;
		this.sigmaManual = sigmaManual;
		Profiler.start("Calculate prior");
		double sigma0 = getSTD(eyeThrows.get(0));
		prior = new RayApproximatedPrior(eyeThrows.get(0), Math.min(1.0, 30 * sigma0) / 180.0 * Math.PI, divineContext);
		Profiler.stopAndStart("Determine constants");
		chunks = new ArrayList<Chunk>();
		double px = eyeThrows.get(0).x;
		double pz = eyeThrows.get(0).z;
		double maxDist = StrongholdConstants.getMaxDistance(px, pz) / 16.0;
		double maxDist2 = maxDist * maxDist;
		Profiler.stopAndStart("Copy chunks from prior");
		for (Chunk c : prior.getChunks()) {
			Chunk clone = c.clone();
			double dx = clone.x - px / 16.0;
			double dz = clone.z - pz / 16.0;
			if (dx * dx + dz * dz > maxDist2) {
				clone.weight = 0;
			}
			chunks.add(clone);
		}
		Profiler.stopAndStart("Measurement error conditioning");
		for (Throw t : eyeThrows) {
			condition(t);
		}
		Profiler.stopAndStart("Closest stronghold conditioning");
		if (Main.preferences.useAdvStatistics.get())
			closestStrongholdCondition(eyeThrows.get(0), 0.001);
		Profiler.stop();
		Profiler.print();
	}
	
	private double getSTD(Throw t) {
		if (t.manualInput)
			return sigmaManual;
		return t.altStd ? sigmaAlt : sigma;
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
		double deltax = chunk.x * 16 + StrongholdConstants.getStrongholdChunkCoord() - t.x;
		double deltaz = chunk.z * 16 + StrongholdConstants.getStrongholdChunkCoord() - t.z;
		double gamma = -180 / Math.PI * Math.atan2(deltax, deltaz); // mod 360 necessary?
		double delta = Math.abs((gamma - t.alpha) % 360.0);
		delta = Math.min(delta, 360.0 - delta);
		double s = getSTD(t);
		chunk.weight *= Math.exp(-delta * delta / (2 * s * s));
	}
	
	public List<Chunk> getChunks() {
		return chunks;
	}
	
	/**
	 * Conditions all chunk weights on the fact that the stronghold is closer than any other stronghold.
	 * This action is relatively costly, and is approximated for all chunks below the given threshold.
	 * @param probabilityTheshold
	 */
	private void closestStrongholdCondition(Throw t, double probabilityTheshold) {
		// Update weights
//		chunks.forEach((chunk) -> closestStrongholdCondition(chunk, t));
		Profiler.start("Sort chunks");
		chunks.sort((c1, c2) -> -Double.compare(c1.weight, c2.weight));
		double totalClosestStrongholdProbability = 0;
		int samples = 0;
		Profiler.stopAndStart("Calculate closest stronghold probability");
		for (int i = 0; i < chunks.size(); i++) {
			Chunk c = chunks.get(i);
			if (i < 100 || c.weight > probabilityTheshold) {
				double a = closestStrongholdCondition(c, t);
				totalClosestStrongholdProbability += a;
				samples++;
			} else {
				c.weight *= totalClosestStrongholdProbability/samples; // Approximation, no need to be precise for chunks that dont matter
			}
		}
		Profiler.stopAndStart("Normalize");
		// Normalize
		double weightSum = 0.0;
		for (Chunk chunk : chunks) {
			weightSum += chunk.weight;
		}
		final double totalWeight = weightSum;
		chunks.forEach((chunk) -> chunk.weight /= totalWeight);
		Profiler.stop();
	}
	
	int K = 7;
	private double closestStrongholdCondition(Chunk chunk, Throw t) {
		double closestStrongholdProbability = 1;
		double deltax = chunk.x + (StrongholdConstants.getStrongholdChunkCoord() - t.x)/16.0;
		double deltaz = chunk.z + (StrongholdConstants.getStrongholdChunkCoord() - t.z)/16.0;
		double r_p = Math.sqrt(t.x * t.x + t.z * t.z)/16.0;
		double d_i = Math.sqrt(deltax * deltax + deltaz * deltaz);
		double phi_prime = Coords.getPhi(chunk.x, chunk.z);
		double phi_p = Coords.getPhi(t.x, t.z);
		double maxDist = StrongholdConstants.getMaxDistance(t.x, t.z) / 16.0;
		double stronghold_r_min = r_p - maxDist;
		double stronghold_r_max = r_p + maxDist;
		Ring ring_chunk = Ring.get(Math.sqrt(chunk.x * chunk.x + chunk.z * chunk.z));
		if (ring_chunk == null) {
			return 0;
		}
		for (int i = 0; i < StrongholdConstants.numRings; i++) {
			Ring ring = Ring.get(i);
			if (stronghold_r_max < ring.innerRadius || stronghold_r_min > ring.outerRadius)
				continue;
			boolean sameRing = ring_chunk.ring == ring.ring;
			double ak = ring_chunk.innerRadius;
			double dphi = sameRing ? (2.0/(2.0 * K + 1) * 15 * Math.sqrt(2) / ak) : (2.0/(2.0 * K + 1) * Math.PI / ring.numStrongholds);
			for (int l = 0; l < ring.numStrongholds; l++) {
				if (sameRing && l == 0) {
					continue;
				}
				double integral = integral(ring, l, phi_prime, dphi,phi_p, r_p, d_i, sameRing);
				closestStrongholdProbability *= 1.0 - integral;
			}
		}
		chunk.weight *= closestStrongholdProbability;
		return closestStrongholdProbability;
	}
	
	private double integral(Ring ring, int l, double phi_prime, double dphi, double phi_p, double r_p, double d_i, boolean sameRingAsChunk) {
		double phi_prime_l_mu = phi_prime + (l * 2 * Math.PI / ring.numStrongholds);
		double pdfint = 0;
		double integral = 0;
		for (int k = -K; k <= K; k++) {
			double delta_phi = k * dphi;
			double pdf = 0;
			if (sameRingAsChunk) {
				pdf = Math.pow(1 + delta_phi * ring.innerRadius / (15 * Math.sqrt(2)), 4.5) * Math.pow(1 - delta_phi * ring.innerRadius / (15 * Math.sqrt(2)), 4.5);
			} else {
				pdf = 1;
			}
			pdfint += pdf * dphi;
			double phi_prime_l = phi_prime_l_mu + k * dphi;
			double gamma = phi_p - phi_prime_l;
			double sin_beta = r_p / d_i * Math.sin(gamma);
			if (sin_beta < 1.0 && sin_beta > -1.0) {
				double beta = Math.asin(sin_beta);
				double alpha0 = beta - gamma;
				double alpha1 = Math.PI - gamma - beta;
				double R0 = d_i * Math.sin(alpha0) / Math.sin(gamma);
				double R1 = d_i * Math.sin(alpha1) / Math.sin(gamma);
				if (R1 > ring.outerRadiusPostSnapping)
					R1 = ring.outerRadiusPostSnapping;
				if (R0 < ring.innerRadiusPostSnapping)
					R0 = ring.innerRadiusPostSnapping;
				if (R0 > ring.outerRadiusPostSnapping)
					R0 = ring.outerRadiusPostSnapping;
				if (R1 < ring.innerRadiusPostSnapping)
					R1 = ring.innerRadiusPostSnapping;
				integral += pdf * (ApproximatedDensity.cumulativePolar(R1) - ApproximatedDensity.cumulativePolar(R0)) * dphi / ring.numStrongholds;
			} // else integrand is 0
		}
		integral /= pdfint;
		if (integral > 1.0)
			integral = 1.0;
		return integral;
	}
	
}
