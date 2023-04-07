package ninjabrainbot.gui.themeeditor;

import java.util.List;

import ninjabrainbot.data.IDataState;
import ninjabrainbot.data.ResultType;
import ninjabrainbot.data.calculator.ICalculatorResult;
import ninjabrainbot.data.calculator.alladvancements.AllAdvancementsDataState;
import ninjabrainbot.data.calculator.alladvancements.IAllAdvancementsDataState;
import ninjabrainbot.data.calculator.blind.BlindResult;
import ninjabrainbot.data.calculator.divine.DivineContext;
import ninjabrainbot.data.calculator.divine.DivineResult;
import ninjabrainbot.data.calculator.divine.Fossil;
import ninjabrainbot.data.calculator.divine.IDivineContext;
import ninjabrainbot.data.calculator.endereye.IThrow;
import ninjabrainbot.data.calculator.highprecision.BoatDataState;
import ninjabrainbot.data.calculator.highprecision.IBoatDataState;
import ninjabrainbot.data.calculator.stronghold.ChunkPrediction;
import ninjabrainbot.data.datalock.AlwaysUnlocked;
import ninjabrainbot.data.datalock.IModificationLock;
import ninjabrainbot.data.temp.DataComponent;
import ninjabrainbot.data.temp.IDataComponent;
import ninjabrainbot.data.temp.IListComponent;
import ninjabrainbot.data.temp.ListComponent;
import ninjabrainbot.event.IObservable;
import ninjabrainbot.event.ObservableField;

public class PreviewDataState implements IDataState {

	private final IBoatDataState boatDataState;
	private final IAllAdvancementsDataState allAdvancementsDataState;

	private final DivineContext divineContext;
	private final ListComponent<IThrow> throwSet;
	private final DataComponent<Boolean> locked;
	private final DataComponent<IThrow> playerPos;

	private final ObservableField<ResultType> resultType;
	private final ObservableField<ICalculatorResult> calculatorResult;
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
		divineContext.fossil.set(f);
	}

	public PreviewDataState() {
		IModificationLock modificationLock = new AlwaysUnlocked();
		divineContext = new DivineContext(null);
		throwSet = new ListComponent<>(null, 10);
		playerPos = new DataComponent<>(null);
		locked = new DataComponent<>(null, false);
		resultType = new ObservableField<>(ResultType.NONE);
		calculatorResult = new ObservableField<>();
		topPrediction = new ObservableField<>();
		blindResult = new ObservableField<>();
		divineResult = new ObservableField<>();

		boatDataState = new BoatDataState(null);
		allAdvancementsDataState = new AllAdvancementsDataState(topPrediction, null);
	}

	@Override
	public IDivineContext getDivineContext() {
		return divineContext;
	}

	@Override
	public IListComponent<IThrow> getThrowSet() {
		return throwSet;
	}

	@Override
	public IDataComponent<IThrow> playerPosition() {
		return playerPos;
	}

	@Override
	public IDataComponent<Boolean> locked() {
		return locked;
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
	public IObservable<ResultType> resultType() {
		return resultType;

	}

	@Override
	public IAllAdvancementsDataState allAdvancementsDataState() {
		return allAdvancementsDataState;
	}

	@Override
	public IBoatDataState boatDataState() {
		return boatDataState;
	}

}
