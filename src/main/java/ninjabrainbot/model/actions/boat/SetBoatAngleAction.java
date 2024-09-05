package ninjabrainbot.model.actions.boat;

import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.model.actions.IAction;
import ninjabrainbot.model.datastate.highprecision.BoatState;
import ninjabrainbot.model.datastate.highprecision.IBoatDataState;

public class SetBoatAngleAction implements IAction {

	private final IBoatDataState boatDataState;
	private final double angle;
	private final double boatErrorLimit;

	public SetBoatAngleAction(IBoatDataState boatDataState, double boatAngle, NinjabrainBotPreferences preferences) {
		this.boatDataState = boatDataState;
		this.angle = boatAngle;
		boatErrorLimit = preferences.boatErrorLimit.get();
	}

	@Override
	public void execute() {
		if (Math.abs(angle) > 360) {
			boatDataState.boatState().set(BoatState.ERROR);
			return;
		}

		float candidate = (angle >= 0) ? Math.round(angle / 1.40625) * 1.40625f : Math.round(angle / 0.140625) * 0.140625f;
		double rounded = Math.round(candidate * 100) / 100.0;

		if (Math.abs(rounded - angle) > boatErrorLimit) {
			boatDataState.boatState().set(BoatState.ERROR);
			return;
		}

		boatDataState.boatAngle().set(candidate);
		boatDataState.enteringBoat().set(false);
		boatDataState.boatState().set(BoatState.VALID);
	}

}
