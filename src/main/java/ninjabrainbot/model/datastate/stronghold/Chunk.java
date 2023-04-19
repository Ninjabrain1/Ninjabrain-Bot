package ninjabrainbot.model.datastate.stronghold;

import java.util.Objects;

import ninjabrainbot.event.IReadOnlyList;
import ninjabrainbot.io.preferences.enums.McVersion;
import ninjabrainbot.model.datastate.common.IOverworldPosition;
import ninjabrainbot.model.datastate.endereye.IEnderEyeThrow;

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

	public int fourFourX() {
		return 16 * x + 4;
	}

	public int fourFourZ() {
		return 16 * z + 4;
	}

	public int eightRightX() {
		return 16 * x + 8;
	}

	public int eightEightZ() {
		return 16 * z + 8;
	}

	public int netherX() {
		return 2 * x;
	}

	public int netherZ() {
		return 2 * z;
	}

	public boolean isNeighboring(Chunk other) {
		return Math.abs(this.x - other.x) <= 1 && Math.abs(this.z - other.z) <= 1;
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
	public int getOverworldDistance(McVersion version, IOverworldPosition t) {
		double deltaX = 16 * x + StrongholdConstants.getStrongholdChunkCoord(version) - t.xInOverworld();
		double deltaZ = 16 * z + StrongholdConstants.getStrongholdChunkCoord(version) - t.zInOverworld();
		return (int) Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
	}

	public double[] getAngleErrors(McVersion version, IReadOnlyList<IEnderEyeThrow> eyeThrows) {
		double[] errors = new double[eyeThrows.size()];
		for (int i = 0; i < errors.length; i++) {
			errors[i] = getAngleError(version, eyeThrows.get(i));
		}
		return errors;
	}

	public double getAngleError(McVersion version, IEnderEyeThrow t) {
		double deltaX = x * 16 + StrongholdConstants.getStrongholdChunkCoord(version) - t.xInOverworld();
		double deltaZ = z * 16 + StrongholdConstants.getStrongholdChunkCoord(version) - t.zInOverworld();
		double gamma = -180 / Math.PI * Math.atan2(deltaX, deltaZ);
		double delta = (t.horizontalAngle() - gamma) % 360.0;
		if (delta < -180)
			delta += 360;
		if (delta > 180)
			delta -= 360;
		return delta;
	}

}
