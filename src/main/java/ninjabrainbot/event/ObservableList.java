package ninjabrainbot.event;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Consumer;

public class ObservableList<T> implements IObservableList<T> {

	protected ArrayListImplementingReadOnlyList<T> list;

	private final ArrayList<Consumer<IReadOnlyList<T>>> subscribers;

	public ObservableList() {
		subscribers = new ArrayList<>();
		list = new ArrayListImplementingReadOnlyList<>();
	}

	public boolean add(T element) {
		list.add(element);
		notifySubscribers();
		return true;
	}

	public void replace(T oldElement, T newElement) {
		int index = list.indexOf(oldElement);
		list.set(index, newElement);
		notifySubscribers();
	}

	public void remove(T element) {
		list.remove(element);
		notifySubscribers();
	}

	public void clear() {
		list.clear();
		notifySubscribers();
	}

	public void setFromList(IReadOnlyList<T> otherList) {
		list.clear();
		otherList.forEach(list::add);
		notifySubscribers();
	}

	@Override
	public IReadOnlyList<T> get() {
		return list;
	}

	@Override
	public T get(int index) {
		return list.get(index);
	}

	@Override
	public int size() {
		return list.size();
	}

	@Override
	public Subscription subscribe(Consumer<IReadOnlyList<T>> subscriber) {
		subscribers.add(subscriber);
		return new Subscription(this, subscriber);
	}

	@Override
	public void unsubscribe(Consumer<IReadOnlyList<T>> subscriber) {
		subscribers.remove(subscriber);
	}

	private void notifySubscribers() {
		for (Consumer<IReadOnlyList<T>> subscriber : subscribers) {
			subscriber.accept(list);
		}
	}

	@Override
	public Iterator<T> iterator() {
		return list.iterator();
	}
}