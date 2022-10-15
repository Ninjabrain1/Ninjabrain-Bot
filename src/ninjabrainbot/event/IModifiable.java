package ninjabrainbot.event;

public interface IModifiable<T> {

	public ISubscribable<T> whenModified();

}
