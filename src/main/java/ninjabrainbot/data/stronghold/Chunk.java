package ninjabrainbot.data.stronghold;

import java.util.Objects;

import ninjabrainbot.data.common.IPosition;
import ninjabrainbot.data.endereye.IThrow;
import ninjabrainbot.io.preferences.MultipleChoicePreferenceDataTypes.McVersion;
import ninjabrainbot.util.ISet;

public class Chunk {

	public final int x;
	public final int z;
	public double weight;

	public Chunk(int x, int z) {
		this.x = x;
		this.z = z;
		weight = 0.0;
	}

	public Chunk(int x, int z, double w) {
		this.x = x;
		this.z = z;
		weight = w;
	}

	public int chunkDistanceSquared(Chunk other) {
		int dx = x - other.x;
		int dz = z - other.z;
		return dx * dx + dz * dz;
	}

	public int fourfourX() {
		return 16 * x + 4;
	}

	public int fourfourZ() {
		return 16 * z + 4;
	}

	public int eighteightX() {
		return 16 * x + 8;
	}

	public int eighteightZ() {
		return 16 * z + 8;
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, z);
	}

	@Override
	public boolean equals(Object obj) {
		if (getClass() != obj.getClass())
			return false;
		Chunk other = (Chunk) obj;
		return x == other.x && z == other.z;
	}

	@Override
	public String toString() {
		return "(" + x + ", " + z + ", " + weight + ")";
	}

	@Override
	public Chunk clone() {
		Chunk c = new Chunk(x, z);
		c.weight = this.weight;
		return c;
	}

	/**
	 * Returns the distance (number of blocks) to the predicted location from the
	 * given position.
	 */
	public int getOverworldDistance(McVersion version, IPosition t) {
		double playerX = t.xInOverworld();
		double playerZ = t.zInOverworld();
		double deltax = 16 * x + StrongholdConstants.getStrongholdChunkCoord(version) - playerX;
		double deltaz = 16 * z + StrongholdConstants.getStrongholdChunkCoord(version) - playerZ;
		return (int) Math.sqrt(deltax * deltax + deltaz * deltaz);
	}

	public double[] getAngleErrors(McVersion version, ISet<IThrow> eyeThrows) {
		double[] errors = new double[eyeThrows.size()];
		for (int i = 0; i < errors.length; i++) {
			errors[i] = getAngleError(version, eyeThrows.get(i));
		}
		return errors;
	}

	public double getAngleError(McVersion version, IThrow t) {
		double deltax = x * 16 + StrongholdConstants.getStrongholdChunkCoord(version) - t.xInPlayerDimension();
		double deltaz = z * 16 + StrongholdConstants.getStrongholdChunkCoord(version) - t.zInPlayerDimension();
		double gamma = -180 / Math.PI * Math.atan2(deltax, deltaz);
		double delta = (t.alpha() - gamma) % 360.0;
		if (delta < -180)
			delta += 360;
		if (delta > 180)
			delta -= 360;
		return delta;
	}

}
