package ninjabrainbot.util;

public interface IModifiableSet<T extends IModifiable<?>> extends IModifiable<IModifiableSet<T>>, ISet<T> {
	
	public ISubscribable<T> whenElementAtIndexModified(int index);
	
}
