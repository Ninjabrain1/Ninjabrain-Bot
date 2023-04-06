package ninjabrainbot.data.calculator.stronghold;

import ninjabrainbot.data.calculator.ICalculatorResult;
import ninjabrainbot.data.datalock.IModificationLock;
import ninjabrainbot.data.datalock.LockableField;
import ninjabrainbot.event.IDisposable;
import ninjabrainbot.event.IObservable;
import ninjabrainbot.event.ObservableField;
import ninjabrainbot.event.Subscription;

public class TopPredictionProvider implements IDisposable {

	private final ObservableField<ChunkPrediction> topPrediction;

	private final Subscription calculatorResultSubscription;

	public TopPredictionProvider(IObservable<ICalculatorResult> calculatorResult, IModificationLock modificationLock) {
		calculatorResultSubscription = calculatorResult.subscribe(this::updateTopPrediction);
		topPrediction = new LockableField<>(modificationLock);
	}

	private void updateTopPrediction(ICalculatorResult calculatorResult) {
		if (calculatorResult == null || !calculatorResult.success()) {
			topPrediction.set(null);
			return;
		}
		topPrediction.set(calculatorResult.getBestPrediction());
	}

	public IObservable<ChunkPrediction> topPrediction() {
		return topPrediction;
	}

	@Override
	public void dispose() {
		calculatorResultSubscription.dispose();
	}
}
