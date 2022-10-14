package ninjabrainbot.calculator;

import java.util.Locale;

import ninjabrainbot.Main;
import ninjabrainbot.io.NinjabrainBotPreferences;
import ninjabrainbot.util.I18n;
import ninjabrainbot.util.IDisposable;
import ninjabrainbot.util.IObservable;
import ninjabrainbot.util.ISet;
import ninjabrainbot.util.Modifiable;
import ninjabrainbot.util.Subscription;

public class ChunkPrediction extends Modifiable<ChunkPrediction> implements IDisposable {

	public final Chunk chunk;
	public final int fourfour_x, fourfour_z;
	public final boolean success;
	private int distance;
	private double travelAngle;
	private double travelAngleDiff;

	private Subscription playerPosSubscription;
	private Subscription strongholdDisplayTypeChangedSubscription;

	/**
	 * Creates a failed triangulation result.
	 */
	public ChunkPrediction() {
		this(new Chunk(0, 0), null);
	}

	/**
	 * Creates a triangulation result.
	 */
	public ChunkPrediction(Chunk chunk, IObservable<IThrow> playerPos) {
		this.chunk = chunk;
		this.fourfour_x = 16 * chunk.x + 4;
		this.fourfour_z = 16 * chunk.z + 4;
		this.success = Double.isFinite(chunk.weight) && chunk.weight > 0.0005;
		if (playerPos != null) {
			updateWithPlayerPos(playerPos.get(), false);
			playerPosSubscription = playerPos.subscribe(pos -> updateWithPlayerPos(pos, true));
			strongholdDisplayTypeChangedSubscription = Main.preferences.strongholdDisplayType.whenModified().subscribe(__ -> whenModified.notifySubscribers(this));
		}
	}

	private void updateWithPlayerPos(IThrow playerPos, boolean notify) {
		if (playerPos == null)
			return;
		distance = chunk.getDistance(playerPos);
		double playerX = playerPos.x();
		double playerZ = playerPos.z();
		if (playerPos.isNether()) {
			playerX *= 8;
			playerZ *= 8;
		}
		double xDiff = fourfour_x + 4 - playerX;
		double zDiff = fourfour_z + 4 - playerZ;

		double newAngle = -Math.atan2(xDiff, zDiff) * 180 / Math.PI;
		double simpleDiff = newAngle - playerPos.alpha();
		double adjustedDiff = ((newAngle + 360) % 360) - ((playerPos.alpha() + 360) % 360);
		double finalDiff = Math.abs(adjustedDiff) < Math.abs(simpleDiff) ? adjustedDiff : simpleDiff;

		this.travelAngle = newAngle;
		this.travelAngleDiff = finalDiff;
		if (notify)
			whenModified.notifySubscribers(this);
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
			return I18n.get("chunk_blocks", chunk.x, chunk.z, distance);
		}
		return I18n.get("chunk_blocks", chunk.x, chunk.z, distance);
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
			return String.format(Locale.US, "(%d, %d)", chunk.x, chunk.z);
		}
		return String.format(Locale.US, "(%d, %d)", chunk.x, chunk.z);
	}

	public String formatCertainty() {
		return String.format(Locale.US, "%.1f%%", chunk.weight * 100);
	}

	public String formatDistance() {
		return String.format(Locale.US, "%d", distance);
	}

	public String formatNether() {
		return String.format(Locale.US, "(%d, %d)", chunk.x * 2, chunk.z * 2);
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

	public double[] getAngleErrors(ISet<IThrow> eyeThrows) {
		return chunk.getAngleErrors(eyeThrows);
	}

	@Override
	public void dispose() {
		if (playerPosSubscription != null)
			playerPosSubscription.cancel();
		if (strongholdDisplayTypeChangedSubscription != null)
			strongholdDisplayTypeChangedSubscription.cancel();
	}

}
