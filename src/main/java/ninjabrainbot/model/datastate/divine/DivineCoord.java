package ninjabrainbot.model.datastate.divine;

public class DivineCoord {

	public final int x, z;

	public DivineCoord(int x, int z) {
		this.x = x;
		this.z = z;
	}

	@Override
	public String toString() {
		return "(" + x + ", " + z + ")";
	}

}