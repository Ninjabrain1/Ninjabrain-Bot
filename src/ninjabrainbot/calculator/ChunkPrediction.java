package ninjabrainbot.calculator;

import java.util.Locale;

import ninjabrainbot.Main;
import ninjabrainbot.io.NinjabrainBotPreferences;
import ninjabrainbot.util.I18n;

public class ChunkPrediction extends Chunk {

	public final int fourfour_x, fourfour_z;
	public final boolean success;
	private int distance;
	private double travelAngle;
	private double travelAngleDiff;
	
	/**
	 * Creates a triangulation result.
	 */
	public ChunkPrediction(Chunk chunk, Throw playerPos) {
		super(chunk.x, chunk.z, chunk.weight);
		this.fourfour_x = 16 * chunk.x + 4;
		this.fourfour_z = 16 * chunk.z + 4;
		this.success = Double.isFinite(chunk.weight) && chunk.weight > 0.0005;
		if (playerPos != null) {
			updateWithPlayerPos(playerPos);
		}
	}

	public void updateWithPlayerPos(Throw playerPos) {
		distance = getDistance(playerPos);
		double playerX = playerPos.x;
		double playerZ = playerPos.z;
		if (playerPos.isNether()) {
			playerX *= 8;
			playerZ *= 8;
		}
		double xDiff = fourfour_x + 4 - playerX;
		double zDiff = fourfour_z + 4 - playerZ;

		double newAngle = -Math.atan2(xDiff, zDiff) * 180 / Math.PI;
		double simpleDiff = newAngle - playerPos.alpha;
		double adjustedDiff = ((newAngle + 360) % 360) - ((playerPos.alpha + 360) % 360);
		double finalDiff = Math.abs(adjustedDiff) < Math.abs(simpleDiff) ? adjustedDiff : simpleDiff;

		this.travelAngle = newAngle;
		this.travelAngleDiff = finalDiff;
	}

	public int getDistance() {
		return distance;
	}

	public double getTravelAngle() {
		return travelAngle;
	}

	public double getTravelAngleDiff() {
		return travelAngleDiff;
	}

	/**
	 * Creates a failed triangulation result.
	 */
	public ChunkPrediction() {
		this(new Chunk(0, 0), null);
	}
	
	public String format() {
		final String key = Main.preferences.strongholdDisplayType.get();
		switch (key) {
		case NinjabrainBotPreferences.FOURFOUR:
			return I18n.get("location_blocks", fourfour_x, fourfour_z, distance);
		case NinjabrainBotPreferences.EIGHTEIGHT:
			return I18n.get("location_blocks", fourfour_x + 4, fourfour_z + 4, distance);
			default:
				break;
		}
		if (key.equals(NinjabrainBotPreferences.CHUNK)) {
			return I18n.get("chunk_blocks", x, z, distance);
		}
		return I18n.get("chunk_blocks", x, z, distance);
	}

	public String formatLocation() {
		final String key = Main.preferences.strongholdDisplayType.get();
		switch (key) {
		case NinjabrainBotPreferences.FOURFOUR:
			return String.format(Locale.US, "(%d, %d)", fourfour_x, fourfour_z);
		case NinjabrainBotPreferences.EIGHTEIGHT:
			return String.format(Locale.US, "(%d, %d)", fourfour_x + 4, fourfour_z + 4);
			default:
				break;
		}
		if (key.equals(NinjabrainBotPreferences.CHUNK)) {
			return String.format(Locale.US, "(%d, %d)", x, z);
		}
		return String.format(Locale.US, "(%d, %d)", x, z);
	}

	public String formatCertainty() {
		return String.format(Locale.US, "%.1f%%", weight * 100);
	}

	public String formatDistance() {
		return String.format(Locale.US, "%d", distance);
	}

	public String formatNether() {
		return String.format(Locale.US, "(%d, %d)", x * 2, z * 2);
	}

	public String formatTravelAngle(boolean forBasic) {
		if (forBasic) {
			return String.format("%s: %.2f", I18n.get("current_angle"), travelAngle);
		}
		return String.format("%.2f", travelAngle);
	}

	public String formatTravelAngleDiff() {
		double absChange = Math.abs(travelAngleDiff);
		return String.format(" (%s %.1f)", travelAngleDiff > 0 ? "->" : "<-", absChange);
	}

	public float getTravelAngleDiffColor() {
		return (float) (1 - Math.abs(travelAngleDiff) / 180.0);
	}

}
