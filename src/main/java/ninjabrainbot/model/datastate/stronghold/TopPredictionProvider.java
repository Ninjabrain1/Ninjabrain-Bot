package ninjabrainbot.model.datastate.stronghold;

import ninjabrainbot.event.IDisposable;
import ninjabrainbot.event.IObservable;
import ninjabrainbot.event.Subscription;
import ninjabrainbot.model.datastate.calculator.ICalculatorResult;
import ninjabrainbot.model.domainmodel.IDomainModel;
import ninjabrainbot.model.domainmodel.InferredComponent;

public class TopPredictionProvider implements IDisposable {

	private final InferredComponent<ChunkPrediction> topPrediction;

	private final Subscription calculatorResultSubscription;

	public TopPredictionProvider(IDomainModel domainModel, IObservable<ICalculatorResult> calculatorResult) {
		calculatorResultSubscription = calculatorResult.subscribe(this::updateTopPrediction);
		topPrediction = new InferredComponent<>(domainModel);
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
