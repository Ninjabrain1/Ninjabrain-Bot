package ninjabrainbot.model.domainmodel;

import java.util.function.Consumer;

import ninjabrainbot.event.ObservableField;
import ninjabrainbot.event.Subscription;

/**
 * A data component that cannot be set externally, but is instead inferred from DataComponents
 * and the EnvironmentState. Can only be modified under the DomainModel write lock.
 */
public class InferredComponent<T> implements IInferredComponent<T> {

	private final IDomainModel domainModel;
	private final ObservableField<T> observableField;

	public InferredComponent(IDomainModel domainModel) {
		this(domainModel, null);
	}

	public InferredComponent(IDomainModel domainModel, T defaultValue) {
		observableField = new ObservableField<>(defaultValue);
		this.domainModel = domainModel;
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
	public Subscription subscribe(Consumer<T> subscriber) {
		return observableField.subscribe(subscriber);
	}

	@Override
	public void unsubscribe(Consumer<T> subscriber) {
		observableField.unsubscribe(subscriber);
	}

}
