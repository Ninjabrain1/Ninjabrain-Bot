package ninjabrainbot.calculator;

import ninjabrainbot.util.Coords;

public class DivineContext {
	
	double phiMin, phiMax;
	public final Fossil fossil;
	
	public DivineContext(Fossil fossil) {
		this.fossil = fossil;
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
	
	/**
	 * Returns the closest of the three divine coords that are a distance r from (0,0)
	 */
	public BlindPosition getClosestCoords(double x, double z, double r) {
		int n = Ring.get(0).numStrongholds;
		double minDist2 = Double.MAX_VALUE;
		double phi = (phiMin + phiMax) * 0.5;
		double optX = 0;
		double optZ = 0;
		for (int i = 0; i < n; i++) {
			double phi_i = phi + i * 2.0 * Math.PI / n;
			double x2 = Coords.getX(r, phi_i);
			double z2 = Coords.getZ(r, phi_i);
			double d2 = Coords.dist2(x, z, x2, z2);
			if (d2 < minDist2) {
				minDist2 = d2;
				optX = x2;
				optZ = z2;
			}
		}
		return new BlindPosition(optX, optZ);
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
