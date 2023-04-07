package ninjabrainbot.data.temp;

import java.util.function.Consumer;

import ninjabrainbot.event.ObservableField;
import ninjabrainbot.event.Subscription;

/**
 * Represents a piece of data, write permissions of DataComponents are automatically handled by the DomainModel.
 * Any modifications to a DataComponent are automatically saved by the DomainModel, for the undo action to work.
 * The generic type T should be immutable to ensure that no modifications to the data go unnoticed by the domain model.
 * If null is passed as the IDomainModel to the constructor, the data in the DataComponent will not be saved
 * for the undo action, and the DataComponent will not be write locked. However, in most cases where saving of
 * the data for undo is not needed, an {@link ObservableField} is more suiting.
 */
public class DataComponent<T> implements IDataComponent<T> {

	private final IDomainModel domainModel;
	private final ObservableField<T> observableField;
	private final T defaultValue;

	public DataComponent(IDomainModel domainModel) {
		this(domainModel, null);
	}

	public DataComponent(IDomainModel domainModel, T defaultValue) {
		observableField = new ObservableField<>(defaultValue);
		this.domainModel = domainModel;
		this.defaultValue = defaultValue;
		if (domainModel != null)
			domainModel.registerDataComponent(this);
	}

	@Override
	public T get() {
		return observableField.get();
	}

	@Override
	public void set(T value) {
		if (domainModel != null)
			domainModel.notifyDataComponentToBeModified();
		observableField.set(value);
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
