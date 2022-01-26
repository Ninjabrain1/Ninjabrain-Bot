package ninjabrainbot.calculator;

import java.util.ArrayList;
import java.util.List;

import ninjabrainbot.Main;
import ninjabrainbot.util.Pair;

public class Calculator {

	double sigma;
	double sigmaAlt;
	// Used only for pixel correction
	int yRes = 1080;
	int fov = 30;

	public Calculator() {
		this(Main.preferences.sigma.get(), Main.preferences.sigmaAlt.get());
	}

	public Calculator(double sigma) {
		this(sigma, sigma);
	}
	
	public Calculator(double sigma, double sigmaAlt) {
		this.sigma = sigma;
		this.sigmaAlt = sigmaAlt;
	}

	public void setSigma(double sigma) {
		this.sigma = sigma;
	}
	
	public void setSigmaAlt(double sigmaAlt) {
		this.sigmaAlt = sigmaAlt;
	}

	public CalculatorResult triangulate(ArrayList<Throw> eyeThrows, DivineContext divineContext) {
		if (eyeThrows.size() == 0)
			return new CalculatorResult();
		long t0 = System.currentTimeMillis();
		// Calculate posteriors
		Posterior posterior = new Posterior(sigma, sigmaAlt, eyeThrows, divineContext);
		System.out.println("Time to triangulate: " + (System.currentTimeMillis() - t0)/1000f + " seconds.");
		return new CalculatorResult(posterior, eyeThrows);
	}
	
	public Posterior getPosterior(ArrayList<Throw> eyeThrows, DivineContext divineContext) {
		if (eyeThrows.size() == 0)
			return null;
		Posterior posterior = new Posterior(sigma, sigmaAlt, eyeThrows, divineContext);
		return posterior;
	}
	
	public BlindResult blind(BlindPosition b, DivineContext divineContext, boolean approximated) {
		long t0 = System.currentTimeMillis();
		int distanceThreshold = 400;
		double h = 5;
		double phi_p = phi(b.x, b.z);
		double probability = getHighrollProbability(b.x, b.z, distanceThreshold, approximated, divineContext);
		int deltaX = 2 * ((int) Math.round(-h * Math.sin(phi_p))/2);
		int deltaZ = 2 * ((int) Math.round(h * Math.cos(phi_p))/2);
		double probability2 = getHighrollProbability(b.x + deltaX, b.z + deltaZ, distanceThreshold, approximated, divineContext);
		double probabilityDerivative = (probability2 - probability) / Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
		double ninetiethPercentileDerivative = probabilityDerivative * Math.sqrt(0.1 / (2 * probability * probability * probability)) * distanceThreshold;
		double avgDist = getAverageDistance(b.x, b.z, 10, 20);
		double avgDist2 = getAverageDistance(b.x - h * Math.sin(phi_p), b.z + h * Math.cos(phi_p), 10, 20);
		double avgDistDerivative = (avgDist2 - avgDist) / h;
		System.out.println("Time to calculate blind features: " + (System.currentTimeMillis() - t0)/1000f + " seconds.");
		return new BlindResult(b.x, b.z, probability, distanceThreshold, avgDist * 16, avgDistDerivative, ninetiethPercentileDerivative);
	}
	
	private double getHighrollProbability(double x, double z, int distanceThreshold, boolean approximated, DivineContext divineContext) {
		double probability = 0;
		Prior prior;
		if (!approximated) {
			prior = new Prior((int) x * 8 / 16, (int) z * 8 / 16, distanceThreshold / 16 + 1, divineContext);
		} else {
			prior = new ApproximatedPrior((int) x * 8 / 16, (int) z * 8 / 16, distanceThreshold / 16 + 1, divineContext);
		}
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
		double sectionAngle =  2 * Math.PI / ring.numStrongholds;
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
				// works well if they are far, which they should be if they are not the closest stronghold
				ArrayList<Uniform> otherStrongholds = new ArrayList<Uniform>();
				otherStrongholds.add(getStrongholdDistr(x, z, phi + sectionAngle, ring));
				otherStrongholds.add(getStrongholdDistr(x, z, phi - sectionAngle, ring));
				int i2 = i % 5 - 2;
				double phi2 = phi0 + i2/5.0 * sectionAngle2;
				otherStrongholds.add(getStrongholdDistr(x, z, phi2, ring2));
				otherStrongholds.add(getStrongholdDistr(x, z, phi2 + sectionAngle2, ring2));
				otherStrongholds.add(getStrongholdDistr(x, z, phi2 - sectionAngle2, ring2));
				Pair<Double, Double> pair = approxAverageDist(otherStrongholds, d);
				integral += (d*(1.0 - pair.fst) + pair.snd * pair.fst) / (nPhi * nR);
			}
		}
		return integral;
	}
	
	private double phi(double x, double z) {
		return -Math.atan2(x, z);
	}
	
	private class Uniform {
		public double a, b;
		public Uniform(double a, double b) {
			this.a = a;
			this.b = b;
		}
	}
	
	private Uniform getStrongholdDistr(double x, double z, double phi, Ring ring) {
		double min = distance(x, z, phi, ring.innerRadius);
		double max = distance(x, z, phi, ring.outerRadius);
		return min < max ? new Uniform(min, max) :  new Uniform(max, min);
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
	
	private Pair<Double, Double> somecomplicatedformula(double x, double z, double phi_p, double phi, double d, Ring ring) {
		double r_p = Math.sqrt(x * x + z * z);
		double gamma = phi_p - phi;
		double sin_beta = r_p / d * Math.sin(gamma);
		if (sin_beta < 1.0 && sin_beta > -1.0) {
			double beta = Math.asin(sin_beta);
			double alpha0 = beta - gamma;
			double alpha1 = Math.PI - gamma - beta;
			double R0 = d * Math.sin(alpha0) / Math.sin(gamma);
			double R1 = d * Math.sin(alpha1) / Math.sin(gamma);
			if (R1 > ring.outerRadius)
				R1 = ring.outerRadius;
			if (R0 < ring.innerRadius)
				R0 = ring.innerRadius;
			if (R0 > ring.outerRadius)
				R0 = ring.outerRadius;
			if (R1 < ring.innerRadius)
				R1 = ring.innerRadius;
			double thickness = R1 - R0;
			if (thickness > 0.01) {
				int n = 10;
				double dr = thickness / n;
				double integral = 0;
				for (int i = 0; i < n; i++) {
					double r = ring.innerRadius + (i + 0.5) * dr;
					double distance = distance(x, z, phi, r);
					integral += distance / n;
				}
				return new Pair<Double, Double>(thickness / (ring.outerRadius - ring.innerRadius), integral);
			}
		}
		return new Pair<Double, Double>(0.0, 0.0);
	}
	
	private double distance(double x, double z, double phi, double r) {
		double dx = x + r * Math.sin(phi);
		double dz = z - r * Math.cos(phi);
		return Math.sqrt(dx * dx + dz * dz);
	}

	/**
	 * Gets the angle of one pixel on the screen (how much the angle changes by
	 * moving the crosshair one pixel to the right).
	 */
	private double getPixelAngle() {
		return Math.toDegrees(Math.atan(Math.tan(Math.toRadians(fov) / 2.0) / (yRes / 2.0)));
	}

	public DivineResult divine(Fossil f) {
		return new DivineResult(f);
	}

}
