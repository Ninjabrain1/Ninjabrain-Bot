package ninjabrainbot.event;

public class Modifiable<T> implements IModifiable<T> {

	private final ObservableProperty<T> whenModified;

	public Modifiable() {
		whenModified = new ObservableProperty<T>();
	}

	@Override
	public ISubscribable<T> whenModified() {
		return whenModified;
	}

	protected void notifySubscribers(T value) {
		whenModified.notifySubscribers(value);
	}

	protected int subscriberCount() {
		return whenModified.subscriberCount();
	}

}
