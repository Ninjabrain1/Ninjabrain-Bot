package ninjabrainbot.util;

import java.util.function.Consumer;

/**
 * Represents an event that can be subscribed to.
 */
public interface ISubscribable<T> extends IUnsubscribable<T> {
	
	public Subscription subscribe(Consumer<T> subscriber);
	
}
