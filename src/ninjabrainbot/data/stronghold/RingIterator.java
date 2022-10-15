package ninjabrainbot.data.stronghold;

import java.util.Iterator;

/**
 * Iterates over the stronghold rings.
 */
public class RingIterator implements Iterator<Ring>, Iterable<Ring> {
		
	int strongholdsInRing = 1;
	int currentStrongholds = 0;
	int ring = -1;

	@Override
	public boolean hasNext() {
		return ring < StrongholdConstants.numRings - 1;
	}

	@Override
	public Ring next() {
		ring += 1;
		strongholdsInRing += 2 * strongholdsInRing / (ring + 1);
		strongholdsInRing = Math.min(strongholdsInRing, StrongholdConstants.numStrongholds - currentStrongholds);
		currentStrongholds += strongholdsInRing;
		return new Ring(strongholdsInRing, ring);
	}

	@Override
	public Iterator<Ring> iterator() {
		return this;
	}
	
}


