package ninjabrainbot.event;

import java.util.function.Consumer;

public interface IUnsubscribable<T> {

	public void unsubscribe(Consumer<T> subscriber);

}
