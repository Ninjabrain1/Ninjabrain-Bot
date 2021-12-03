package ninjabrainbot.calculator;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
		this.innerRadiusPostSnapping = this.innerRadius - (StrongholdConstants.snappingRadius + 0.5) * Math.sqrt(2);
		this.outerRadiusPostSnapping = this.outerRadius + (StrongholdConstants.snappingRadius + 0.5) * Math.sqrt(2);
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
	
	public boolean inRing(double chunkR) {
		return chunkR >= this.innerRadiusPostSnapping && chunkR <= this.outerRadiusPostSnapping;
	}
	
}