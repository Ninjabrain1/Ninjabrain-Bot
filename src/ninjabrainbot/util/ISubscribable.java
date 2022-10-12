package ninjabrainbot.util;

import java.util.function.Consumer;

/**
 * Represents an event that can be subscribed to.
 */
public interface ISubscribable<T> {
	
	public Subscription subscribe(Consumer<T> subscriber);

	public void unsubscribe(Consumer<T> subscriber);
	
}
