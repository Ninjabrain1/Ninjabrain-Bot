package ninjabrainbot.data.calculator;

import ninjabrainbot.data.calculator.blind.BlindPosition;
import ninjabrainbot.data.calculator.blind.BlindResult;
import ninjabrainbot.data.calculator.divine.DivineResult;
import ninjabrainbot.data.calculator.divine.IDivineContext;
import ninjabrainbot.data.calculator.endereye.IThrow;
import ninjabrainbot.data.calculator.stronghold.ChunkPrediction;
import ninjabrainbot.data.calculator.stronghold.TopPredictionProvider;
import ninjabrainbot.data.datalock.IModificationLock;
import ninjabrainbot.data.datalock.LockableField;
import ninjabrainbot.event.DisposeHandler;
import ninjabrainbot.event.IDisposable;
import ninjabrainbot.event.IObservable;
import ninjabrainbot.event.IObservableList;
import ninjabrainbot.event.ObservableField;

public class CalculatorManager implements ICalculatorManager, IDisposable {

	private ICalculator calculator;

	private final IObservableList<IThrow> throwSet;
	private final IObservable<IThrow> playerPosition;
	private final IDivineContext divineContext;

	private final ObservableField<ICalculatorResult> calculatorResult;
	private final ObservableField<BlindResult> blindResult;
	private final ObservableField<DivineResult> divineResult;

	private final TopPredictionProvider topPredictionProvider;

	private final DisposeHandler disposeHandler = new DisposeHandler();

	public CalculatorManager(ICalculator calculator, IObservableList<IThrow> throwSet, IObservable<IThrow> playerPosition, IDivineContext divineContext) {
		this.calculator = calculator;
		this.throwSet = throwSet;
		this.playerPosition = playerPosition;
		this.divineContext = divineContext;

		calculatorResult = new ObservableField<>();
		blindResult = new ObservableField<>();
		divineResult = new ObservableField<>();

		disposeHandler.add(throwSet.subscribe(this::onThrowSetModified));
		disposeHandler.add(playerPosition.subscribe(this::onPlayerPositionChanged));
		disposeHandler.add(divineContext.fossil().subscribe(this::onFossilChanged));
		topPredictionProvider = disposeHandler.add(new TopPredictionProvider(calculatorResult));
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
		if (throwSet.get().size() > 0 || playerPosition.get() == null || !playerPosition.get().isNether()) {
			blindResult.set(null);
			return;
		}
		blindResult.set(calculator.blind(new BlindPosition(playerPosition.get()), divineContext));
	}

	private void updateDivineResult() {
		if (throwSet.get().size() > 0 || (playerPosition.get() != null && playerPosition.get().isNether())) {
			divineResult.set(null);
			return;
		}
		divineResult.set(calculator.divine(divineContext));
	}

	public void setCalculator(ICalculator calculator) {
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
