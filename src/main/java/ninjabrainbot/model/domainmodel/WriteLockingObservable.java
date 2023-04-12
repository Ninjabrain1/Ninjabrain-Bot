package ninjabrainbot.model.domainmodel;

import java.util.function.Consumer;

import ninjabrainbot.event.IObservable;
import ninjabrainbot.event.ObservableField;
import ninjabrainbot.event.Subscription;

public class WriteLockingObservable<T> implements IObservable<T> {

	private final ObservableField<T> observableField;
	private final IWriteLock writeLock;

	public WriteLockingObservable(IWriteLock writeLock) {
		this.writeLock = writeLock;
		observableField = new ObservableField<>();
	}

	@Override
	public T get() {
		return null;
	}

	@Override
	public Subscription subscribe(Consumer<T> subscriber) {
		return null;
	}

	@Override
	public void unsubscribe(Consumer<T> subscriber) {

	}
}
