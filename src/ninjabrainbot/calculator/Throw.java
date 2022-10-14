package ninjabrainbot.calculator;

import ninjabrainbot.Main;
import ninjabrainbot.util.IDisposable;
import ninjabrainbot.util.ISubscribable;
import ninjabrainbot.util.Modifiable;
import ninjabrainbot.util.ObservableField;
import ninjabrainbot.util.Subscription;

/**
 * Represents an eye of ender throw.
 */
public class Throw extends Modifiable<IThrow> implements IThrow, IDisposable {

	private final double x, z, alpha_0, beta;
	private final boolean nether;

	private IStdProfile stdProfile;
	private int stdProfileNumber;
	private double std;
	private double correction;

	private Subscription stdProfileSubscription;
	private ObservableField<Double> error = new ObservableField<Double>(0.0);

	public Throw(double x, double z, double alpha, double beta, boolean nether) {
		this.x = x;
		this.z = z;
		alpha = alpha % 360.0;
		if (alpha < -180.0) {
			alpha += 360.0;
		} else if (alpha > 180.0) {
			alpha -= 360.0;
		}
		this.alpha_0 = alpha;
		this.beta = beta;
		this.nether = nether;
		this.correction = 0;
	}

	@Override
	public String toString() {
		return "x=" + x + ", z=" + z + ", alpha=" + alpha_0;
	}

	/**
	 * Returns a Throw object if the given string is the result of an F3+C command,
	 * null otherwise.
	 */
	public static IThrow parseF3C(String string) {
		if (!(string.startsWith("/execute in minecraft:overworld run tp @s")
				|| string.startsWith("/execute in minecraft:the_nether run tp @s"))) {
			return null;
		}
		String[] substrings = string.split(" ");
		if (substrings.length != 11)
			return null;
		try {
			boolean nether = substrings[2].equals("minecraft:the_nether");
			double x = Double.parseDouble(substrings[6]);
			double z = Double.parseDouble(substrings[8]);
			double alpha = Double.parseDouble(substrings[9]);
			double beta = Double.parseDouble(substrings[10]);
			alpha += Main.preferences.crosshairCorrection.get();
			return new Throw(x, z, alpha, beta, nether);
		} catch (NullPointerException | NumberFormatException e) {
			return null;
		}
	}

	@Override
	public void setStdProfileNumber(int profileNumber) {
		stdProfileNumber = profileNumber;
		updateStd();
	}

	@Override
	public void setStdProfile(IStdProfile stdProfile) {
		this.stdProfile = stdProfile;
		if (stdProfileSubscription != null)
			stdProfileSubscription.cancel();
		stdProfileSubscription = stdProfile.whenModified().subscribe(__ -> updateStd());
		setStdProfileNumber(stdProfile.getInitialProfileNumber(this));
	}

	@Override
	public int getStdProfileNumber() {
		return stdProfileNumber;
	}

	@Override
	public boolean lookingBelowHorizon() {
		return beta > 0;
	}

	@Override
	public double x() {
		return x;
	}

	@Override
	public double z() {
		return z;
	}

	@Override
	public double alpha() {
		return alpha_0 + correction;
	}

	@Override
	public double correction() {
		return correction;
	}

	@Override
	public double alpha_0() {
		return alpha_0;
	}

	@Override
	public boolean isNether() {
		return nether;
	}

	@Override
	public double getStd() {
		return std;
	}
	
	@Override
	public void setError(Double error) {
		this.error.set(error);
	}
	
	@Override
	public Double error() {
		return error.get();
	}
	
	@Override
	public ISubscribable<Double> whenErrorChanged() {
		return error;
	}

	@Override
	public void addCorrection(double angle) {
		correction += angle;
		whenModified.notifySubscribers(this);
	}

	private void updateStd() {
		if(stdProfile == null)
			return;
		double newStd = stdProfile.getStd(stdProfileNumber);
		if (newStd == std)
			return;
		std = newStd;
		whenModified.notifySubscribers(this);
	}

	@Override
	public void dispose() {
		if (stdProfileSubscription != null)
			stdProfileSubscription.cancel();
	}

}
