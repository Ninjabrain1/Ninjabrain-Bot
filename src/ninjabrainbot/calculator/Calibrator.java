package ninjabrainbot.calculator;

import java.awt.AWTException;
import java.util.ArrayList;

import ninjabrainbot.io.KeyPresser;
import ninjabrainbot.util.I18n;

public class Calibrator {
	
	KeyPresser keyPresser;
	int delay = 150; // key press delay
	
	Calculator triangulator;
	boolean calibrating;
	ArrayList<Throw> eyeThrows;
	boolean ready; 
	
	Chunk stronghold;
	double lastX;
	double lastZ;

	public Calibrator() {
		triangulator = new Calculator(0.5);
		calibrating = false;
	}

	public boolean isCalibrating() {
		return calibrating;
	}

	public void startCalibrating() throws AWTException {
		calibrating = true;
		eyeThrows = new ArrayList<Throw>();
		keyPresser = new KeyPresser();
		ready = false;
	}

	public void add(Throw t) throws InterruptedException {
		if (!ready) {
			keyPresser.releaseF3C();
			doCommand("clear");
			doCommand("give @p minecraft:ender_eye");
			tp(0, 0, 0, 0);
			ready = true;
		} else {
			if (distanceFromIntendedPosition(t) > 0.05) { // truncation error makes the distance non-zero
				doCommand("say "+ I18n.get("calibrator.you_moved"));
				tp(lastX, lastZ, t.alpha, -31.2);
				return;
			}
			eyeThrows.add(t);
			Chunk closest;
			Chunk prediction;
			if (stronghold == null) {
				Posterior posterior = triangulator.getPosterior(eyeThrows, null);
				prediction = posterior.getMostProbableChunk();
				if (1.0 - prediction.weight < 1e-8) {
					stronghold = prediction;
				}
				closest = posterior.getClosestPossibleChunk(1e-4, t);
			} else {
				closest = stronghold;
				prediction = stronghold;
			}
			double deltaX = closest.x * 16 + 8 - t.x;
			double deltaZ = closest.z * 16 + 8 - t.z;
			double phi = t.alpha * Math.PI / 180.0;
			double perpendicularDistance = 100.0;
			double nextX = t.x + deltaX * 0.8 - Math.cos(phi) * perpendicularDistance;
			double nextZ = t.z + deltaZ * 0.8 - Math.sin(phi) * perpendicularDistance;
			// Face in the general direction of the stronghold
			double nextAlpha = getAlpha(prediction, nextX, nextZ) + (Math.random() - 0.5) * 10.0;
			tp(nextX, nextZ, nextAlpha, -31.2);
			return;
		}
	}
	
	public void changeLastAngle(double delta) {
		int i = eyeThrows.size() - 1;
		if (i == -1)
			return;
		Throw last = eyeThrows.get(i);
		Throw t = new Throw(last.x, last.z, last.alpha + delta, 0, last.correction + delta, false);
		eyeThrows.remove(last);
		eyeThrows.add(t);
	}
	
	private double distanceFromIntendedPosition(Throw t) {
		double dx = lastX - t.x;
		double dz = lastZ - t.z;
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
		double alpha = -180 / Math.PI * Math.atan2(deltax, deltaz); // mod 360 necessary?
		return alpha;
	}
	
	private double getSTD(Chunk result) {
		double[] errors = result.getAngleErrors(eyeThrows);
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
	
	public double getSTD() {
		if (stronghold == null)
			return -1;
		return getSTD(stronghold);
	}
	
	public double[] getErrors() {
		if (stronghold == null)
			return null;
		return stronghold.getAngleErrors(eyeThrows);
	}
	
	public ArrayList<Throw> getThrows() {
		return eyeThrows;
	}
	
	public boolean isReady() {
		return ready;
	}
	
	public int getNumThrows() {
		return eyeThrows.size();
	}
	
	public void stop() {
		keyPresser = null;
		calibrating = false;
		stronghold = null;
	}
	
}
