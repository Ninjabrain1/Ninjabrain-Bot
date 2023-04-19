package ninjabrainbot.model.domainmodel;

import java.util.function.Consumer;

import ninjabrainbot.event.IObservable;
import ninjabrainbot.event.ObservableField;
import ninjabrainbot.event.Subscription;

public class DomainModelObservableField<T> implements IObservable<T> {

	private final IDomainModel domainModel;
	private final ObservableField<T> observableField;

	public DomainModelObservableField(IDomainModel domainModel) {
		this(domainModel, null);
	}

	public DomainModelObservableField(IDomainModel domainModel, T defaultValue) {
		this.domainModel = domainModel;
		this.observableField = new ObservableField<>(defaultValue);
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
	public Subscription subscribe(Consumer<T> subscriber) {
		return observableField.subscribe(subscriber);
	}

}
