package ninjabrainbot.model.datastate.calibrator;

import java.awt.AWTException;

import ninjabrainbot.event.DisposeHandler;
import ninjabrainbot.event.IDisposable;
import ninjabrainbot.event.IObservable;
import ninjabrainbot.event.IReadOnlyList;
import ninjabrainbot.event.ISubscribable;
import ninjabrainbot.event.ObservableField;
import ninjabrainbot.event.ObservableProperty;
import ninjabrainbot.io.KeyPresser;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.io.preferences.enums.McVersion;
import ninjabrainbot.model.actions.endereye.ChangeLastAngleAction;
import ninjabrainbot.model.datastate.calculator.Calculator;
import ninjabrainbot.model.datastate.calculator.ICalculator;
import ninjabrainbot.model.datastate.calculator.ICalculatorResult;
import ninjabrainbot.model.datastate.common.IDetailedPlayerPosition;
import ninjabrainbot.model.datastate.common.IPlayerPositionInputSource;
import ninjabrainbot.model.datastate.divine.DivineContext;
import ninjabrainbot.model.datastate.divine.IDivineContext;
import ninjabrainbot.model.datastate.endereye.IEnderEyeThrow;
import ninjabrainbot.model.datastate.endereye.NormalEnderEyeThrow;
import ninjabrainbot.model.datastate.highprecision.BoatEnderEyeThrow;
import ninjabrainbot.model.datastate.stronghold.Chunk;
import ninjabrainbot.model.domainmodel.IListComponent;
import ninjabrainbot.model.domainmodel.ListComponent;
import ninjabrainbot.model.environmentstate.CalculatorSettings;
import ninjabrainbot.model.environmentstate.StandardDeviationSettings;
import ninjabrainbot.util.I18n;

public class Calibrator implements IDisposable {

	private final KeyPresser keyPresser;
	private final int delay = 150; // key press delay

	private final ICalculator calculator;
	private final NinjabrainBotPreferences preferences;
	private final IObservable<Boolean> locked = new ObservableField<>(false);
	private final IListComponent<IEnderEyeThrow> throwList;
	private boolean ready;

	private Chunk stronghold;
	private double lastX;
	private double lastZ;

	private final IDivineContext divineContext = new DivineContext(null);

	private final DisposeHandler disposeHandler = new DisposeHandler();
	private final ObservableProperty<Calibrator> whenModified = new ObservableProperty<>();

	private final boolean isBoatThrowCalibrator;

	public Calibrator(CalculatorSettings calculatorSettings, IPlayerPositionInputSource playerPositionInputSource, NinjabrainBotPreferences preferences, boolean isBoatThrowCalibrator) {
		try {
			keyPresser = new KeyPresser();
		} catch (AWTException e) {
			throw new RuntimeException(e);
		}
		this.preferences = preferences;
		calculator = new Calculator(calculatorSettings, new StandardDeviationSettings(0.2, 0.2, 0.2, 0.2));
		throwList = new ListComponent<>(null, 100);
		disposeHandler.add(playerPositionInputSource.whenNewDetailedPlayerPositionInputted().subscribe(this::onNewPlayerPositionInputted));
		disposeHandler.add(preferences.hotkeyIncrement.whenTriggered().subscribe(__ -> new ChangeLastAngleAction(throwList, locked, preferences, true).execute()));
		disposeHandler.add(preferences.hotkeyDecrement.whenTriggered().subscribe(__ -> new ChangeLastAngleAction(throwList, locked, preferences, false).execute()));
		disposeHandler.add(throwList.subscribe(__ -> whenModified.notifySubscribers(this)));
		ready = false;
		this.isBoatThrowCalibrator = isBoatThrowCalibrator;
	}

	private void onNewPlayerPositionInputted(IDetailedPlayerPosition playerPosition) {
		try {
			if (isBoatThrowCalibrator) {
				add(new BoatEnderEyeThrow(playerPosition, preferences, 0));
			}
			else {
				add(new NormalEnderEyeThrow(playerPosition, preferences.crosshairCorrection.get()));
			}
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	private void add(IEnderEyeThrow t) throws InterruptedException {
		if (!ready) {
			keyPresser.releaseF3C();
			doCommand("clear");
			Thread.sleep(delay);
			doCommand("give @p minecraft:ender_eye");
			Thread.sleep(delay);
			tp(0, 0, 0, 0);
			ready = true;
		} else {
			if (distanceFromIntendedPosition(t) > 0.05) { // truncation error makes the distance non-zero
				doCommand("say " + I18n.get("calibrator.you_moved"));
				tp(lastX, lastZ, t.horizontalAngle(), -31.2);
				return;
			}
			throwList.add(t);
			Chunk closest;
			Chunk prediction;
			if (stronghold == null) {
				ICalculatorResult result = calculator.triangulate(throwList.get(), new ObservableField<>(t.getPlayerPosition()), divineContext);
				prediction = result.getBestPrediction().chunk;
				if (1.0 - prediction.weight < 1e-8) {
					stronghold = prediction;
				}
				closest = prediction;
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
			if (isBoatThrowCalibrator) {
				double preMultiplier = preferences.sensitivity.get() * 0.6f + 0.2f;
				preMultiplier = preMultiplier * preMultiplier * preMultiplier * 8.0f;
				double minInc = preMultiplier * 0.15D;
				nextAlpha = Math.round(nextAlpha / minInc) * minInc;
			}
			tp(nextX, nextZ, nextAlpha, -31.2);
		}
	}

	private double distanceFromIntendedPosition(IEnderEyeThrow t) {
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
		Thread.sleep(delay);
		keyPresser.openCommand();
		Thread.sleep(delay);
		keyPresser.paste(String.format("setblock %d 255 %d minecraft:diamond_block", (int) Math.floor(x), (int) Math.floor(z)));
		keyPresser.enter();
		// tp
		Thread.sleep(delay);
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
		double[] errors = result.getAngleErrors(version, throwList.get());
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
		return stronghold.getAngleErrors(version, throwList.get());
	}

	public IReadOnlyList<IEnderEyeThrow> getThrows() {
		return throwList.get();
	}

	public boolean isReady() {
		return ready;
	}

	public int getNumThrows() {
		return throwList.get().size();
	}

	public ISubscribable<Calibrator> whenModified() {
		return whenModified;
	}

	@Override
	public void dispose() {
		disposeHandler.dispose();
	}

}
