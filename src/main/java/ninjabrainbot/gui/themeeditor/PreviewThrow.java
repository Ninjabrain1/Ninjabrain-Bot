package ninjabrainbot.gui.themeeditor;

import ninjabrainbot.data.calculator.endereye.IThrow;
import ninjabrainbot.event.ISubscribable;
import ninjabrainbot.event.ObservableProperty;

public class PreviewThrow implements IThrow {

	private final ISubscribable<IThrow> whenModified = new ObservableProperty<IThrow>();

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
	public double xInOverworld() {
		return x;
	}

	@Override
	public double zInOverworld() {
		return z;
	}

	@Override
	public IThrow withCorrection(double correction) {
		this.correction = correction;
		return this;
	}

	@Override
	public double xInPlayerDimension() {
		return x;
	}

	@Override
	public double yInPlayerDimension() {
		return 80;
	}

	@Override
	public double zInPlayerDimension() {
		return z;
	}

	@Override
	public double alpha() {
		return alpha + correction;
	}

	@Override
	public double getStd() {
		return 0;
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
	public double beta() {
		return 0;
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
	public int getStdProfileNumber() {
		return 0;
	}

	@Override
	public boolean isMcVersion1_12() {
		return false;
	}

	@Override
	public boolean isBoatThrow() {
		return false;
	}

}
