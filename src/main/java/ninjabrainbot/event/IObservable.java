package ninjabrainbot.event;

/**
 * Represents a variable that can be observed but not set. Changes to the
 * variable can be subscribed to.
 *
 * @param <T> The type of the observed variable.
 */
public interface IObservable<T> extends ISubscribable<T> {

	public T get();

}
