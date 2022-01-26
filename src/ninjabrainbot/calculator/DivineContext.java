package ninjabrainbot.calculator;

public class DivineContext {
	
	double phiMin, phiMax;
	
	public DivineContext(Fossil fossil) {
		int k = -4 + fossil.x;
		if (k >= 8) {
			k -= 16;
		}
		phiMin = 2.0 * Math.PI * (k / 16.0);
		phiMax = 2.0 * Math.PI * ((k+1) / 16.0);
	}
	
	public double relativeDensity() {
		return 16.0 / 3.0;
	}
	
	/**
	 * Returns how the minimum angle which phi has to change to be within the divine sector.
	 * If the angle is inside a sector the returned value will be negative.
	 * phi has to be in the range [-pi, pi]
	 */
	public double angleOffsetFromSector(double phi) {
		int n = Ring.get(0).numStrongholds;
		double minOffset = Math.PI;
		for (int i = 0; i < n; i++) {
			double phi_i = phi + i * 2.0 * Math.PI / n;
			if (phi_i > Math.PI) {
				phi_i -= 2.0 * Math.PI;
			}
			double offset = angleOffsetFromFirstSector(phi_i);
			if (offset < minOffset)
				minOffset = offset;
		}
		return minOffset;
	}
	
	private double angleOffsetFromFirstSector(double phi) {
		double phiCenter = 0.5 * (phiMin + phiMax);
		double change = angleDiff(phi, phiCenter);
		return change - (phiMax - phiCenter);
	}
	
	private double angleDiff(double a, double b) {
		double change = a - b;
		if (change < -Math.PI)
			change +=  2 * Math.PI;
		if (change > Math.PI)
			change -=  2 * Math.PI;
		return Math.abs(change);
	}
	
	
}
