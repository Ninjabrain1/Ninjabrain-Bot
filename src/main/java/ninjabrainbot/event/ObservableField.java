package ninjabrainbot.event;

import java.util.ArrayList;
import java.util.function.Consumer;

public class ObservableField<T> implements IObservable<T> {

	private final boolean pushDataToNewSubscribers;

	private T data;

	private final ArrayList<Consumer<T>> subscribers;

	public ObservableField() {
		this(null);
	}

	public ObservableField(T data) {
		this(data, false);
	}

	public ObservableField(T data, boolean pushDataToNewSubscribers) {
		subscribers = new ArrayList<Consumer<T>>();
		this.data = data;
		this.pushDataToNewSubscribers = pushDataToNewSubscribers;
	}

	@Override
	public Subscription subscribe(Consumer<T> subscriber) {
		subscribers.add(subscriber);
		if (pushDataToNewSubscribers) {
			if (data != null)
				subscriber.accept(data);
		}
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
	}

	public T get() {
		return data;
	}

}
