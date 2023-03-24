package ninjabrainbot.data.endereye;

import ninjabrainbot.data.statistics.IRay;
import ninjabrainbot.event.IModifiable;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;

public interface IThrow extends IRay, IModifiable<IThrow> {

	public double xInPlayerDimension();

	public double zInPlayerDimension();

	public double getStd();

	public double rawAlpha();

	public double alpha_0();

	public double correction();

	public boolean lookingBelowHorizon();

	public boolean isNether();

	public void addCorrection(boolean positive, NinjabrainBotPreferences preferences);

	public void setStdProfileNumber(int profileNumber);

	public void setStdProfile(IStdProfile stdProfile);

	public int getStdProfileNumber();

	public default boolean isMcVersion1_12() {
		return false;
	}

	public default boolean isBoatThrow() {
		return false;
	}

}
