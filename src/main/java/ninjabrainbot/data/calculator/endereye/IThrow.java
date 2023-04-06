package ninjabrainbot.data.calculator.endereye;

import ninjabrainbot.data.calculator.statistics.IRay;
import ninjabrainbot.event.IModifiable;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;

public interface IThrow extends IRay, IModifiable<IThrow> {

	double xInPlayerDimension();

	double zInPlayerDimension();

	double yInPlayerDimension();

	double getStd();

	double rawAlpha();

	double alpha_0();

	double beta();

	double correction();

	boolean lookingBelowHorizon();

	boolean isNether();

	void addCorrection(boolean positive, NinjabrainBotPreferences preferences);

	void setStdProfileNumber(int profileNumber);

	void setStdProfile(IStdProfile stdProfile);

	int getStdProfileNumber();

	default boolean isMcVersion1_12() {
		return false;
	}

	default boolean isBoatThrow() {
		return false;
	}

}
