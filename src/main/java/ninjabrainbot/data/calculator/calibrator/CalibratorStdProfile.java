package ninjabrainbot.data.calculator.calibrator;

import ninjabrainbot.data.calculator.endereye.IStdProfile;
import ninjabrainbot.data.calculator.endereye.IThrow;
import ninjabrainbot.event.Unmodifiable;

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
