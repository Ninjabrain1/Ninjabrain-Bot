package ninjabrainbot.util;

import java.util.ArrayList;
import java.util.function.Consumer;

public class IndexedObservableProperty<T> implements IMultiSubscribable<T> {

	protected ArrayList<Pair<Consumer<T>, Integer>> subscribers;

	public IndexedObservableProperty() {
		subscribers = new ArrayList<Pair<Consumer<T>, Integer>>();
	}

	@Override
	public Subscription subscribe(Consumer<T> subscriber, int index) {
		subscribers.add(new Pair<Consumer<T>, Integer>(subscriber, index));
		return new Subscription(this, subscriber);
	}

	@Override
	public void unsubscribe(Consumer<T> subscriber) {
		subscribers.removeIf(pair -> pair.fst == subscriber);
	}

	public void notifySubscribers(T changed, int index) {
		for (Pair<Consumer<T>, Integer> subscriber : subscribers) {
			if (subscriber.snd.intValue() == index)
				subscriber.fst.accept(changed);
		}
		System.out.println(changed);
	}
	
	public int subscriberCount() {
		return subscribers.size();
	}

}