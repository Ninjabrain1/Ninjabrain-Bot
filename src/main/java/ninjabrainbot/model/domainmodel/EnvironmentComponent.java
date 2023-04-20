package ninjabrainbot.model.domainmodel;

import java.util.function.Consumer;

import ninjabrainbot.event.DisposeHandler;
import ninjabrainbot.event.IObservable;
import ninjabrainbot.event.ISubscribable;
import ninjabrainbot.event.ObservableField;
import ninjabrainbot.event.Subscription;
import ninjabrainbot.util.Assert;

/**
 * A data component that represents the state of an external variable that cannot be controlled,
 * such as the Minecraft world. EnvironmentComponents are not controlled by domain model reset
 * or undo actions. Can only be modified under the DomainModel write lock.
 */
public class EnvironmentComponent<T> implements IEnvironmentComponent<T> {

	private final IDomainModel domainModel;
	private final ObservableField<T> observableField;
	private final ISubscribable<T> externalEvent;

	public EnvironmentComponent(IDomainModel domainModel) {
		this(domainModel, null);
	}

	public EnvironmentComponent(IDomainModel domainModel, T initialValue) {
		this.domainModel = domainModel;
		this.observableField = new ObservableField<>(initialValue);
		externalEvent = domainModel != null ? domainModel.createExternalEventFor(observableField) : observableField;
	}

	public void set(T value) {
		if (domainModel != null)
			domainModel.acquireWriteLock();
		observableField.set(value);
		if (domainModel != null)
			domainModel.releaseWriteLock();
	}

	@Override
	public T get() {
		return observableField.get();
	}

	@Override
	public Subscription subscribeInternal(Consumer<T> subscriber) {
		if (domainModel != null)
			Assert.isFalse(domainModel.isFullyInitialized(), "Attempted to subscribe to internal events after domain model initialization has completed. External subscribers should use IDataComponent.subscribe().");
		return observableField.subscribe(subscriber);
	}

	@Override
	public Subscription subscribe(Consumer<T> subscriber) {
		if (domainModel != null)
			Assert.isTrue(domainModel.isFullyInitialized(), "Attempted to subscribe to external events before domain model initialization has completed. Internal subscribers should use IDataComponent.subscribeInternal().");
		return externalEvent.subscribe(subscriber);
	}

	public static <T> EnvironmentComponent<T> of(IDomainModel domainModel, IObservable<T> observable, DisposeHandler disposeHandler) {
		var environmentComponent = new EnvironmentComponent<>(domainModel, observable.get());
		disposeHandler.add(observable.subscribe(environmentComponent::set));
		return environmentComponent;
	}
}
