package ninjabrainbot.model.datastate.divine;

import ninjabrainbot.model.datastate.blind.BlindPosition;
import ninjabrainbot.model.datastate.statistics.DiscretizedDensity;
import ninjabrainbot.model.datastate.stronghold.Ring;
import ninjabrainbot.model.domainmodel.DataComponent;
import ninjabrainbot.model.domainmodel.IDataComponent;
import ninjabrainbot.model.domainmodel.IDomainModel;
import ninjabrainbot.util.Coords;

public class DivineContext implements IDivineContext {

	private final DiscretizedDensity discretizedAngularDensity;

	public final DataComponent<Fossil> fossil;

	public DivineContext(IDomainModel domainModel) {
		fossil = new DataComponent<>(domainModel);
		discretizedAngularDensity = new DiscretizedDensity(0, 2.0 * Math.PI);
		fossil.subscribe(this::onFossilChanged);
	}

	@Override
	public Fossil getFossil() {
		return fossil.get();
	}

	@Override
	public double relativeDensity() {
		return fossil.get() == null ? 1.0 : (16.0 / 3.0);
	}

	@Override
	public IDataComponent<Fossil> fossil() {
		return fossil;
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
