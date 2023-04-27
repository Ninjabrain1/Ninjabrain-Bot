package ninjabrainbot.model.domainmodel;

import java.util.function.Consumer;

import ninjabrainbot.event.IObservable;
import ninjabrainbot.event.Subscription;

public interface IDomainModelComponent<T> extends IObservable<T> {

	/**
	 * Used by other domain model components to receive events before external components (e.g. UI).
	 * External events might also be suppressed or delayed to not send intermediate states to the UI,
	 * but subscribeInternal is not affected by this and will receive all events.
	 */
	Subscription subscribeInternal(Consumer<T> subscriber);

	/**
	 * Used by other domain model components to receive events before external components (e.g. UI).
	 * External events might also be suppressed or delayed to not send intermediate states to the UI,
	 * but subscribeInternal is not affected by this and will receive all events.
	 */
	default Subscription subscribeInternal(Runnable runnable) {
		return subscribeInternal(__ -> runnable.run());
	}

}
