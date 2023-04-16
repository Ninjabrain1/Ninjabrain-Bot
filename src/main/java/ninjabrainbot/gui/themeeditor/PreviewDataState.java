package ninjabrainbot.gui.themeeditor;

import java.util.List;

import ninjabrainbot.model.datastate.IDataState;
import ninjabrainbot.model.datastate.common.ResultType;
import ninjabrainbot.model.datastate.calculator.ICalculatorResult;
import ninjabrainbot.model.datastate.alladvancements.IAllAdvancementsDataState;
import ninjabrainbot.model.datastate.blind.BlindResult;
import ninjabrainbot.model.datastate.common.IPlayerPosition;
import ninjabrainbot.model.datastate.divine.DivineContext;
import ninjabrainbot.model.datastate.divine.DivineResult;
import ninjabrainbot.model.datastate.divine.Fossil;
import ninjabrainbot.model.datastate.divine.IDivineContext;
import ninjabrainbot.model.datastate.endereye.IEnderEyeThrow;
import ninjabrainbot.model.datastate.highprecision.BoatDataState;
import ninjabrainbot.model.datastate.highprecision.IBoatDataState;
import ninjabrainbot.model.datastate.stronghold.ChunkPrediction;
import ninjabrainbot.model.domainmodel.DataComponent;
import ninjabrainbot.model.domainmodel.IDataComponent;
import ninjabrainbot.model.domainmodel.IListComponent;
import ninjabrainbot.model.domainmodel.ListComponent;
import ninjabrainbot.event.IObservable;
import ninjabrainbot.event.ObservableField;

public class PreviewDataState implements IDataState {

	private final IBoatDataState boatDataState;
	private final IAllAdvancementsDataState allAdvancementsDataState;

	private final DivineContext divineContext;
	private final ListComponent<IEnderEyeThrow> throwSet;
	private final DataComponent<Boolean> locked;
	private final DataComponent<IPlayerPosition> playerPosition;

	private final ObservableField<ResultType> resultType;
	private final ObservableField<ICalculatorResult> calculatorResult;
	private final ObservableField<ChunkPrediction> topPrediction;
	private final ObservableField<BlindResult> blindResult;
	private final ObservableField<DivineResult> divineResult;

	public PreviewDataState(ICalculatorResult result, List<IEnderEyeThrow> eyeThrows, Fossil f) {
		this();
		calculatorResult.set(result);
		topPrediction.set(result.getBestPrediction());
		for (IEnderEyeThrow t : eyeThrows) {
			throwSet.add(t);
		}
		divineContext.fossil.set(f);
	}

	public PreviewDataState() {
		divineContext = new DivineContext(null);
		throwSet = new ListComponent<>(null, 10);
		playerPosition = new DataComponent<>(null);
		locked = new DataComponent<>(null, false);
		resultType = new ObservableField<>(ResultType.NONE);
		calculatorResult = new ObservableField<>();
		topPrediction = new ObservableField<>();
		blindResult = new ObservableField<>();
		divineResult = new ObservableField<>();

		boatDataState = new BoatDataState(null);
		allAdvancementsDataState = new PreviewAllAdvancementsDataState();
	}

	@Override
	public IDivineContext getDivineContext() {
		return divineContext;
	}

	@Override
	public IListComponent<IEnderEyeThrow> getThrowList() {
		return throwSet;
	}

	@Override
	public IDataComponent<IPlayerPosition> playerPosition() {
		return playerPosition;
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
