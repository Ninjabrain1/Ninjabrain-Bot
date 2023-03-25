package ninjabrainbot.util;

import ninjabrainbot.data.endereye.IStdProfile;
import ninjabrainbot.data.endereye.IThrow;
import ninjabrainbot.event.ISubscribable;
import ninjabrainbot.event.ObservableProperty;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;

public class TestThrow implements IThrow {

	ObservableProperty<IThrow> whenModified = new ObservableProperty<>();

	double x, z, alpha, std;

	public TestThrow(double x, double z, double alpha, double std) {
		this.x = x;
		this.z = z;
		this.alpha = alpha;
		this.std = std;
	}

	@Override
	public double alpha() {
		return alpha;
	}

	@Override
	public double xInOverworld() {
		return x;
	}

	@Override
	public double zInOverworld() {
		return z;
	}

	@Override
	public ISubscribable<IThrow> whenModified() {
		return whenModified;
	}

	@Override
	public double xInPlayerDimension() {
		return x;
	}

	@Override
	public double zInPlayerDimension() {
		return z;
	}

	@Override
	public double getStd() {
		return std;
	}

	@Override
	public double rawAlpha() {
		return alpha;
	}

	@Override
	public double alpha_0() {
		return alpha;
	}

	@Override
	public double correction() {
		return 0;
	}

	@Override
	public boolean lookingBelowHorizon() {
		return false;
	}

	@Override
	public boolean isNether() {
		return false;
	}

	@Override
	public void addCorrection(boolean positive, NinjabrainBotPreferences preferences) {
	}

	@Override
	public void setStdProfileNumber(int profileNumber) {
	}

	@Override
	public void setStdProfile(IStdProfile stdProfile) {
	}

	@Override
	public int getStdProfileNumber() {
		return 0;
	}

}
