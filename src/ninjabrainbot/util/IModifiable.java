package ninjabrainbot.util;

public interface IModifiable<T> {

	public ISubscribable<T> whenModified();

}
