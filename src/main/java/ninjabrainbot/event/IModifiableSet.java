package ninjabrainbot.event;

public interface IModifiableSet<T extends IModifiable<?>> extends IModifiable<IModifiableSet<T>>, IReadOnlyList<T> {

	IMultiSubscribable<T> whenElementAtIndexModified();

}
