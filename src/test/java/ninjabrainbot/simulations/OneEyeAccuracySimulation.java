package ninjabrainbot.simulations;

import java.util.Random;

import ninjabrainbot.data.calculator.Calculator;
import ninjabrainbot.data.calculator.CalculatorSettings;
import ninjabrainbot.data.calculator.ICalculatorResult;
import ninjabrainbot.data.common.IPosition;
import ninjabrainbot.data.datalock.AlwaysUnlocked;
import ninjabrainbot.data.datalock.IModificationLock;
import ninjabrainbot.data.divine.DivineContext;
import ninjabrainbot.data.divine.IDivineContext;
import ninjabrainbot.data.endereye.IThrow;
import ninjabrainbot.data.endereye.ThrowSet;
import ninjabrainbot.data.stronghold.Chunk;
import ninjabrainbot.data.stronghold.Ring;
import ninjabrainbot.event.ObservableField;
import ninjabrainbot.io.preferences.MultipleChoicePreferenceDataTypes.McVersion;
import ninjabrainbot.util.Logger;
import ninjabrainbot.util.TestThrow;

public class OneEyeAccuracySimulation {

	private static final int ITERATIONS = 1000;
	private static final double std = 0.005;

	private static Calculator calculator;
	private static IDivineContext divineContext;
	private static final IModificationLock lock = new AlwaysUnlocked();
	private static final Random random = new Random();
	private static final Ring ring = Ring.get(0);

	public static void main(String[] args) {
		Logger.enabled = false;
		calculator = new Calculator(new CalculatorSettings());
		divineContext = new DivineContext(lock);

		for (int r = 0; r < 3000; r += 100) {
			int successes = 0;
			int total = 0;
			for (int j = 0; j < ITERATIONS; j++) {
				double phi = random.nextDouble() * Math.PI * 2.0;
				double x = r * Math.sin(phi);
				double z = r * Math.cos(phi);

				IThrow position = new TestThrow(x, z, 0, std);
				Chunk closestStronghold = getClosestStronghold(sampleThreeStrongholdsFirstRing(), position);

				double phiToStronghold = -closestStronghold.getAngleError(McVersion.PRE_119, position);
				double sampledError = (random.nextDouble() - random.nextDouble()) * 0.01;
				IThrow eyeThrow = new TestThrow(x, z, phiToStronghold + sampledError, std);

				ICalculatorResult calculatorResult = calculateOneEye(eyeThrow);
				total += 1;
				successes += calculatorResult.getBestPrediction().chunk.equals(closestStronghold) ? 1 : 0;
				calculatorResult.dispose();
			}
			System.out.println(r + ": " + (float) successes / total);
		}
	}

	private static ICalculatorResult calculateOneEye(IThrow eyeThrow) {
		ThrowSet throwSet = new ThrowSet(lock);
		throwSet.add(eyeThrow);
		ICalculatorResult calculatorResult = calculator.triangulate(throwSet, new ObservableField<>(eyeThrow), divineContext);
		throwSet.dispose();
		return calculatorResult;
	}

	private static Chunk[] sampleThreeStrongholdsFirstRing() {
		double phi = random.nextDouble() * Math.PI * 2.0;
		double r = ring.innerRadius + random.nextDouble() * (ring.outerRadius - ring.innerRadius);
		Chunk stronghold0 = new Chunk((int) (r * Math.sin(phi)), (int) (r * Math.cos(phi)));
		phi += Math.PI * 2.0 / 3.0;
		Chunk stronghold1 = new Chunk((int) (r * Math.sin(phi)), (int) (r * Math.cos(phi)));
		phi += Math.PI * 2.0 / 3.0;
		Chunk stronghold2 = new Chunk((int) (r * Math.sin(phi)), (int) (r * Math.cos(phi)));
		return new Chunk[] { stronghold0, stronghold1, stronghold2 };
	}

	private static Chunk getClosestStronghold(Chunk[] strongholds, IPosition position) {
		Chunk closestStronghold = null;
		double minDistance = Double.MAX_VALUE;
		for (Chunk stronghold : strongholds) {
			double distance = stronghold.getOverworldDistance(McVersion.PRE_119, position);
			if (distance < minDistance) {
				minDistance = distance;
				closestStronghold = stronghold;
			}
		}
		return closestStronghold;
	}

}
