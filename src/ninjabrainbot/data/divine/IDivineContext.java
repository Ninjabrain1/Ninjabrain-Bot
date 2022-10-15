package ninjabrainbot.data.divine;

import ninjabrainbot.data.blind.BlindPosition;
import ninjabrainbot.util.ISubscribable;

public interface IDivineContext {
	
	/**
	 * @return The x-coordinate of the divine fossil if one exists, null otherwise.
	 */
	public Fossil getFossil();
	
	public void resetFossil();
	
	public ISubscribable<Fossil> whenFossilChanged();
	
	public double relativeDensity();
	
	/**
	 * Returns how the minimum angle which phi has to change to be within the divine
	 * sector. If the angle is inside a sector the returned value will be negative.
	 * phi has to be in the range [-pi, pi]
	 */
	public double angleOffsetFromSector(double phi);

	/**
	 * Returns the closest of the three divine coords that are a distance r from
	 * (0,0)
	 */
	public BlindPosition getClosestCoords(double x, double z, double r);
	
}
