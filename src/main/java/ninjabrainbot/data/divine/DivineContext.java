package ninjabrainbot.data.divine;

import ninjabrainbot.data.blind.BlindPosition;
import ninjabrainbot.data.datalock.IModificationLock;
import ninjabrainbot.data.datalock.LockableField;
import ninjabrainbot.data.statistics.DiscretizedDensity;
import ninjabrainbot.data.stronghold.Ring;
import ninjabrainbot.event.IObservable;
import ninjabrainbot.event.ISubscribable;
import ninjabrainbot.event.ObservableField;
import ninjabrainbot.util.Coords;

public class DivineContext implements IDivineContext {

	private DiscretizedDensity discretizedAngularDensity;

	private ObservableField<Fossil> fossil;

	public DivineContext(IModificationLock modificationLock) {
		fossil = new LockableField<Fossil>(modificationLock);
		discretizedAngularDensity = new DiscretizedDensity(0, 2.0 * Math.PI);
	}

	@Override
	public Fossil getFossil() {
		return fossil.get();
	}

	@Override
	public void resetFossil() {
		setFossil(null);
	}

	@Override
	public double relativeDensity() {
		return fossil.get() == null ? 1.0 : (16.0 / 3.0);
	}

	@Override
	public IObservable<Fossil> fossil() {
		return fossil;
	}

	public void setFossil(Fossil f) {
		onFossilChanged(f);
		fossil.set(f);
	}

	public void clear() {
		setFossil(null);
	}

	@Override
	public boolean hasDivine() {
		return fossil.get() != null;
	}

	public double getDensityAtAngleBeforeSnapping(double phi) {
		while (phi < 0)
			phi += 2.0 * Math.PI;
		return discretizedAngularDensity.getDensity(phi);
	}

	/**
	 * Returns the closest of the three divine coords that are a distance r from
	 * (0,0)
	 */
	public BlindPosition getClosestCoords(double x, double z, double r) {
		if (fossil.get() == null) {
			double multiplier = r / Coords.dist(x, z, 0, 0);
			return new BlindPosition(x * multiplier, z * multiplier);
		}
		int n = Ring.get(0).numStrongholds;
		double minDist2 = Double.MAX_VALUE;
		int angleIndex = -4 + fossil.get().x;
		if (angleIndex < 0) {
			angleIndex += 16;
		}
		double phi = 2.0 * Math.PI * ((angleIndex + 0.5) / 16.0);
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

	private void onFossilChanged(Fossil fossil) {
		if (fossil == null) {
			discretizedAngularDensity.reset(1);
			return;
		}
		discretizedAngularDensity.reset(16 * 3);
		int angleIndex = -4 + fossil.x;
		if (angleIndex < 0) {
			angleIndex += 16;
		}
		double sectorPhiMin = 2.0 * Math.PI * ((angleIndex) / 16.0);
		double sectorPhiMax = 2.0 * Math.PI * ((angleIndex + 1) / 16.0);
		addDensityThreeStrongholds(sectorPhiMin, sectorPhiMax, 1.0);
		discretizedAngularDensity.normalize();
	}

	private void addDensityThreeStrongholds(double minPhi, double maxPhi, double density) {
		for (int i = 0; i < 3; i++) {
			if (minPhi > 2 * Math.PI)
				minPhi -= 2 * Math.PI;
			if (maxPhi > 2 * Math.PI)
				maxPhi -= 2 * Math.PI;
			if (maxPhi >= minPhi) {
				discretizedAngularDensity.addDensity(minPhi, maxPhi, 1.0);
			} else {
				discretizedAngularDensity.addDensity(minPhi, 2 * Math.PI, 1.0);
				discretizedAngularDensity.addDensity(0, maxPhi, 1.0);
			}
			minPhi += 2.0 / 3.0 * Math.PI;
			maxPhi += 2.0 / 3.0 * Math.PI;
		}
	}

}
