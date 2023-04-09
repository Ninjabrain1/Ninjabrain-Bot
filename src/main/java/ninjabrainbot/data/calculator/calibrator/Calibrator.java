package ninjabrainbot.data.calculator.calibrator;

import java.awt.AWTException;

import ninjabrainbot.data.calculator.Calculator;
import ninjabrainbot.data.calculator.divine.DivineContext;
import ninjabrainbot.data.calculator.divine.IDivineContext;
import ninjabrainbot.data.calculator.endereye.IThrow;
import ninjabrainbot.data.calculator.statistics.Posterior;
import ninjabrainbot.data.calculator.stronghold.Chunk;
import ninjabrainbot.event.IDisposable;
import ninjabrainbot.event.IReadOnlyList;
import ninjabrainbot.event.ObservableList;
import ninjabrainbot.io.KeyPresser;
import ninjabrainbot.io.preferences.MultipleChoicePreferenceDataTypes.McVersion;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.util.I18n;

public class Calibrator implements IDisposable {

	KeyPresser keyPresser;
	int delay = 150; // key press delay

	Calculator triangulator;
	boolean calibrating;
	ObservableList<IThrow> eyeThrows;
	boolean ready;

	Chunk stronghold;
	double lastX;
	double lastZ;

	private final IDivineContext divineContext = new DivineContext(null);

	public Calibrator() {
		triangulator = new Calculator();
		calibrating = false;
	}

	public boolean isCalibrating() {
		return calibrating;
	}

	public void startCalibrating() throws AWTException {
		calibrating = true;
		eyeThrows = new ObservableList<>();
		keyPresser = new KeyPresser();
		ready = false;
	}

	public void add(IThrow t) throws InterruptedException {
		if (!ready) {
			keyPresser.releaseF3C();
			doCommand("clear");
			doCommand("give @p minecraft:ender_eye");
			tp(0, 0, 0, 0);
			ready = true;
		} else {
			if (distanceFromIntendedPosition(t) > 0.05) { // truncation error makes the distance non-zero
				doCommand("say " + I18n.get("calibrator.you_moved"));
				tp(lastX, lastZ, t.horizontalAngle(), -31.2);
				return;
			}
			eyeThrows.add(t);
			Chunk closest;
			Chunk prediction;
			if (stronghold == null) {
				Posterior posterior = triangulator.getPosterior(eyeThrows.get(), divineContext);
				prediction = posterior.getMostProbableChunk();
				if (1.0 - prediction.weight < 1e-8) {
					stronghold = prediction;
				}
				closest = posterior.getClosestPossibleChunk(1e-4, t);
			} else {
				closest = stronghold;
				prediction = stronghold;
			}
			double deltaX = closest.x * 16 + 8 - t.xInOverworld();
			double deltaZ = closest.z * 16 + 8 - t.zInOverworld();
			double phi = t.horizontalAngle() * Math.PI / 180.0;
			double perpendicularDistance = 100.0;
			double nextX = t.xInOverworld() + deltaX * 0.8 - Math.cos(phi) * perpendicularDistance;
			double nextZ = t.zInOverworld() + deltaZ * 0.8 - Math.sin(phi) * perpendicularDistance;
			// Face in the general direction of the stronghold
			double nextAlpha = getAlpha(prediction, nextX, nextZ) + (Math.random() - 0.5) * 10.0;
			tp(nextX, nextZ, nextAlpha, -31.2);
		}
	}

	public void changeLastAngle(boolean positive, NinjabrainBotPreferences preferences) {
		int i = eyeThrows.size() - 1;
		if (i == -1) {
		}
//		eyeThrows.get(i).addCorrection(positive, preferences);
	}

	private double distanceFromIntendedPosition(IThrow t) {
		double dx = lastX - t.xInOverworld();
		double dz = lastZ - t.zInOverworld();
		return Math.sqrt(dx * dx + dz * dz);
	}

	private void doCommand(String command) throws InterruptedException {
		keyPresser.openCommand();
		Thread.sleep(delay);
		keyPresser.paste(command);
		keyPresser.enter();
	}

	private void tp(double x, double z, double alpha, double theta) throws InterruptedException {
		// tp
		keyPresser.openCommand();
		Thread.sleep(delay);
		keyPresser.paste(String.format("tp @p %.2f 128 %.2f %.5f %.2f", x, z, alpha, theta));
		keyPresser.enter();
		// place block
		keyPresser.openCommand();
		Thread.sleep(delay);
		keyPresser.paste(String.format("setblock %d 255 %d minecraft:diamond_block", (int) Math.floor(x), (int) Math.floor(z)));
		keyPresser.enter();
		// tp
		keyPresser.openCommand();
		Thread.sleep(delay);
		keyPresser.paste(String.format("tp @p %.2f 256 %.2f %.5f %.2f", x, z, alpha, theta));
		keyPresser.enter();
		//
		lastX = x;
		lastZ = z;
	}

	private double getAlpha(Chunk strongholdChunk, double x, double z) {
		double deltax = strongholdChunk.x * 16 + 8 - x;
		double deltaz = strongholdChunk.z * 16 + 8 - z;
		return -180 / Math.PI * Math.atan2(deltax, deltaz); // mod 360 necessary?
	}

	private double getSTD(McVersion version, Chunk result) {
		double[] errors = result.getAngleErrors(version, eyeThrows.get());
		// Assume 0 mean
		double sqSum = 0;
		for (double e : errors) {
			sqSum += e * e;
		}
		// Unbiased (approximately, otherwise requires the gamma function)
		// If it is not assumed that the mean is 0, replace 0.5 with 1.5
		return Math.sqrt(sqSum / (errors.length - 0.5));
	}

	public boolean isStrongholdDetermined() {
		return stronghold != null;
	}

	public double getSTD(McVersion version) {
		if (stronghold == null)
			return -1;
		return getSTD(version, stronghold);
	}

	public double[] getErrors(McVersion version) {
		if (stronghold == null)
			return null;
		return stronghold.getAngleErrors(version, eyeThrows.get());
	}

	public IReadOnlyList<IThrow> getThrows() {
		return eyeThrows.get();
	}

	public boolean isReady() {
		return ready;
	}

	public int getNumThrows() {
		return eyeThrows.get().size();
	}

	public void stop() {
		keyPresser = null;
		calibrating = false;
		stronghold = null;
	}

	@Override
	public void dispose() {
	}

}
