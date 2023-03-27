package ninjabrainbot.data.endereye;

import ninjabrainbot.event.Modifiable;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;

public class StandardStdProfile extends Modifiable<IStdProfile> implements IStdProfile {

	public static final int NORMAL = 0;
	public static final int ALTERNATIVE = 1;
	public static final int MANUAL = 2;
	public static final int BOAT = 3;

	private final double[] stds;

	public StandardStdProfile(NinjabrainBotPreferences preferences) {
		stds = new double[] { preferences.sigma.get(), preferences.sigmaAlt.get(), preferences.sigmaManual.get(), preferences.sigmaBoat.get() };
	}

	@Override
	public double getStd(int profileNumber) {
		return stds[profileNumber];
	}

	@Override
	public int getNumberOfProfiles() {
		return stds.length;
	}

	public void setStd(int id, double std) {
		if (stds[id] == std)
			return;
		stds[id] = std;
		notifySubscribers(this);
	}

	@Override
	public int getInitialProfileNumber(IThrow t) {
		return t.isMcVersion1_12() ? MANUAL : t.isBoatThrow() ? BOAT : NORMAL;
	}

}