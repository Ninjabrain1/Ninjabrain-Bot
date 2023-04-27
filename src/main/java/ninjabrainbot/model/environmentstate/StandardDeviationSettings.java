package ninjabrainbot.model.environmentstate;

public class StandardDeviationSettings {

	public final double std;
	public final double altStd;
	public final double manualStd;
	public final double boatStd;

	public StandardDeviationSettings(double std, double altStd, double manualStd, double boatStd) {
		this.std = std;
		this.altStd = altStd;
		this.manualStd = manualStd;
		this.boatStd = boatStd;
	}

}
