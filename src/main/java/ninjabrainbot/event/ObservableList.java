package ninjabrainbot.event;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ObservableList<T> implements IObservable<List<T>> {

	protected ArrayList<T> list;

	private ArrayList<Consumer<List<T>>> subscribers;

	public ObservableList() {
		subscribers = new ArrayList<Consumer<List<T>>>();
		list = new ArrayList<T>();
	}

	@Override
	public Subscription subscribe(Consumer<List<T>> subscriber) {
		subscribers.add(subscriber);
		return new Subscription(this, subscriber);
	}

	@Override
	public void unsubscribe(Consumer<List<T>> subscriber) {
		subscribers.remove(subscriber);
	}

	public void add(T element) {
		list.add(element);
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

	public List<T> get() {
		return list;
	}

	private void notifySubscribers() {
		for (Consumer<List<T>> subscriber : subscribers) {
			subscriber.accept(list);
		}
	}

}