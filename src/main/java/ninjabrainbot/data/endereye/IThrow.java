package ninjabrainbot.data.endereye;

import ninjabrainbot.data.statistics.IRay;
import ninjabrainbot.event.IModifiable;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;

public interface IThrow extends IRay, IModifiable<IThrow> {

	public double getStd();

	public double alpha_0();

	public double correction();

	public boolean lookingBelowHorizon();

	public boolean isNether();

	public void addCorrection(int multiplier, NinjabrainBotPreferences preferences);

	public void setStdProfileNumber(int profileNumber);

	public void setStdProfile(IStdProfile stdProfile);

	public int getStdProfileNumber();

	public default boolean isMcVersion1_12() {
		return false;
	}

}
