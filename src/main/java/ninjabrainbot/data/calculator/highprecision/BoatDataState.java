package ninjabrainbot.data.calculator.highprecision;

import ninjabrainbot.data.datalock.IModificationLock;
import ninjabrainbot.data.datalock.LockableField;
import ninjabrainbot.data.temp.DataComponent;
import ninjabrainbot.data.temp.IDomainModel;
import ninjabrainbot.event.IObservable;
import ninjabrainbot.event.ObservableField;

public class BoatDataState implements IBoatDataState {

	private final DataComponent<Boolean> enteringBoat;
	private final DataComponent<Float> boatAngle;
	private final DataComponent<BoatState> boatState;

	public BoatDataState(IDomainModel domainModel) {
		enteringBoat = new DataComponent<>(domainModel, false);
		boatAngle = new DataComponent<>(domainModel);
		boatState = new DataComponent<>(domainModel, BoatState.NONE);
	}

	public void reset() {
		enteringBoat.set(false);
		boatAngle.set(null);
		boatState.set(BoatState.NONE);
	}

	public boolean setBoatAngle(double angle, float boatErrorLimit) {
		if (Math.abs(angle) > 360) {
			boatAngle.set(null);
			boatState.set(BoatState.ERROR);
			return false;
		}

		float candidate = Math.round(angle / 1.40625) * 1.40625f;
		double rounded = Double.parseDouble(String.format("%.2f", candidate));

		if (Math.abs(rounded - angle) > boatErrorLimit) {
			boatAngle.set(null);
			boatState.set(BoatState.ERROR);
			return false;
		}

		boatAngle.set(candidate);
		enteringBoat.set(false);
		boatState.set(BoatState.VALID);
		return true;
	}

	public void toggleEnteringBoat() {
		enteringBoat.set(!enteringBoat.get());
		if (enteringBoat.get()) {
			boatState.set(BoatState.MEASURING);
		} else {
			boatState.set((boatAngle.get() == null) ? BoatState.NONE : BoatState.VALID);
		}
	}

	@Override
	public IObservable<Boolean> enteringBoat() {
		return enteringBoat;
	}

	@Override
	public IObservable<Float> boatAngle() {
		return boatAngle;
	}

	@Override
	public IObservable<BoatState> boatState() {
		return boatState;
	}

}
