package ninjabrainbot.calculator;

import java.util.HashMap;

public class ApproximatedDensity {
	
	private static final int deltaR = 1; // discretisation step size (in chunks)
	private static double[] density; // approximated density
	
	public static void init() {
		if (density != null)
			return;
		density = new double[StrongholdConstants.maxChunk / deltaR + 5];
		RingIterator ringIterator = new RingIterator();
		for (Ring ring : ringIterator) {
			int c0 = (int) ring.innerRadius;
			int c1 = (int) ring.outerRadius;
			for (int i = c0; i <= c1; i += deltaR) {
				double rho = ring.numStrongholds / (2.0 * Math.PI * (ring.outerRadius - ring.innerRadius) * i);
				if (i == c0 || i == c1)
					rho *= 0.5;
				density[i / deltaR] = rho;
			}
		}
		double[] filter = new double[(int) (Math.ceil(StrongholdConstants.snappingRadius / deltaR * Math.sqrt(2))) + 1];
		double sum = 0;
		HashMap<Integer, Integer> offsetWeights = Prior.getOffsetWeights();
		for (int k = -StrongholdConstants.snappingRadius; k <= StrongholdConstants.snappingRadius; k++) {
			int xOffsetWeight = offsetWeights.get(k);
			for (int l = -StrongholdConstants.snappingRadius; l <= StrongholdConstants.snappingRadius; l++) {
				int zOffsetWeight = offsetWeights.get(k);
				int w = xOffsetWeight * zOffsetWeight;
				int n = 200;
				for (int i = 0; i < n; i++) {
					double phi = 2 * Math.PI * i / n;
					int dr = Math.abs(Math.round((float) (Math.sqrt(k * k + l * l) * Math.sin(phi))));
					filter[dr] += w;
					sum += dr == 0 ? w : 2 * w; // All offsets except dr=0 are double counted
				}
			}
		}
		for (int i = 0; i < filter.length; i++) {
			filter[i] = filter[i] / sum;
			//System.out.println(filter[i]);
		}
		// convolve
		double[] densityPreSnapping = density;
		density = new double[StrongholdConstants.maxChunk / deltaR + 5];
		for (int i = 0; i < density.length; i++) {
			for (int j = -filter.length + 1; j < filter.length; j++) {
				if (i + j >= 0 && i + j < densityPreSnapping.length) {
					int k = j < 0 ? -j : j;
					density[i] += densityPreSnapping[i + j] * filter[k];
				}
			}
		}
	}
	
	public static double density(double cx, double cz) {
		init();
		double d2 = cx * cx + cz * cz;
		double k = Math.sqrt(d2) / deltaR;
		double t = k - (int) k;
		int i0 = (int) k;
		int i1 = (int) k + 1;
		return i1 < density.length ? (1.0 - t) * density[i0]  + t * density[i1] : 0;
	}
	
}
