package ninjabrainbot.util;

import java.util.function.Consumer;

public interface IUnsubscribable<T> {

	public void unsubscribe(Consumer<T> subscriber);

}
