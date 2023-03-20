package ninjabrainbot.data.highprecision;

import ninjabrainbot.event.IObservable;

public interface IBoatDataState {

	public IObservable<Boolean> enteringBoat();

	public IObservable<Float> boatAngle();

	public IObservable<BoatState> boatState();

}
