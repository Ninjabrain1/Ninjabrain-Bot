package ninjabrainbot.data.actions;

import ninjabrainbot.data.calculator.highprecision.BoatState;
import ninjabrainbot.data.calculator.highprecision.IBoatDataState;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;

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
			boatDataState.boatAngle().set(null);
			boatDataState.boatState().set(BoatState.ERROR);
			return;
		}

		float candidate = Math.round(angle / 1.40625) * 1.40625f;
		double rounded = Double.parseDouble(String.format("%.2f", candidate));

		if (Math.abs(rounded - angle) > boatErrorLimit) {
			boatDataState.boatAngle().set(null);
			boatDataState.boatState().set(BoatState.ERROR);
			return;
		}

		boatDataState.boatAngle().set(candidate);
		boatDataState.enteringBoat().set(false);
		boatDataState.boatState().set(BoatState.VALID);
	}

}
