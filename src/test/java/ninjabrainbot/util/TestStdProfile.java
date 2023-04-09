package ninjabrainbot.util;

import ninjabrainbot.data.calculator.endereye.IStdProfile;
import ninjabrainbot.data.calculator.endereye.IEnderEyeThrow;
import ninjabrainbot.event.ISubscribable;
import ninjabrainbot.event.ObservableProperty;

public class TestStdProfile implements IStdProfile {

	private final double std;

	public TestStdProfile(double std) {
		this.std = std;
	}

	@Override
	public double getStd(int profileNumber) {
		return std;
	}

	@Override
	public int getNumberOfProfiles() {
		return 1;
	}

	@Override
	public int getInitialProfileNumber(IEnderEyeThrow t) {
		return 0;
	}

	@Override
	public ISubscribable<IStdProfile> whenModified() {
		return new ObservableProperty<>();
	}
}
