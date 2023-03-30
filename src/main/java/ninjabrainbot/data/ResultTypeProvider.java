package ninjabrainbot.data;

import ninjabrainbot.data.alladvancements.IAllAdvancementsDataState;
import ninjabrainbot.data.calculator.ICalculatorResult;
import ninjabrainbot.data.datalock.IModificationLock;
import ninjabrainbot.data.datalock.LockableField;
import ninjabrainbot.data.divine.Fossil;
import ninjabrainbot.data.endereye.IThrow;
import ninjabrainbot.event.DisposeHandler;
import ninjabrainbot.event.IDisposable;
import ninjabrainbot.event.IObservable;
import ninjabrainbot.event.ObservableField;

public class ResultTypeProvider implements IDisposable {

	private final ObservableField<ResultType> resultType;

	private final IAllAdvancementsDataState allAdvancementsDataState;
	private final IObservable<ICalculatorResult> calculatorResult;
	private final IObservable<IThrow> playerPosition;
	private final IObservable<Fossil> fossil;

	private final DisposeHandler disposeHandler = new DisposeHandler();

	public ResultTypeProvider(IDataState dataState, IModificationLock modificationLock) {
		resultType = new LockableField<>(ResultType.NONE, modificationLock);
		allAdvancementsDataState = dataState.allAdvancementDataState();
		calculatorResult = dataState.calculatorResult();
		playerPosition = dataState.playerPosition();
		fossil = dataState.getDivineContext().fossil();

		disposeHandler.add(allAdvancementsDataState.allAdvancementsModeEnabled().subscribe(this::updateResultType));
		disposeHandler.add(calculatorResult.subscribe(this::updateResultType));
		disposeHandler.add(playerPosition.subscribe(this::updateResultType));
		disposeHandler.add(fossil.subscribe(this::updateResultType));
	}

	public IObservable<ResultType> resultType() {
		return resultType;
	}

	private ResultType getExpectedResultType() {
		if (allAdvancementsDataState.allAdvancementsModeEnabled().get())
			return ResultType.ALL_ADVANCEMENTS;

		if (calculatorResult.get() != null && calculatorResult.get().success())
			return ResultType.TRIANGULATION;

		if (calculatorResult.get() != null)
			return ResultType.FAILED;

		if (playerPosition.get() != null)
			return ResultType.BLIND;

		if (fossil.get() != null)
			return ResultType.DIVINE;

		return ResultType.NONE;
	}

	private void updateResultType() {
		resultType.set(getExpectedResultType());
	}

	@Override
	public void dispose() {
		disposeHandler.dispose();
	}
}
