package ninjabrainbot.util;

import java.util.function.Consumer;

public class Subscription {
	
	private Runnable unsubscribe;
	
	public <T> Subscription(IUnsubscribable<T> subscribable, Consumer<T> subscriber){
		unsubscribe = () -> subscribable.unsubscribe(subscriber);
	}
	
	public void cancel() {
		unsubscribe.run();
		unsubscribe = null;
	}
	
}
