package ninjabrainbot.model.actions.boat;

import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.model.actions.IAction;
import ninjabrainbot.model.datastate.highprecision.IBoatDataState;

public class ReduceBoatAngleMod360Action implements IAction {

	private final IBoatDataState boatDataState;
	private final double angle;
	private final double sensitivity;

	public ReduceBoatAngleMod360Action(IBoatDataState boatDataState, double playerAngle, double sensitivity) {
		this.boatDataState = boatDataState;
		this.angle = playerAngle;
		this.sensitivity = sensitivity;
	}

	@Override
	public void execute() {
		float boatAngle = boatDataState.boatAngle().get();

		double preMultiplier = sensitivity * (double) 0.6f + (double) 0.2f;
		preMultiplier = preMultiplier * preMultiplier * preMultiplier * 8.0D;
		double minInc = preMultiplier * 0.15D;
		float trueAngle = (float) (boatAngle + Math.round((angle - boatAngle) / minInc) * minInc);

		float change = trueAngle - (trueAngle % 360);
		boatDataState.boatAngle().set(boatAngle - change);
		boatDataState.reducingModulo360().set(false);
	}

}
