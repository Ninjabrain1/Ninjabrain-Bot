package ninjabrainbot.model.datastate.divine;

import ninjabrainbot.model.datastate.endereye.F3IData;

public class Fossil {

	public final int x;

	public Fossil(int x) {
		this.x = x;
	}

	/**
	 * Returns a Fossil object if the given F3+I-location is
	 * in the 0,0 chunk, null otherwise.
	 */
	public static Fossil tryCreateFromF3I(F3IData f3IData) {
		if (0 <= f3IData.x && f3IData.x < 16 && 0 <= f3IData.z && f3IData.z < 16)
			return new Fossil(f3IData.x);
		return null;
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof Fossil && ((Fossil) obj).x == x;
	}

}
