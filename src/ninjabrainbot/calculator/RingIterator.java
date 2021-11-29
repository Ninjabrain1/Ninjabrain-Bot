package ninjabrainbot.calculator;

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

class Ring {
	
	public final int numStrongholds;
	public final int ring;
	// Ring radiuses before snapping (in chunks)
	public final double innerRadius;
	public final double outerRadius;
	
	public Ring(int strongholdsInRing, int ring) {
		this.numStrongholds = strongholdsInRing;
		this.ring = ring;
		this.innerRadius = StrongholdConstants.distParam * ((4 + ring * 6) - 0.5D * 2.5D);
		this.outerRadius = StrongholdConstants.distParam * ((4 + ring * 6) + 0.5D * 2.5D);
	}
	
}
