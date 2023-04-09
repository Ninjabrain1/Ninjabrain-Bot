package ninjabrainbot.gui.themeeditor;

import ninjabrainbot.data.calculator.common.IPlayerPosition;
import ninjabrainbot.data.calculator.common.LimitedPlayerPosition;
import ninjabrainbot.data.calculator.endereye.IEnderEyeThrow;
import ninjabrainbot.event.ISubscribable;
import ninjabrainbot.event.ObservableProperty;

public class PreviewEnderEyeThrow implements IEnderEyeThrow {

	private final ISubscribable<IEnderEyeThrow> whenModified = new ObservableProperty<>();

	double x, z, alpha, correction;

	public PreviewEnderEyeThrow(double x, double z) {
		this(x, z, 0, 0);
	}

	public PreviewEnderEyeThrow(double x, double z, double alpha, double correction) {
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
	public double verticalAngle() {
		return 0;
	}

	@Override
	public IEnderEyeThrow withCorrection(double correction) {
		this.correction = correction;
		return this;
	}

	@Override
	public double horizontalAngle() {
		return alpha + correction;
	}

	@Override
	public double getStd() {
		return 0;
	}

	@Override
	public double horizontalAngleWithoutCorrection() {
		return alpha;
	}

	@Override
	public double correction() {
		return correction;
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

	@Override
	public IPlayerPosition getPlayerPosition() {
		return new LimitedPlayerPosition(x, z, horizontalAngle());
	}

}
