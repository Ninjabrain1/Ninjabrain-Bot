package ninjabrainbot.data.temp;

import java.util.function.Consumer;

import ninjabrainbot.event.IObservableList;
import ninjabrainbot.event.IReadOnlyList;
import ninjabrainbot.event.ObservableList;
import ninjabrainbot.event.Subscription;

public class ListComponent<T> implements IObservableList<T>, IListComponent<T> {

	private final DomainModel domainModel;
	private final ObservableList<T> observableList;
	private final int maxCapacity;

	public ListComponent(DomainModel domainModel, int maxCapacity) {
		this.domainModel = domainModel;
		this.maxCapacity = maxCapacity;
		observableList = new ObservableList<>();
		domainModel.registerDataComponent(this);
	}

	@Override
	public boolean add(T t) {
		domainModel.notifyDataComponentToBeModified();
		if (observableList.get().size() >= maxCapacity)
			return false;
		return observableList.add(t);
	}

	@Override
	public void remove(T t) {
		domainModel.notifyDataComponentToBeModified();
		observableList.remove(t);
	}

	@Override
	public void set(IReadOnlyList<T> list) {
		domainModel.notifyDataComponentToBeModified();
		if (list.size() > maxCapacity)
			throw new IllegalModificationException("Attempting to set list to size greater than maxCapacity.");
		observableList.setFromList(list);
	}

	@Override
	public IReadOnlyList<T> get() {
		return observableList.get();
	}

	@Override
	public int maxCapacity() {
		return maxCapacity;
	}

	@Override
	public Subscription subscribe(Consumer<IReadOnlyList<T>> subscriber) {
		return observableList.subscribe(subscriber);
	}

	@Override
	public void unsubscribe(Consumer<IReadOnlyList<T>> subscriber) {
		observableList.unsubscribe(subscriber);
	}
}
