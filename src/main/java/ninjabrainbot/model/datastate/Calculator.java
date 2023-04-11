package ninjabrainbot.model.datastate;

import java.util.ArrayList;
import java.util.List;

import ninjabrainbot.model.datastate.blind.BlindPosition;
import ninjabrainbot.model.datastate.blind.BlindResult;
import ninjabrainbot.model.datastate.common.IPlayerPosition;
import ninjabrainbot.model.datastate.divine.DivineResult;
import ninjabrainbot.model.datastate.divine.Fossil;
import ninjabrainbot.model.datastate.divine.IDivineContext;
import ninjabrainbot.model.datastate.endereye.IEnderEyeThrow;
import ninjabrainbot.model.datastate.statistics.Posterior;
import ninjabrainbot.model.datastate.statistics.Prior;
import ninjabrainbot.model.datastate.stronghold.Chunk;
import ninjabrainbot.model.datastate.stronghold.Ring;
import ninjabrainbot.event.IObservable;
import ninjabrainbot.event.IReadOnlyList;
import ninjabrainbot.io.preferences.MultipleChoicePreferenceDataTypes;
import ninjabrainbot.util.Coords;
import ninjabrainbot.util.Logger;
import ninjabrainbot.util.Pair;

public class Calculator implements ICalculator {

	private final int numberOfReturnedPredictions;
	private final boolean useAdvancedStatistics;
	private final MultipleChoicePreferenceDataTypes.McVersion mcVersion;

	public Calculator() {
		this(new CalculatorSettings());
	}

	public Calculator(CalculatorSettings settings) {
		numberOfReturnedPredictions = settings.numberOfReturnedPredictions;
		useAdvancedStatistics = settings.useAdvancedStatistics;
		mcVersion = settings.mcVersion;
	}

	@Override
	public ICalculatorResult triangulate(IReadOnlyList<IEnderEyeThrow> eyeThrows, IObservable<IPlayerPosition> playerPos, IDivineContext divineContext) {
		if (eyeThrows.size() == 0)
			return null;
		long t0 = System.currentTimeMillis();
		// Calculate posteriors
		Posterior posterior = new Posterior(eyeThrows, divineContext, useAdvancedStatistics, mcVersion);
		Logger.log("Time to triangulate: " + (System.currentTimeMillis() - t0) / 1000f + " seconds.");
		return new CalculatorResult(posterior, eyeThrows, playerPos, numberOfReturnedPredictions, mcVersion);
	}

	public Posterior getPosterior(IReadOnlyList<IEnderEyeThrow> eyeThrows, IDivineContext divineContext) {
		if (eyeThrows.size() == 0)
			return null;
		return new Posterior(eyeThrows, divineContext, useAdvancedStatistics, mcVersion);
	}

	@Override
	public BlindResult blind(BlindPosition b, IDivineContext divineContext) {
		long t0 = System.currentTimeMillis();
		int distanceThreshold = 400;
		int h = 2;
		double phi_p = phi(b.x, b.z);
		double probability = getHighrollProbability(b.x, b.z, distanceThreshold, divineContext);
		// difference in x direction
		int deltaX1 = h;
		int deltaZ1 = 0;
		double probability1 = getHighrollProbability(b.x + deltaX1, b.z + deltaZ1, distanceThreshold, divineContext);
		double probabilityDerivative1 = (probability1 - probability) / Math.sqrt(deltaX1 * deltaX1 + deltaZ1 * deltaZ1);
		// difference in z direction
		int deltaX2 = deltaZ1;
		int deltaZ2 = -deltaX1;
		double probability2 = getHighrollProbability(b.x + deltaX2, b.z + deltaZ2, distanceThreshold, divineContext);
		double probabilityDerivative2 = (probability2 - probability) / Math.sqrt(deltaX1 * deltaX1 + deltaZ1 * deltaZ1);
		double probabilityDerivative = Math.sqrt(probabilityDerivative1 * probabilityDerivative1 + probabilityDerivative2 * probabilityDerivative2);
		double ninetiethPercentileDerivative = probabilityDerivative * Math.sqrt(0.1 / (2 * probability * probability * probability)) * distanceThreshold;
		double avgDist = getAverageDistance(b.x, b.z, 10, 20);
		double avgDist2 = getAverageDistance(b.x - h * Math.sin(phi_p), b.z + h * Math.cos(phi_p), 10, 20);
		double avgDistDerivative = (avgDist2 - avgDist) / h;
		// Optimal coords
		Ring closestRing = Ring.getClosestRings(b.x / 2.0, b.z / 2.0).fst;
		double optDist = (closestRing.innerRadius + distanceThreshold / 16.0) * 2.0;
		double optX = b.x;
		double optZ = b.z;
		double optHighrollProb = 0.1;
		if (divineContext != null && closestRing.ring == 0) {
			BlindPosition divinePos = divineContext.getClosestCoords(optX, optZ, optDist);
			optX = divinePos.x;
			optZ = divinePos.z;
			optHighrollProb *= divineContext.relativeDensity();
		}
		double optR = Math.sqrt(optX * optX + optZ * optZ);
		optX *= optDist / optR;
		optZ *= optDist / optR;
		Logger.log("Time to calculate blind features: " + (System.currentTimeMillis() - t0) / 1000f + " seconds.");
		return new BlindResult(b.x, b.z, probability, distanceThreshold, avgDist * 16, avgDistDerivative, ninetiethPercentileDerivative, Coords.getPhi(optX - b.x, optZ - b.z), Coords.dist(optX, optZ, b.x, b.z), optHighrollProb);
	}

	@Override
	public DivineResult divine(IDivineContext divineContext) {
		Fossil f = divineContext.getFossil();
		return f != null ? new DivineResult(f) : null;
	}

	private double getHighrollProbability(double x, double z, int distanceThreshold, IDivineContext divineContext) {
		double probability = 0;
		Prior prior;
		prior = new Prior((int) x * 8 / 16, (int) z * 8 / 16, distanceThreshold / 16 + 1, divineContext);
		for (Chunk c : prior.getChunks()) {
			double dx = x * 8 - c.x * 16 + 8;
			double dz = z * 8 - c.z * 16 + 8;
			if (dx * dx + dz * dz < distanceThreshold * distanceThreshold)
				probability += c.weight;
		}
		return probability;
	}

	private double getAverageDistance(double netherX, double netherZ, double rdPhi, double dR) {
		Pair<Ring, Ring> rings = Ring.getClosestRings(netherX / 2.0, netherZ / 2.0);
		Ring ring = rings.fst;
		Ring ring2 = rings.snd;
		double sectionAngle = 2 * Math.PI / ring.numStrongholds;
		double sectionAngle2 = 2 * Math.PI / ring2.numStrongholds;
		double ringThickness = ring.outerRadius - ring.innerRadius;
		double dPhi = rdPhi / ring.innerRadius;
		int nPhi = (int) (sectionAngle / dPhi);
		dPhi = sectionAngle / nPhi;
		int nR = (int) (ringThickness / dR);
		dR = ringThickness / nR;
		double phi0 = phi(netherX, netherZ);
		double z = netherZ / 2.0; // Chunk
		double x = netherX / 2.0; // Chunk
		double integral = 0;
		for (int i = 0; i < nPhi; i++) {
			double phi = phi0 - sectionAngle / 2.0 + i * dPhi;
			for (int j = 0; j < nR; j++) {
				double r = ring.innerRadius + (j + 0.5) * dR;
				double d = distance(x, z, phi, r);
				// Approximate the non-closest strongholds using uniform distributions,
				// works well if they are far, which they should be if they are not the closest
				// stronghold
				ArrayList<Uniform> otherStrongholds = new ArrayList<Uniform>();
				otherStrongholds.add(getStrongholdDistr(x, z, phi + sectionAngle, ring));
				otherStrongholds.add(getStrongholdDistr(x, z, phi - sectionAngle, ring));
				int i2 = i % 5 - 2;
				double phi2 = phi0 + i2 / 5.0 * sectionAngle2;
				otherStrongholds.add(getStrongholdDistr(x, z, phi2, ring2));
				otherStrongholds.add(getStrongholdDistr(x, z, phi2 + sectionAngle2, ring2));
				otherStrongholds.add(getStrongholdDistr(x, z, phi2 - sectionAngle2, ring2));
				Pair<Double, Double> pair = approxAverageDist(otherStrongholds, d);
				integral += (d * (1.0 - pair.fst) + pair.snd * pair.fst) / (nPhi * nR);
			}
		}
		return integral;
	}

	private double phi(double x, double z) {
		return -Math.atan2(x, z);
	}

	private class Uniform {
		public final double a;
		public final double b;

		public Uniform(double a, double b) {
			this.a = a;
			this.b = b;
		}
	}

	private Uniform getStrongholdDistr(double x, double z, double phi, Ring ring) {
		double min = distance(x, z, phi, ring.innerRadius);
		double max = distance(x, z, phi, ring.outerRadius);
		return min < max ? new Uniform(min, max) : new Uniform(max, min);
	}

	private Pair<Double, Double> approxAverageDist(List<Uniform> distributions, double maxDist) {
		ArrayList<Double> discontinuitites = new ArrayList<Double>();
		for (Uniform u : distributions) {
			if (u.a < maxDist) {
				discontinuitites.add(u.a);
				if (u.b < maxDist)
					discontinuitites.add(u.b);
			}
		}
		discontinuitites.add(maxDist);
		discontinuitites.sort((x1, x2) -> Double.compare(x1, x2));
		double cumulativeProb = 0;
		double expectedDistance = 0;
		for (int j = 1; j < discontinuitites.size(); j++) {
			int i = j - 1;
			double lower = discontinuitites.get(i);
			double upper = discontinuitites.get(j);
			double center = (lower + upper) / 2.0;
			double complementary_prob = 1.0;
			double n = 0;
			for (Uniform u : distributions) {
				if (center > u.a && center < u.b) {
					// Distribution is in interval
					complementary_prob *= (u.b - upper) / (u.b - lower);
					n += (upper - lower) / (u.b - lower);
				}
			}
			double prob = (1.0 - cumulativeProb) * (1.0 - complementary_prob);
			cumulativeProb += prob;
			double ev = (upper + n * lower) / (n + 1); // Approximation
			expectedDistance += prob * ev;
		}
		return new Pair<Double, Double>(cumulativeProb, cumulativeProb > 0 ? expectedDistance / cumulativeProb : 0);
	}

	private double distance(double x, double z, double phi, double r) {
		double dx = x + r * Math.sin(phi);
		double dz = z - r * Math.cos(phi);
		return Math.sqrt(dx * dx + dz * dz);
	}

}
