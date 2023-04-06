package ninjabrainbot.data.calculator.statistics;

public class DiscretizedDensity {

	private double[] discretizedDensity; // values for each "sector"

	private final double minX, length;

	public DiscretizedDensity(double minX, double maxX) {
		this.minX = minX;
		length = maxX - minX;
		discretizedDensity = new double[] { 1 / length };
	}

	/**
	 * Sets the density to 0 everywhere, and redefines the number of discretization
	 * points.
	 */
	public void reset(int discretizationPoints) {
		discretizedDensity = new double[discretizationPoints];
	}

	/**
	 * Adds density at the given x-coord (has to be normalized after)
	 */
	public void addDensity(double x, double density) {
		int index = findIndexFromX(x);
		discretizedDensity[index] += density;
	}

	/**
	 * Adds density uniformly on the given interval (has to be normalized after).
	 * Total density added is proportional to the length of the interval.
	 */
	public void addDensity(double minX, double maxX, double density) {
		int index0 = findIndexFromX(minX);
		int index1 = findIndexFromX(maxX) + 1;
		if (index1 > discretizedDensity.length)
			index1 = discretizedDensity.length;
		for (int i = index0; i < index1; i++) {
			double min = Math.max(minX, getXFromIndex(i));
			double max = Math.min(maxX, getXFromIndex(i + 1));
			discretizedDensity[i] += density * (max - min);
		}
	}

	/**
	 * Makes the integral equal to 1.
	 */
	public void normalize() {
		double integral = 0;
		for (int i = 0; i < discretizedDensity.length; i++) {
			integral += discretizedDensity[i] * length / discretizedDensity.length;
		}
		for (int i = 0; i < discretizedDensity.length; i++) {
			discretizedDensity[i] /= integral;
		}
	}

	public double getDensity(double x) {
		return discretizedDensity[findIndexFromX(x)];
	}

	private int findIndexFromX(double x) {
		return (int) (discretizedDensity.length * (x - minX) / length);
	}

	private double getXFromIndex(int i) {
		return minX + length * ((double) i) / discretizedDensity.length;
	}

}
