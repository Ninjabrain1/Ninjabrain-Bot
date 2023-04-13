package ninjabrainbot.model.domainmodel;

import java.util.Iterator;
import java.util.function.Consumer;

import ninjabrainbot.event.ArrayListImplementingReadOnlyList;
import ninjabrainbot.event.IReadOnlyList;
import ninjabrainbot.event.ObservableList;
import ninjabrainbot.event.Subscription;
import ninjabrainbot.util.Assert;

/**
 * Represents a list of data, write permissions of ListComponent are automatically handled by the DomainModel.
 * Any modifications to a ListComponent are automatically saved by the DomainModel, for the undo action to work.
 * The generic type T should be immutable to ensure that no modifications to the data go unnoticed by the domain model.
 * If null is passed as the IDomainModel to the constructor, the data in the ListComponent will not be saved
 * for the undo action, and the ListComponent will not be write locked. However, in most cases where saving of
 * the data for undo is not needed, an {@link ObservableList} is more suiting.
 */
public class ListComponent<T> implements IListComponent<T> {

	private final IDomainModel domainModel;
	private final ObservableList<T> observableList;
	private final int maxCapacity;

	public ListComponent(IDomainModel domainModel, int maxCapacity) {
		this.domainModel = domainModel;
		this.maxCapacity = maxCapacity;
		observableList = new ObservableList<>();
		if (domainModel != null)
			domainModel.registerDataComponent(this);
	}

	@Override
	public boolean add(T t) {
		if (domainModel != null)
			domainModel.notifyDataComponentToBeModified();
		if (observableList.get().size() >= maxCapacity)
			return false;
		return observableList.add(t);
	}

	@Override
	public void replace(T oldElement, T newElement) {
		if (domainModel != null)
			domainModel.notifyDataComponentToBeModified();
		observableList.replace(oldElement, newElement);
	}

	@Override
	public void remove(T t) {
		if (domainModel != null)
			domainModel.notifyDataComponentToBeModified();
		observableList.remove(t);
	}

	@Override
	public void set(IReadOnlyList<T> list) {
		if (domainModel != null)
			domainModel.notifyDataComponentToBeModified();
		if (list.size() > maxCapacity)
			throw new IllegalModificationException("Attempting to set list to size greater than maxCapacity.");
		observableList.setFromList(list);
	}

	@Override
	public void reset() {
		if (domainModel != null)
			domainModel.notifyDataComponentToBeModified();
		observableList.clear();
		Assert.isTrue(isReset());
	}

	@Override
	public boolean contentEquals(IReadOnlyList<T> value) {
		if (value == null)
			return false;
		if (value.size() != this.size())
			return false;
		for (int i = 0; i < size(); i++) {
			if (value.get(i) != get(i))
				return false;
		}
		return true;
	}

	@Override
	public boolean isReset() {
		return size() == 0;
	}

	@Override
	public IReadOnlyList<T> get() {
		return observableList.get();
	}

	@Override
	public IReadOnlyList<T> getAsImmutable() {
		return new ArrayListImplementingReadOnlyList<>(observableList.get());
	}

	@Override
	public int maxCapacity() {
		return maxCapacity;
	}

	@Override
	public T get(int index) {
		return observableList.get(index);
	}

	@Override
	public int size() {
		return observableList.size();
	}

	@Override
	public Iterator<T> iterator() {
		return observableList.iterator();
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
