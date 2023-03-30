package ninjabrainbot.gui.themeeditor;

import java.util.List;

import ninjabrainbot.data.IDataState;
import ninjabrainbot.data.alladvancements.AllAdvancementsDataState;
import ninjabrainbot.data.alladvancements.IAllAdvancementsDataState;
import ninjabrainbot.data.blind.BlindResult;
import ninjabrainbot.data.calculator.ICalculatorResult;
import ninjabrainbot.data.ResultType;
import ninjabrainbot.data.datalock.AlwaysUnlocked;
import ninjabrainbot.data.datalock.IModificationLock;
import ninjabrainbot.data.datalock.LockableField;
import ninjabrainbot.data.divine.DivineContext;
import ninjabrainbot.data.divine.DivineResult;
import ninjabrainbot.data.divine.Fossil;
import ninjabrainbot.data.divine.IDivineContext;
import ninjabrainbot.data.endereye.IThrow;
import ninjabrainbot.data.endereye.IThrowSet;
import ninjabrainbot.data.endereye.ThrowSet;
import ninjabrainbot.data.highprecision.BoatDataState;
import ninjabrainbot.data.highprecision.IBoatDataState;
import ninjabrainbot.data.stronghold.ChunkPrediction;
import ninjabrainbot.event.IObservable;
import ninjabrainbot.event.ObservableField;

public class PreviewDataState implements IDataState {

	private final IBoatDataState boatDataState;
	private final IAllAdvancementsDataState allAdvancementsDataState;

	private final DivineContext divineContext;
	private final ThrowSet throwSet;
	private final ObservableField<Boolean> locked;

	private final ObservableField<ResultType> resultType;
	private final ObservableField<ICalculatorResult> calculatorResult;
	private final ObservableField<IThrow> playerPos;
	private final ObservableField<ChunkPrediction> topPrediction;
	private final ObservableField<BlindResult> blindResult;
	private final ObservableField<DivineResult> divineResult;

	public PreviewDataState(ICalculatorResult result, List<IThrow> eyeThrows, Fossil f) {
		this();
		calculatorResult.set(result);
		topPrediction.set(result.getBestPrediction());
		for (IThrow t : eyeThrows) {
			throwSet.add(t);
		}
		divineContext.setFossil(f);
	}

	public PreviewDataState() {
		IModificationLock modificationLock = new AlwaysUnlocked();
		divineContext = new DivineContext(modificationLock);
		throwSet = new ThrowSet(modificationLock);
		playerPos = new LockableField<>(modificationLock);
		locked = new LockableField<>(false, modificationLock);
		resultType = new LockableField<>(ResultType.NONE, modificationLock);
		calculatorResult = new LockableField<>(modificationLock);
		topPrediction = new LockableField<>(modificationLock);
		blindResult = new LockableField<>(modificationLock);
		divineResult = new LockableField<>(modificationLock);

		boatDataState = new BoatDataState(modificationLock);
		allAdvancementsDataState = new AllAdvancementsDataState(topPrediction, modificationLock);
	}

	@Override
	public IDivineContext getDivineContext() {
		return divineContext;
	}

	@Override
	public IThrowSet getThrowSet() {
		return throwSet;
	}

	@Override
	public IObservable<ICalculatorResult> calculatorResult() {
		return calculatorResult;
	}

	@Override
	public IObservable<ChunkPrediction> topPrediction() {
		return topPrediction;
	}

	@Override
	public IObservable<BlindResult> blindResult() {
		return blindResult;
	}

	@Override
	public IObservable<DivineResult> divineResult() {
		return divineResult;
	}

	@Override
	public IObservable<Boolean> locked() {
		return locked;
	}

	@Override
	public IObservable<ResultType> resultType() {
		return resultType;

	}

	@Override
	public IAllAdvancementsDataState allAdvancementDataState() {
		return allAdvancementsDataState;
	}

	@Override
	public IBoatDataState boatDataState() {
		return boatDataState;
	}

	@Override
	public void toggleLocked() {
		locked.set(!locked.get());
	}

	@Override
	public void reset() {
	}

	@Override
	public IObservable<IThrow> playerPosition() {
		return playerPos;
	}

}
