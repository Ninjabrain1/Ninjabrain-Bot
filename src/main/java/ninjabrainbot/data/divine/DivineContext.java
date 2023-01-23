package ninjabrainbot.data.divine;

import ninjabrainbot.data.blind.BlindPosition;
import ninjabrainbot.data.datalock.IModificationLock;
import ninjabrainbot.data.datalock.LockableField;
import ninjabrainbot.data.statistics.DiscretizedDensity;
import ninjabrainbot.data.stronghold.Ring;
import ninjabrainbot.event.ISubscribable;
import ninjabrainbot.event.ObservableField;
import ninjabrainbot.event.ObservableProperty;
import ninjabrainbot.util.Coords;

public class DivineContext implements IDivineContext {

	private IBuriedTreasureSimulator buriedTreasureSimulator;

	private DiscretizedDensity discretizedAngularDensity;

	private ObservableField<Fossil> fossil;
	private ObservableField<BuriedTreasure> buriedTreasure;
	private ObservableProperty<DivineContext> whenPhiDistributionChanged;

	public DivineContext(IModificationLock modificationLock) {
		fossil = new LockableField<Fossil>(modificationLock);
		buriedTreasure = new LockableField<BuriedTreasure>(modificationLock);
		discretizedAngularDensity = new DiscretizedDensity(0, 2.0 * Math.PI);
		buriedTreasureSimulator = new BuriedTreasureMonteCarloSimulator();
		whenPhiDistributionChanged = new ObservableProperty<>();
	}

	@Override
	public Fossil getFossil() {
		return fossil.get();
	}

	@Override
	public void reset() {
		setFossil(null);
	}

	@Override
	public double relativeDensity() {
		return fossil.get() == null ? 1.0 : (16.0 / 3.0);
	}

	@Override
	public ISubscribable<BuriedTreasure> whenBuriedTreasureChanged() {
		return buriedTreasure;
	}

	@Override
	public ISubscribable<Fossil> whenFossilChanged() {
		return fossil;
	}

	@Override
	public ISubscribable<DivineContext> whenPhiDistributionChanged() {
		return whenPhiDistributionChanged;
	}

	public void setFossil(Fossil f) {
		fossil.set(f);
		onChanged();
	}

	public void setBuriedTreasure(BuriedTreasure bt) {
		buriedTreasure.set(bt);
		onChanged();
	}

	public void clear() {
		fossil.set(null);
		buriedTreasure.set(null);
		onChanged();
	}

	@Override
	public boolean hasDivine() {
		return fossil.get() != null || buriedTreasure.get() != null;
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

	private void onChanged() {
		if (!hasDivine())
			discretizedAngularDensity.reset(1);
		else if (buriedTreasure.get() != null)
			recalculateDensityBuriedTreasureOnly();
		else
			recalculateDensityFossilOnly();
		whenPhiDistributionChanged.notifySubscribers(this);
	}

	private void recalculateDensityBuriedTreasureOnly() {
		discretizedAngularDensity.reset(256 * 3);
		buriedTreasureSimulator.setBuriedTreasure(buriedTreasure.get());

		for (int i = 0; i < 1000000; i++) {
			double phi = buriedTreasureSimulator.nextAngle();
			addDensityThreeStrongholds(phi, 1);
		}
		discretizedAngularDensity.normalize();
		System.out.println(buriedTreasure.get().x);
		System.out.println(buriedTreasure.get().z);
		System.out.println("recalculated");
	}

	private void recalculateDensityFossilOnly() {
		discretizedAngularDensity.reset(16 * 3);
		int angleIndex = -4 + fossil.get().x;
		if (angleIndex < 0) {
			angleIndex += 16;
		}
		double sectorPhiMin = 2.0 * Math.PI * ((angleIndex) / 16.0);
		double sectorPhiMax = 2.0 * Math.PI * ((angleIndex + 1) / 16.0);
		addDensityThreeStrongholds(sectorPhiMin, sectorPhiMax, 1.0);
		discretizedAngularDensity.normalize();
	}

	private void addDensityThreeStrongholds(double phi, double density) {
		for (int i = 0; i < 3; i++) {
			if (phi > 2 * Math.PI)
				phi -= 2 * Math.PI;
			discretizedAngularDensity.addDensity(phi, 1.0);
			phi += 2.0 / 3.0 * Math.PI;
		}
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
