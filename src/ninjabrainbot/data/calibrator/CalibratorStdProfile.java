package ninjabrainbot.data.calibrator;

import ninjabrainbot.data.endereye.IStdProfile;
import ninjabrainbot.data.endereye.IThrow;
import ninjabrainbot.util.Unmodifiable;

public class CalibratorStdProfile extends Unmodifiable<IStdProfile> implements IStdProfile {

	@Override
	public double getStd(int profileNumber) {
		return 0.5;
	}

	@Override
	public int getNumberOfProfiles() {
		return 1;
	}

	@Override
	public int getInitialProfileNumber(IThrow t) {
		return 0;
	}

}
