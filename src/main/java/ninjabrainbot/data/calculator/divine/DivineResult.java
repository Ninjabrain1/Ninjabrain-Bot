package ninjabrainbot.data.calculator.divine;

import ninjabrainbot.data.calculator.stronghold.Ring;
import ninjabrainbot.data.calculator.stronghold.StrongholdConstants;

public class DivineResult {

	public final Fossil fossil;
	public final DivineCoord[] safe;
	public final DivineCoord[] highroll;

	public DivineResult(Fossil fossil) {
		this.fossil = fossil;
		Ring ring = Ring.get(0);
		double phi0 = (fossil.x + 0.5) / 16.0 * Math.PI * 2;
		double safe_r = 2 * ring.centerRadius();
		double highroll_r = 2 * (ring.innerRadius + StrongholdConstants.snappingRadius);
		int n = ring.numStrongholds;
		safe = new DivineCoord[n];
		highroll = new DivineCoord[n];
		for (int i = 0; i < n; i++) {
			double phi = phi0 + 2 * Math.PI * (double) i / n;
			safe[i] = new DivineCoord(Math.round((float) (safe_r * Math.cos(phi))), Math.round((float) (safe_r * Math.sin(phi))));
			highroll[i] = new DivineCoord(Math.round((float) (highroll_r * Math.cos(phi))), Math.round((float) (highroll_r * Math.sin(phi))));
		}
	}

	public String formatSafeCoords() {
		return format(safe);
	}

	public String formatHighrollCoords() {
		return format(highroll);
	}

	private String format(DivineCoord[] coords) {
		String[] substrings = new String[coords.length];
		for (int i = 0; i < coords.length; i++) {
			substrings[i] = coords[i].toString();
		}
		return String.join(", ", substrings);
	}

}
