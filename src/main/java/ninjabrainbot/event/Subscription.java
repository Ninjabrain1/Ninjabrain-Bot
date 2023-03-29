package ninjabrainbot.event;

import java.util.function.Consumer;

public class Subscription implements IDisposable {

	private Runnable unsubscribe;

	public <T> Subscription(IUnsubscribable<T> subscribable, Consumer<T> subscriber) {
		unsubscribe = () -> subscribable.unsubscribe(subscriber);
	}

	@Override
	public void dispose() {
		unsubscribe.run();
		unsubscribe = null;
	}

}
