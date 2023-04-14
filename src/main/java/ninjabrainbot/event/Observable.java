package ninjabrainbot.event;

import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public final class Observable<T> implements IObservable<T>, IDisposable {

	private final Supplier<T> supplier;
	private final ObservableField<T> observableField;
	private final DisposeHandler disposeHandler = new DisposeHandler();
	private Runnable onNext = this::onNext;

	private Observable(Supplier<T> supplier) {
		this.supplier = supplier;
		observableField = new ObservableField<>(supplier.get());
	}

	public static <T> Observable<T> inferFrom(Supplier<T> supplier) {
		return new Observable<>(supplier);
	}

	public Observable<T> dependsOn(ISubscribable<?>... dependencies) {
		for (ISubscribable<?> dependency : dependencies) {
			disposeHandler.add(dependency.subscribe(() -> onNext.run()));
		}
		return this;
	}

	public Observable<T> dependsOn(IModifiable<?>... dependencies) {
		for (IModifiable<?> dependency : dependencies) {
			disposeHandler.add(dependency.whenModified().subscribe(() -> onNext.run()));
		}
		return this;
	}

	public Observable<T> whenPushingEventsDo(UnaryOperator<Runnable> onNextOperator) {
		onNext = onNextOperator.apply(onNext);
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
	public void unsubscribe(Consumer<T> subscriber) {
		observableField.unsubscribe(subscriber);
	}

	@Override
	public void dispose() {
		disposeHandler.dispose();
	}
}
