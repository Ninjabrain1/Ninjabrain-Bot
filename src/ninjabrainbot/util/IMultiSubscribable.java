package ninjabrainbot.util;

import java.util.function.Consumer;

import javax.swing.SwingUtilities;

/**
 * Represents an inedxed event that can be subscribed to.
 */
public interface IMultiSubscribable<T> extends IUnsubscribable<T> {

	public Subscription subscribe(Consumer<T> subscriber, int index);

	public default Subscription subscribeEDT(Consumer<T> subscriber, int index) {
		return subscribe(t -> SwingUtilities.invokeLater(() -> subscriber.accept(t)), index);
	}

}
