package ninjabrainbot.data.actions;

import ninjabrainbot.data.calculator.highprecision.BoatState;
import ninjabrainbot.data.calculator.highprecision.IBoatDataState;

public class ToggleEnteringBoatAction implements IAction {

	private final IBoatDataState boatDataState;

	public ToggleEnteringBoatAction(IBoatDataState boatDataState) {
		this.boatDataState = boatDataState;
	}

	@Override
	public void execute() {
		boatDataState.enteringBoat().set(!boatDataState.enteringBoat().get());
		if (boatDataState.enteringBoat().get()) {
			boatDataState.boatState().set(BoatState.MEASURING);
		} else {
			boatDataState.boatState().set((boatDataState.boatAngle().get() == null) ? BoatState.NONE : BoatState.VALID);
		}
	}
}
