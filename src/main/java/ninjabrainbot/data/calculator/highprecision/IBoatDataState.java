package ninjabrainbot.data.calculator.highprecision;

import ninjabrainbot.data.temp.IDataComponent;

public interface IBoatDataState {

	IDataComponent<Boolean> enteringBoat();

	IDataComponent<Float> boatAngle();

	IDataComponent<BoatState> boatState();

}
