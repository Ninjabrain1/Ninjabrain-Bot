package ninjabrainbot.model.datastate.calculator;

import ninjabrainbot.event.DisposeHandler;
import ninjabrainbot.event.IDisposable;
import ninjabrainbot.event.IObservable;
import ninjabrainbot.event.IObservableList;
import ninjabrainbot.model.datastate.blind.BlindPosition;
import ninjabrainbot.model.datastate.blind.BlindResult;
import ninjabrainbot.model.datastate.common.IPlayerPosition;
import ninjabrainbot.model.datastate.divine.DivineResult;
import ninjabrainbot.model.datastate.divine.IDivineContext;
import ninjabrainbot.model.datastate.endereye.IEnderEyeThrow;
import ninjabrainbot.model.datastate.stronghold.ChunkPrediction;
import ninjabrainbot.model.datastate.stronghold.TopPredictionProvider;
import ninjabrainbot.model.domainmodel.IDomainModel;
import ninjabrainbot.model.domainmodel.InferredComponent;
import ninjabrainbot.model.environmentstate.IEnvironmentState;

public class CalculatorManager implements ICalculatorManager, IDisposable {

	private ICalculator calculator;

	private final IObservableList<IEnderEyeThrow> throwSet;
	private final IObservable<IPlayerPosition> playerPosition;
	private final IDivineContext divineContext;

	private final InferredComponent<ICalculatorResult> calculatorResult;
	private final InferredComponent<BlindResult> blindResult;
	private final InferredComponent<DivineResult> divineResult;

	private final TopPredictionProvider topPredictionProvider;

	private final DisposeHandler disposeHandler = new DisposeHandler();

	public CalculatorManager(IDomainModel domainModel, IEnvironmentState environmentState, IObservableList<IEnderEyeThrow> throwSet, IObservable<IPlayerPosition> playerPosition, IDivineContext divineContext) {
		this.calculator = environmentState.calculator().get();
		this.throwSet = throwSet;
		this.playerPosition = playerPosition;
		this.divineContext = divineContext;

		calculatorResult = new InferredComponent<>(domainModel);
		blindResult = new InferredComponent<>(domainModel);
		divineResult = new InferredComponent<>(domainModel);

		disposeHandler.add(environmentState.calculator().subscribe(this::setCalculator));
		disposeHandler.add(throwSet.subscribe(this::onThrowSetModified));
		disposeHandler.add(playerPosition.subscribe(this::onPlayerPositionChanged));
		disposeHandler.add(divineContext.fossil().subscribe(this::onFossilChanged));
		topPredictionProvider = disposeHandler.add(new TopPredictionProvider(domainModel, calculatorResult));
	}

	private void onThrowSetModified() {
		updateCalculatorResult();
		updateBlindResult();
		updateDivineResult();
	}

	private void onPlayerPositionChanged() {
		updateBlindResult();
		updateDivineResult();
	}

	private void onFossilChanged() {
		updateCalculatorResult();
		updateBlindResult();
		updateDivineResult();
	}

	private void updateCalculatorResult() {
		if (calculatorResult.get() != null)
			calculatorResult.get().dispose();
		calculatorResult.set(calculator.triangulate(throwSet.get(), playerPosition, divineContext));
	}

	private void updateBlindResult() {
		if (throwSet.get().size() > 0 || playerPosition.get() == null || !playerPosition.get().isInNether()) {
			blindResult.set(null);
			return;
		}
		blindResult.set(calculator.blind(new BlindPosition(playerPosition.get()), divineContext));
	}

	private void updateDivineResult() {
		if (throwSet.get().size() > 0 || (playerPosition.get() != null && playerPosition.get().isInNether())) {
			divineResult.set(null);
			return;
		}
		divineResult.set(calculator.divine(divineContext));
	}

	private void setCalculator(ICalculator calculator) {
		this.calculator = calculator;
		updateCalculatorResult();
		updateBlindResult();
		updateDivineResult();
	}

	@Override
	public IObservable<ICalculatorResult> calculatorResult() {
		return calculatorResult;
	}

	@Override
	public IObservable<ChunkPrediction> topPrediction() {
		return topPredictionProvider.topPrediction();
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
	public void dispose() {
		disposeHandler.dispose();
		if (calculatorResult.get() != null)
			calculatorResult.get().dispose();
	}
}
