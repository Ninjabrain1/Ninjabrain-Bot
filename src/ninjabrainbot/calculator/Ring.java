package ninjabrainbot.calculator;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import ninjabrainbot.util.Pair;

public class Ring {
	
	public static final List<Ring> rings = StreamSupport.stream(new RingIterator().spliterator(), false).collect(Collectors.toList());
	
	public final int numStrongholds;
	public final int ring;
	// Ring radiuses before snapping (in chunks)
	public final double innerRadius;
	public final double outerRadius;
	// after snapping
	public final double innerRadiusPostSnapping;
	public final double outerRadiusPostSnapping;
	
	public Ring(int strongholdsInRing, int ring) {
		this.numStrongholds = strongholdsInRing;
		this.ring = ring;
		this.innerRadius = StrongholdConstants.distParam * ((4 + ring * 6) - 0.5D * 2.5D);
		this.outerRadius = StrongholdConstants.distParam * ((4 + ring * 6) + 0.5D * 2.5D);
		this.innerRadiusPostSnapping = this.innerRadius - (StrongholdConstants.snappingRadius + 1.0) * Math.sqrt(2);
		this.outerRadiusPostSnapping = this.outerRadius + (StrongholdConstants.snappingRadius + 1.0) * Math.sqrt(2);
	}
	
	public static Ring get(int i) {
		if (i >= 0 && i < StrongholdConstants.numRings) {
			return rings.get(i);
		}
		return null;
	}
	
	public static Ring get(double chunkR) {
		for (int k = 0; k < rings.size(); k++) {
			if (rings.get(k).inRing(chunkR)) {
				return rings.get(k);
			}
		}
		return null;
	}
	
	public static Pair<Ring, Ring> getClosestRings(double cx, double cz){
		double r = Math.sqrt(cx * cx + cz * cz);
		Ring closest = Ring.get(0);
		Ring nextClosest = Ring.get(1);
		double closestDist = Math.abs(closest.centerRadius() - r);
		double nextClosestDist = Math.abs(nextClosest.centerRadius() - r);
		if (nextClosestDist < closestDist) {
			Ring temp = closest;
			double tempDist = closestDist;
			closest = nextClosest;
			closestDist = nextClosestDist;
			nextClosest = temp;
			nextClosestDist = tempDist;
		}
		for (int i = 2; i < rings.size(); i++) {
			Ring ring = rings.get(i);
			double dist = Math.abs(ring.centerRadius() - r);
			if (dist < closestDist) {
				nextClosest = closest;
				nextClosestDist = closestDist;
				closest = ring;
				closestDist = dist;
			} else if (dist < nextClosestDist) {
				nextClosest = ring;
				nextClosestDist = dist;
			}
		}
		return new Pair<Ring, Ring>(closest, nextClosest);
	}
	
	public double centerRadius() {
		return (outerRadius + innerRadius) / 2.0;
	}
	
	public boolean inRing(double chunkR) {
		return chunkR >= this.innerRadiusPostSnapping && chunkR <= this.outerRadiusPostSnapping;
	}
	
}