package ninjabrainbot.data.common;

import java.util.Locale;

import ninjabrainbot.data.endereye.IThrow;
import ninjabrainbot.event.IDisposable;
import ninjabrainbot.event.IObservable;
import ninjabrainbot.event.ISubscribable;
import ninjabrainbot.event.ObservableProperty;
import ninjabrainbot.event.Subscription;
import ninjabrainbot.util.I18n;

public class StructurePosition implements IPosition, IDisposable {

	private final int x, z;

	private int overworldDistance;
	private double travelAngle;
	private double travelAngleDiff;
	private boolean playerIsInNether;

	private Subscription playerPosSubscription;

	private ObservableProperty<StructurePosition> whenRelativePlayerPositionChanged;

	public StructurePosition(int x, int z) {
		this(x, z, null);
	}

	public StructurePosition(int x, int z, IObservable<IThrow> playerPos) {
		this.x = x;
		this.z = z;
		whenRelativePlayerPositionChanged = new ObservableProperty<StructurePosition>();
		if (playerPos != null) {
			updateWithPlayerPos(playerPos.get(), false);
			playerPosSubscription = playerPos.subscribe(pos -> updateWithPlayerPos(pos, true));
		}
	}

	private void updateWithPlayerPos(IThrow playerPos, boolean notify) {
		if (playerPos == null)
			return;

		overworldDistance = (int) this.distanceInOverworld(playerPos);
		playerIsInNether = playerPos.isNether();
		double xDiff = x - playerPos.xInOverworld();
		double zDiff = z - playerPos.zInOverworld();

		double newAngle = -Math.atan2(xDiff, zDiff) * 180 / Math.PI;
		double simpleDiff = newAngle - playerPos.alpha();
		double adjustedDiff = ((newAngle + 360) % 360) - ((playerPos.alpha() + 360) % 360);
		double finalDiff = Math.abs(adjustedDiff) < Math.abs(simpleDiff) ? adjustedDiff : simpleDiff;

		this.travelAngle = newAngle;
		this.travelAngleDiff = finalDiff;
		whenRelativePlayerPositionChanged.notifySubscribers(this);
	}

	public String formatDistanceInPlayersDimension() {
		return String.format(Locale.US, "%d", playerIsInNether ? getNetherDistance() : overworldDistance);
	}

	public String formatNether() {
		return String.format(Locale.US, "(%d, %d)", (int) Math.floor(x / 8.0), (int) Math.floor(z / 8.0));
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

	@Override
	public double xInOverworld() {
		return x;
	}

	@Override
	public double zInOverworld() {
		return z;
	}

	public int getOverworldDistance() {
		return overworldDistance;
	}

	public int getNetherDistance() {
		return overworldDistance / 8;
	}

	public double getTravelAngle() {
		return travelAngle;
	}

	public double getTravelAngleDiff() {
		return travelAngleDiff;
	}

	public ISubscribable<StructurePosition> whenRelativePlayerPositionChanged() {
		return whenRelativePlayerPositionChanged;
	}

	@Override
	public void dispose() {
		if (playerPosSubscription != null)
			playerPosSubscription.dispose();
	}

}
