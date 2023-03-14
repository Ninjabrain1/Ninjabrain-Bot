package ninjabrainbot.data.endereye;

import ninjabrainbot.data.datalock.DataComponent;
import ninjabrainbot.data.datalock.IModificationLock;
import ninjabrainbot.event.IDisposable;
import ninjabrainbot.event.Subscription;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;

/**
 * Represents an eye of ender throw.
 */
public class Throw extends DataComponent<IThrow> implements IThrow, IDisposable {

	private final double x, z, rawAlpha, alpha_0, beta;
	private final boolean nether;

	private IStdProfile stdProfile;
	private int stdProfileNumber;
	private double std;
	private double correction;

	private Subscription stdProfileSubscription;

	public Throw(double x, double z, double alpha, double beta, boolean nether, IModificationLock modificationLock) {
		this(x, z, alpha, alpha, beta, nether, modificationLock);
	}

	public Throw(double x, double z, double rawAlpha, double alpha, double beta, boolean nether, IModificationLock modificationLock) {
		super(modificationLock);
		this.x = x;
		this.z = z;
		this.rawAlpha = rawAlpha;
		alpha %= 360.0;
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
	public static IThrow parseF3C(String string, double crosshairCorrection, IModificationLock modificationLock) {
		if (!(string.startsWith("/execute in minecraft:overworld run tp @s") || string.startsWith("/execute in minecraft:the_nether run tp @s"))) {
			return null;
		}
		String[] substrings = string.split(" ");
		if (substrings.length != 11)
			return null;
		try {
			boolean nether = substrings[2].equals("minecraft:the_nether");
			double x = Double.parseDouble(substrings[6]);
			double z = Double.parseDouble(substrings[8]);
			double rawAlpha = Double.parseDouble(substrings[9]);
			double beta = Double.parseDouble(substrings[10]);
			double alpha = getPreciseAlpha(rawAlpha, crosshairCorrection);
			return new Throw(x, z, rawAlpha, alpha, beta, nether, modificationLock);
		} catch (NullPointerException | NumberFormatException e) {
			return null;
		}
	}

	protected static double getPreciseAlpha(double alpha, double crosshairCorrection) {
		alpha += crosshairCorrection;

		// Determined experimentally, exact cause unknown
		alpha -= 0.00079 * Math.sin((alpha + 45) * Math.PI / 180.0);

		return alpha;
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
	public double rawAlpha() {
		return rawAlpha;
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
	public void addCorrection(boolean positive, NinjabrainBotPreferences preferences) {
		double change = 0.01;
		if (preferences.useTallRes.get()) {
			final double toRad = Math.PI / 180.0;
			change = Math.atan(2 * Math.tan(15 * toRad) / preferences.resolutionHeight.get()) / Math.cos(beta * toRad) / toRad;
		}
		change *= positive ? 1 : -1;
		correction += change;
		notifySubscribers(this);
	}

	private void updateStd() {
		if (stdProfile == null)
			return;
		double newStd = stdProfile.getStd(stdProfileNumber);
		if (newStd == std)
			return;
		std = newStd;
		notifySubscribers(this);
	}

	@Override
	public void dispose() {
		if (stdProfileSubscription != null)
			stdProfileSubscription.cancel();
	}

}
