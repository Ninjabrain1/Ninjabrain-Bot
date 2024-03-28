package ninjabrainbot.model.actions.boat;

import ninjabrainbot.model.actions.IAction;
import ninjabrainbot.model.datastate.IDataState;
import ninjabrainbot.model.datastate.highprecision.IBoatDataState;

public class ResetBoatStateAction implements IAction {

	private final IDataState dataState;

	public ResetBoatStateAction(IDataState dataState) {
		this.dataState = dataState;
	}

	@Override
	public void execute() {
		if (dataState.locked().get())
			return;

		IBoatDataState boatDataState = dataState.boatDataState();
		boatDataState.enteringBoat().reset();
		boatDataState.boatState().reset();
		boatDataState.boatAngle().reset();
	}
}
