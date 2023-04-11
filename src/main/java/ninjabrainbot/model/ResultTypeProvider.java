package ninjabrainbot.model;

import ninjabrainbot.model.datastate.ICalculatorResult;
import ninjabrainbot.model.datastate.alladvancements.IAllAdvancementsDataState;
import ninjabrainbot.model.datastate.common.IPlayerPosition;
import ninjabrainbot.model.datastate.divine.Fossil;
import ninjabrainbot.event.DisposeHandler;
import ninjabrainbot.event.IDisposable;
import ninjabrainbot.event.IObservable;
import ninjabrainbot.event.ObservableField;

public class ResultTypeProvider implements IDisposable {

	private final ObservableField<ResultType> resultType;

	private final IAllAdvancementsDataState allAdvancementsDataState;
	private final IObservable<ICalculatorResult> calculatorResult;
	private final IObservable<IPlayerPosition> playerPosition;
	private final IObservable<Fossil> fossil;

	private final DisposeHandler disposeHandler = new DisposeHandler();

	public ResultTypeProvider(IDataState dataState) {
		resultType = new ObservableField<>(ResultType.NONE);
		allAdvancementsDataState = dataState.allAdvancementsDataState();
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

		if (playerPosition.get() != null && playerPosition.get().isInNether())
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
