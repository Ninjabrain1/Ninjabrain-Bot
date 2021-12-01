package ninjabrainbot.calculator;

import java.util.ArrayList;

import ninjabrainbot.Main;

public class Triangulator {

	double sigma;
	// Used only for pixel correction
	int yRes = 1080;
	int fov = 30;

	public Triangulator() {
		this(Main.preferences.sigma.get());
	}

	public Triangulator(double sigma) {
		this.sigma = sigma;
	}

	public void setSigma(double sigma) {
		this.sigma = sigma;
	}

	public TriangulationResult triangulate(ArrayList<Throw> eyeThrows) {
		if (eyeThrows.size() == 0)
			return new TriangulationResult();
		long t0 = System.currentTimeMillis();
		// Calculate posteriors
		Posterior posterior = new Posterior(sigma, eyeThrows);
		// Find chunk with largest posterior probability
		Chunk predictedChunk = posterior.getMostProbableChunk();
		System.out.println("Time to triangulate: " + (System.currentTimeMillis() - t0)/1000f + " seconds.");
//		posterior.getChunks().stream().sorted((a, b) -> Double.compare(a.weight, b.weight)).forEach(p -> System.out.println(p));;
		return new TriangulationResult(predictedChunk.x, predictedChunk.z, predictedChunk.weight);
	}
	
	public Posterior getPosterior(ArrayList<Throw> eyeThrows) {
		if (eyeThrows.size() == 0)
			return null;
		Posterior posterior = new Posterior(sigma, eyeThrows);
		return posterior;
	}

	/**
	 * Gets the angle of one pixel on the screen (how much the angle changes by
	 * moving the crosshair one pixel to the right).
	 */
	private double getPixelAngle() {
		return Math.toDegrees(Math.atan(Math.tan(Math.toRadians(fov) / 2.0) / (yRes / 2.0)));
	}

}
