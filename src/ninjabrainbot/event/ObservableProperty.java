package ninjabrainbot.event;

import java.util.ArrayList;
import java.util.function.Consumer;

public class ObservableProperty<T> implements ISubscribable<T> {

	protected ArrayList<Consumer<T>> subscribers;

	public ObservableProperty() {
		subscribers = new ArrayList<Consumer<T>>();
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

	public void notifySubscribers(T changed) {
		for (Consumer<T> subscriber : subscribers) {
			subscriber.accept(changed);
		}
		System.out.println(changed + ", " + changed.getClass());
	}

	public int subscriberCount() {
		return subscribers.size();
	}

}
