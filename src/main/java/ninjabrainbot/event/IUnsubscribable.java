package ninjabrainbot.event;

import java.util.function.Consumer;

public interface IUnsubscribable<T> {

	void unsubscribe(Consumer<T> subscriber);

}
