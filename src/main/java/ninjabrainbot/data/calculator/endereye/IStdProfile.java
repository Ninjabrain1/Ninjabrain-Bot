package ninjabrainbot.data.calculator.endereye;

import ninjabrainbot.event.IModifiable;

public interface IStdProfile extends IModifiable<IStdProfile> {

	public double getStd(int profileNumber);

	public int getNumberOfProfiles();

	public int getInitialProfileNumber(IThrow t);

}
