package ninjabrainbot.event;

import java.util.ArrayList;
import java.util.function.Consumer;

public class ObservableList<T> implements IObservableList<T> {

	protected ArrayListImplementingReadOnlyList<T> list;

	private final ArrayList<Consumer<IReadOnlyList<T>>> subscribers;

	public ObservableList() {
		subscribers = new ArrayList<>();
		list = new ArrayListImplementingReadOnlyList<>();
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

	public boolean add(T element) {
		list.add(element);
		notifySubscribers();
		return true;
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

	public IReadOnlyList<T> get() {
		return list;
	}

	private void notifySubscribers() {
		for (Consumer<IReadOnlyList<T>> subscriber : subscribers) {
			subscriber.accept(list);
		}
	}

}