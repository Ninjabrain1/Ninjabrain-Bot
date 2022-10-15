package ninjabrainbot.util;

import java.util.ArrayList;
import java.util.function.Consumer;

public class ObservableField<T> implements IObservable<T> {

	private T data;

	private ArrayList<Consumer<T>> subscribers;

	public ObservableField() {
		this(null);
	}

	public ObservableField(T data) {
		subscribers = new ArrayList<Consumer<T>>();
		this.data = data;
	}

	@Override
	public Subscription subscribe(Consumer<T> subscriber) {
		subscribers.add(subscriber);
		return new Subscription(this, subscriber);
	}

	@Override
	public void unsubscribe(Consumer<T> subscriber) {
		subscribers.remove(subscriber);
	}

	public void set(T value) {
		if (value == data || (value != null && value.equals(data)))
			return;
		data = value;
		for (Consumer<T> subscriber : subscribers) {
			subscriber.accept(data);
		}
		System.out.println(value);
	}

	public T get() {
		return data;
	}

}
