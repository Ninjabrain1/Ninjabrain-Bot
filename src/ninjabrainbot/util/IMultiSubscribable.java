package ninjabrainbot.util;

import java.util.function.Consumer;

/**
 * Represents an inedxed event that can be subscribed to.
 */
public interface IMultiSubscribable<T> extends IUnsubscribable<T> {
	
	public Subscription subscribe(Consumer<T> subscriber, int index);
	
}
