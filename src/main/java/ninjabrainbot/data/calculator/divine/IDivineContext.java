package ninjabrainbot.data.calculator.divine;

import ninjabrainbot.data.calculator.blind.BlindPosition;
import ninjabrainbot.event.IObservable;

public interface IDivineContext {

	/**
	 * @return The x-coordinate of the divine fossil if one exists, null otherwise.
	 */
	public Fossil getFossil();

	public void resetFossil();

	public IObservable<Fossil> fossil();

	public boolean hasDivine();

	public double relativeDensity();

	public double getDensityAtAngleBeforeSnapping(double phi);

	/**
	 * Returns the closest of the three divine coords that are a distance r from
	 * (0,0)
	 */
	public BlindPosition getClosestCoords(double x, double z, double r);

}
