package ninjabrainbot.model.actions.boat;

import ninjabrainbot.model.datastate.IDataState;
import ninjabrainbot.model.actions.IAction;
import ninjabrainbot.model.datastate.highprecision.BoatState;
import ninjabrainbot.model.datastate.highprecision.IBoatDataState;

public class ToggleEnteringBoatAction implements IAction {

	private final IDataState dataState;

	public ToggleEnteringBoatAction(IDataState dataState) {
		this.dataState = dataState;
	}

	@Override
	public void execute() {
		if (dataState.locked().get())
			return;

		IBoatDataState boatDataState = dataState.boatDataState();
		boatDataState.enteringBoat().set(!boatDataState.enteringBoat().get());
		boatDataState.boatState().set(boatDataState.enteringBoat().get() ? BoatState.MEASURING : BoatState.NONE);
		boatDataState.boatAngle().set(null);
	}
}
