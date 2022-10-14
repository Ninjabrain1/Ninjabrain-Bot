package ninjabrainbot.calculator;

import ninjabrainbot.calculator.statistics.IRay;
import ninjabrainbot.util.IModifiable;
import ninjabrainbot.util.ISubscribable;

public interface IThrow extends IRay, IModifiable<IThrow> {

	public double getStd();

	public double alpha_0();

	public double correction();

	public boolean lookingBelowHorizon();

	public boolean isNether();
	
	public void setError(Double error);
	
	public Double error();
	
	public ISubscribable<Double> whenErrorChanged();
	
	public void addCorrection(double angle);
	
	public void setStdProfileNumber(int profileNumber);
	
	public void setStdProfile(IStdProfile stdProfile);

	public int getStdProfileNumber();
	
	public default boolean isMcVersion1_12() {
		return false;
	}

}
