package ninjabrainbot.util;

import java.util.function.Consumer;

public class Unmodifiable<T> implements IModifiable<T> {

	protected ISubscribable<T> whenModified;

	public Unmodifiable() {
		whenModified = new ISubscribable<T>() {
			@Override
			public Subscription subscribe(Consumer<T> subscriber) {
				return null;
			}

			@Override
			public void unsubscribe(Consumer<T> subscriber) {
			}
		};
	}

	@Override
	public ISubscribable<T> whenModified() {
		return whenModified;
	}

}
