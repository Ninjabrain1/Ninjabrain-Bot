package ninjabrainbot.gui.settings.themeeditor;

import ninjabrainbot.data.endereye.IStdProfile;
import ninjabrainbot.data.endereye.IThrow;
import ninjabrainbot.event.ISubscribable;
import ninjabrainbot.event.ObservableProperty;

public class PreviewThrow implements IThrow {

	private ISubscribable<IThrow> whenModified = new ObservableProperty<IThrow>();
	
	double x, z, alpha, correction;
	
	public PreviewThrow(double x, double z) {
		this(x, z, 0, 0);
	}
	
	public PreviewThrow(double x, double z, double alpha, double correction) {
		this.x = x;
		this.z = z;
		this.alpha = alpha;
		this.correction = correction;
	}
	
	@Override
	public double x() {
		return x;
	}

	@Override
	public double z() {
		return z;
	}

	@Override
	public double alpha() {
		return alpha + correction;
	}

	@Override
	public ISubscribable<IThrow> whenModified() {
		return whenModified;
	}

	@Override
	public double getStd() {
		return 0;
	}

	@Override
	public double alpha_0() {
		return alpha;
	}

	@Override
	public double correction() {
		return correction;
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
	public void addCorrection(double angle) {
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
