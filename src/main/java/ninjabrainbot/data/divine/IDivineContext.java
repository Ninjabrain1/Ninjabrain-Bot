package ninjabrainbot.data.divine;

import ninjabrainbot.data.blind.BlindPosition;
import ninjabrainbot.event.ISubscribable;

public interface IDivineContext {

	/**
	 * @return The x-coordinate of the divine fossil if one exists, null otherwise.
	 */
	public Fossil getFossil();

	public void reset();

	public ISubscribable<BuriedTreasure> whenBuriedTreasureChanged();

	public ISubscribable<Fossil> whenFossilChanged();
	
	public ISubscribable<DivineContext> whenPhiDistributionChanged();

	public boolean hasDivine();

	public double relativeDensity();

	public double getDensityAtAngleBeforeSnapping(double phi);

	/**
	 * Returns the closest of the three divine coords that are a distance r from
	 * (0,0)
	 */
	public BlindPosition getClosestCoords(double x, double z, double r);

}
