package ninjabrainbot.event;

import java.util.function.Consumer;

import javax.swing.SwingUtilities;

/**
 * Represents an event that can be subscribed to.
 */
public interface ISubscribable<T> extends IUnsubscribable<T> {

	Subscription subscribe(Consumer<T> subscriber);

	default Subscription subscribeEDT(Consumer<T> subscriber) {
		return subscribe(t -> SwingUtilities.invokeLater(() -> subscriber.accept(t)));
	}

}
