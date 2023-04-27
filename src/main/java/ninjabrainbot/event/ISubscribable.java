package ninjabrainbot.event;

import java.util.function.Consumer;

import javax.swing.SwingUtilities;

/**
 * Represents an event that can be subscribed to.
 */
public interface ISubscribable<T> {

	Subscription subscribe(Consumer<T> subscriber);

	/**
	 * Subscribes to this subscribable on the AWT event dispatching thread.
	 */
	default Subscription subscribeEDT(Consumer<T> subscriber) {
		return subscribe(t -> SwingUtilities.invokeLater(() -> subscriber.accept(t)));
	}

	default Subscription subscribe(Runnable runnable) {
		return subscribe(__ -> runnable.run());
	}

	default Subscription subscribeEDT(Runnable runnable) {
		return subscribeEDT(__ -> runnable.run());
	}

}
