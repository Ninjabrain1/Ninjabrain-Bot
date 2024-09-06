package ninjabrainbot.model.domainmodel;

import java.util.function.Consumer;

import ninjabrainbot.event.ISubscribable;
import ninjabrainbot.event.ObservableField;
import ninjabrainbot.event.Subscription;
import ninjabrainbot.util.Assert;

/**
 * A data component that cannot be set externally, but is instead inferred from IFundamentalComponents
 * and the EnvironmentState. Can only be modified under the DomainModel write lock.
 */
public class InferredComponent<T> implements IInferredComponent<T> {

	private final IDomainModel domainModel;
	private final ObservableField<T> observableField;
	private final ISubscribable<T> externalEvent;

	public InferredComponent(IDomainModel domainModel) {
		this(domainModel, null);
	}

	public InferredComponent(IDomainModel domainModel, T defaultValue) {
		this.domainModel = domainModel;
		observableField = new ObservableField<>(defaultValue);
		externalEvent = domainModel != null ? domainModel.createExternalEventFor(observableField) : observableField;
		if (domainModel != null)
			domainModel.registerInferredComponent(this);
	}

	public void set(T value) {
		if (domainModel != null)
			domainModel.checkWriteAccess();
		observableField.set(value);
	}

	@Override
	public T get() {
		return observableField.get();
	}

	@Override
	public Subscription subscribeInternal(Consumer<T> subscriber) {
		if (domainModel != null)
			Assert.isFalse(domainModel.isFullyInitialized(), "Attempted to subscribe to internal events after domain model initialization has completed. External subscribers should use IInferredComponent.subscribe().");
		return observableField.subscribe(subscriber);
	}

	@Override
	public Subscription subscribe(Consumer<T> subscriber) {
		if (domainModel != null)
			Assert.isTrue(domainModel.isFullyInitialized(), "Attempted to subscribe to external events before domain model initialization has completed. Internal subscribers should use IInferredComponent.subscribeInternal().");
		return externalEvent.subscribe(subscriber);
	}

}
