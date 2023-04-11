package ninjabrainbot.event;

public interface IModifiable<T> {

	ISubscribable<T> whenModified();

}
