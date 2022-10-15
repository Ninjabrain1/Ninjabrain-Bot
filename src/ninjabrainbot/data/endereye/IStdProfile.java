package ninjabrainbot.data.endereye;

import ninjabrainbot.util.IModifiable;

public interface IStdProfile extends IModifiable<IStdProfile> {
	
	public double getStd(int profileNumber);
	
	public int getNumberOfProfiles();
	
	public int getInitialProfileNumber(IThrow t);

}
