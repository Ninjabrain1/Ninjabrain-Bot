package ninjabrainbot.event;

import java.util.function.Consumer;
import java.util.function.Supplier;

public final class Observable<T> implements IObservable<T>, IDisposable {

	private final Supplier<T> supplier;
	private final ObservableField<T> observableField;
	private final DisposeHandler disposeHandler = new DisposeHandler();

	private Observable(Supplier<T> supplier) {
		this.supplier = supplier;
		observableField = new ObservableField<>(supplier.get());
	}

	public static <T> Observable<T> inferFrom(Supplier<T> supplier) {
		return new Observable<>(supplier);
	}

	public Observable<T> dependsOn(ISubscribable<?>... dependencies) {
		for (ISubscribable<?> dependency : dependencies) {
			disposeHandler.add(dependency.subscribe(this::onNext));
		}
		return this;
	}

	public Observable<T> dependsOn(IModifiable<?>... dependencies) {
		for (IModifiable<?> dependency : dependencies) {
			disposeHandler.add(dependency.whenModified().subscribe(this::onNext));
		}
		return this;
	}

	private void onNext() {
		observableField.set(supplier.get());
	}

	@Override
	public T get() {
		return observableField.get();
	}

	@Override
	public Subscription subscribe(Consumer<T> subscriber) {
		return observableField.subscribe(subscriber);
	}

	@Override
	public void dispose() {
		disposeHandler.dispose();
	}
}
