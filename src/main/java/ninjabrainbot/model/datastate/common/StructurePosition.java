package ninjabrainbot.model.datastate.common;

import java.util.Locale;

import ninjabrainbot.event.IDisposable;
import ninjabrainbot.event.IObservable;
import ninjabrainbot.event.ISubscribable;
import ninjabrainbot.event.ObservableProperty;
import ninjabrainbot.event.Subscription;
import ninjabrainbot.util.I18n;

public class StructurePosition implements IOverworldPosition, IDisposable {

	private final int x, z;

	private int overworldDistance;
	private double travelAngle;
	private double travelAngleDiff;
	private boolean playerIsInNether;

	private Subscription playerPosSubscription;

	private final ObservableProperty<StructurePosition> whenRelativePlayerPositionChanged;

	public StructurePosition(int x, int z) {
		this(x, z, null);
	}

	public StructurePosition(int x, int z, IObservable<IPlayerPosition> playerPosition) {
		this.x = x;
		this.z = z;
		whenRelativePlayerPositionChanged = new ObservableProperty<StructurePosition>();
		if (playerPosition != null) {
			updateWithPlayerPos(playerPosition.get(), false);
			playerPosSubscription = playerPosition.subscribe(pos -> updateWithPlayerPos(pos, true));
		}
	}

	private void updateWithPlayerPos(IPlayerPosition playerPos, boolean notify) {
		if (playerPos == null)
			return;

		overworldDistance = (int) this.distanceInOverworld(playerPos);
		playerIsInNether = playerPos.isInNether();
		double xDiff = x - playerPos.xInOverworld();
		double zDiff = z - playerPos.zInOverworld();

		double newAngle = -Math.atan2(xDiff, zDiff) * 180 / Math.PI;
		double angleDiff = (newAngle - playerPos.horizontalAngle()) % 360.0;
		if (angleDiff < -180.0) {
			angleDiff += 360.0;
		} else if (angleDiff > 180.0) {
			angleDiff -= 360.0;
		}

		this.travelAngle = newAngle;
		this.travelAngleDiff = angleDiff;
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

	public double xInNether() {
		return xInOverworld() / 8.0;
	}

	public double zInNether() {
		return zInOverworld() / 8.0;
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
