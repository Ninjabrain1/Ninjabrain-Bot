package ninjabrainbot.gui.themeeditor;

import java.util.List;

import ninjabrainbot.model.datastate.IDataState;
import ninjabrainbot.model.datastate.alladvancements.IAllAdvancementsDataState;
import ninjabrainbot.model.datastate.blind.BlindResult;
import ninjabrainbot.model.datastate.calculator.ICalculatorResult;
import ninjabrainbot.model.datastate.common.IPlayerPosition;
import ninjabrainbot.model.datastate.common.DetachedDomainModel;
import ninjabrainbot.model.datastate.common.ResultType;
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
import ninjabrainbot.model.domainmodel.IDomainModel;
import ninjabrainbot.model.domainmodel.IDomainModelComponent;
import ninjabrainbot.model.domainmodel.IListComponent;
import ninjabrainbot.model.domainmodel.InferredComponent;
import ninjabrainbot.model.domainmodel.ListComponent;

public class PreviewDataState implements IDataState {

	private final IBoatDataState boatDataState;
	private final IAllAdvancementsDataState allAdvancementsDataState;

	private final DivineContext divineContext;
	private final ListComponent<IEnderEyeThrow> throwSet;
	private final DataComponent<Boolean> locked;
	private final DataComponent<IPlayerPosition> playerPosition;

	private final DataComponent<ResultType> resultType;
	private final InferredComponent<ICalculatorResult> calculatorResult;
	private final InferredComponent<ChunkPrediction> topPrediction;
	private final InferredComponent<BlindResult> blindResult;
	private final InferredComponent<DivineResult> divineResult;

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
		IDomainModel domainModel = new DetachedDomainModel();
		divineContext = new DivineContext(domainModel);
		throwSet = new ListComponent<>("", domainModel, 10);
		playerPosition = new DataComponent<>("", domainModel, null);
		locked = new DataComponent<>("", domainModel, false);
		resultType = new DataComponent<>("", domainModel, ResultType.NONE);
		calculatorResult = new InferredComponent<>(domainModel);
		topPrediction = new InferredComponent<>(domainModel);
		blindResult = new InferredComponent<>(domainModel);
		divineResult = new InferredComponent<>(domainModel);

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
	public IDomainModelComponent<ICalculatorResult> calculatorResult() {
		return calculatorResult;
	}

	@Override
	public IDomainModelComponent<ChunkPrediction> topPrediction() {
		return topPrediction;
	}

	@Override
	public IDomainModelComponent<BlindResult> blindResult() {
		return blindResult;
	}

	@Override
	public IDomainModelComponent<DivineResult> divineResult() {
		return divineResult;
	}

	@Override
	public IDomainModelComponent<ResultType> resultType() {
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
