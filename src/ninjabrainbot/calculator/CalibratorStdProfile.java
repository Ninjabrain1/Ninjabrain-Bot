package ninjabrainbot.calculator;

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

}
