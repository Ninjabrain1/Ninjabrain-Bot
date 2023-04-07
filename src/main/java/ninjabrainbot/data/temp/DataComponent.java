package ninjabrainbot.data.temp;

import java.util.function.Consumer;

import ninjabrainbot.event.IObservable;
import ninjabrainbot.event.ObservableField;
import ninjabrainbot.event.Subscription;

public class DataComponent<T> implements IObservable<T>, IDataComponent<T> {

	private final IDomainModel domainModel;
	private ObservableField<T> observableField;

	public DataComponent(IDomainModel domainModel) {
		this.domainModel = domainModel;
		domainModel.registerDataComponent(this);
	}

	@Override
	public T get() {
		return observableField.get();
	}

	@Override
	public void set(T value) {
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
