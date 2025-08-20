package ninjabrainbot.model.datastate.statistics;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import ninjabrainbot.event.IReadOnlyList;
import ninjabrainbot.io.preferences.enums.McVersion;
import ninjabrainbot.model.datastate.divine.IDivineContext;
import ninjabrainbot.model.datastate.endereye.IEnderEyeThrow;
import ninjabrainbot.model.datastate.stronghold.Chunk;
import ninjabrainbot.model.datastate.stronghold.Ring;
import ninjabrainbot.model.datastate.stronghold.StrongholdConstants;
import ninjabrainbot.model.environmentstate.StandardDeviationSettings;
import ninjabrainbot.util.Coords;

public class Posterior {

	private final StandardDeviationSettings standardDeviationSettings;
	private final McVersion version;

	final IPrior prior;
	final ArrayList<Chunk> chunks;

	public Posterior(IReadOnlyList<IEnderEyeThrow> eyeThrows, IDivineContext divineContext, StandardDeviationSettings standardDeviationSettings, boolean useAdvStatistics, McVersion version) {
		this.standardDeviationSettings = standardDeviationSettings;
		this.version = version;
		double sigma0 = eyeThrows.get(0).getStandardDeviation(standardDeviationSettings);
		prior = new RayApproximatedPrior(eyeThrows.get(0), Math.min(1.0, 30 * sigma0) / 180.0 * Math.PI, divineContext, version);
		chunks = new ArrayList<Chunk>();
		double px = eyeThrows.get(0).xInOverworld();
		double pz = eyeThrows.get(0).zInOverworld();
		double maxDist = StrongholdConstants.getMaxDistance(px, pz) / 16.0;
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
		for (IEnderEyeThrow t : eyeThrows) {
			condition(t);
		}
		if (useAdvStatistics)
			closestStrongholdCondition(eyeThrows.get(0), 0.001);
	}

	public void condition(IEnderEyeThrow t) {
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
	 * Returns the closest distance to any chunk that has a stronghold with
	 * probability greater than the given tolerance.
	 */
	public double getMinDistance(double tolerance, IEnderEyeThrow position) {
		return getClosestPossibleChunk(tolerance, position).getOverworldDistance(version, position);
	}

	/**
	 * Returns the closest chunk that has a stronghold with probability greater than
	 * the given tolerance.
	 */
	public Chunk getClosestPossibleChunk(double tolerance, IEnderEyeThrow position) {
		Chunk closest = null;
		double minDist = Double.POSITIVE_INFINITY;
		for (Chunk c : chunks) {
			if (c.weight > tolerance) {
				double dist = c.getOverworldDistance(version, position);
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

	private void updateConditionalProbability(Chunk chunk, IEnderEyeThrow t) {
		double deltax = chunk.x * 16 + StrongholdConstants.getStrongholdChunkCoord(version) - t.xInOverworld();
		double deltaz = chunk.z * 16 + StrongholdConstants.getStrongholdChunkCoord(version) - t.zInOverworld();
		double gamma = -180 / Math.PI * Math.atan2(deltax, deltaz); // mod 360 necessary?
		double delta = Math.abs((gamma - t.horizontalAngle()) % 360.0);
		delta = Math.min(delta, 360.0 - delta);
		double s1 = t.getStandardDeviation(standardDeviationSettings);
		double v2 = getVarianceFromPositionImprecision(deltax * deltax + deltaz * deltaz, t.xInOverworld(), t.zInOverworld());
		double s = Math.sqrt(s1 * s1 + v2);
		chunk.weight *= Math.exp(-delta * delta / (2 * s * s));
	}

	private double getVarianceFromPositionImprecision(double distance2, double throwX, double throwZ) {
		// Check if the throw was against a block corner
		if ((Math.abs(throwX - Math.floor(throwX) - 0.3) < 1e-6 || Math.abs(throwX - Math.floor(throwX) - 0.7) < 1e-6) &&
			(Math.abs(throwZ - Math.floor(throwZ) - 0.3) < 1e-6 || Math.abs(throwZ - Math.floor(throwZ) - 0.7) < 1e-6)) {
			return 0;
		}
		// Assume the error is uniformly distributed for simplicity
		double maxLateralError = 0.005 * Math.sqrt(2) * 180 / Math.PI;
		return maxLateralError * maxLateralError / distance2 / 6; // Variance for uniform distribution
	}

	public List<Chunk> getChunks() {
		return chunks;
	}

	/**
	 * Conditions all chunk weights on the fact that the stronghold is closer than
	 * any other stronghold. This action is relatively costly, and is approximated
	 * for all chunks below the given threshold.
	 *
	 * @param probabilityTheshold
	 */
	private void closestStrongholdCondition(IEnderEyeThrow t, double probabilityTheshold) {
		// Update weights
		chunks.sort((c1, c2) -> -Double.compare(c1.weight, c2.weight));
		double totalClosestStrongholdProbability = 0;
		int samples = 0;
		for (int i = 0; i < chunks.size(); i++) {
			Chunk c = chunks.get(i);
			if (i < 100 || c.weight > probabilityTheshold) {
				double a = closestStrongholdCondition(c, t);
				totalClosestStrongholdProbability += a;
				samples++;
			} else {
				c.weight *= totalClosestStrongholdProbability / samples; // Approximation, no need to be precise for chunks that dont matter
			}
		}
		// Normalize
		double weightSum = 0.0;
		for (Chunk chunk : chunks) {
			weightSum += chunk.weight;
		}
		final double totalWeight = weightSum;
		chunks.forEach((chunk) -> chunk.weight /= totalWeight);
	}

	final int K = 7;

	private double closestStrongholdCondition(Chunk chunk, IEnderEyeThrow t) {
		double closestStrongholdProbability = 1;
		double deltax = chunk.x + (StrongholdConstants.getStrongholdChunkCoord(version) - t.xInOverworld()) / 16.0;
		double deltaz = chunk.z + (StrongholdConstants.getStrongholdChunkCoord(version) - t.zInOverworld()) / 16.0;
		double r_p = Math.sqrt(t.xInOverworld() * t.xInOverworld() + t.zInOverworld() * t.zInOverworld()) / 16.0;
		double d_i = Math.sqrt(deltax * deltax + deltaz * deltaz);
		double phi_prime = Coords.getPhi(chunk.x, chunk.z);
		double phi_p = Coords.getPhi(t.xInOverworld(), t.zInOverworld());
		double maxDist = StrongholdConstants.getMaxDistance(t.xInOverworld(), t.zInOverworld()) / 16.0;
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
			double dphi = sameRing ? (2.0 / (2.0 * K + 1) * 15 * Math.sqrt(2) / ak) : (2.0 / (2.0 * K + 1) * Math.PI / ring.numStrongholds);
			for (int l = 0; l < ring.numStrongholds; l++) {
				if (sameRing && l == 0) {
					continue;
				}
				double integral = integral(ring, l, phi_prime, dphi, phi_p, r_p, d_i, sameRing);
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
