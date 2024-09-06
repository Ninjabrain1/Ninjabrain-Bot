package ninjabrainbot.model.datastate.alladvancements;

public class AllAdvancementsPosition implements IAllAdvancementsPosition {

	private final double x, z;

	public AllAdvancementsPosition(double x, double z) {
		this.x = x;
		this.z = z;
	}

	@Override
	public double xInOverworld() {
		return x;
	}

	@Override
	public double zInOverworld() {
		return z;
	}
}
