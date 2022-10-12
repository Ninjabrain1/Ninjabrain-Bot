package ninjabrainbot.util;

public interface IModifiableSet<T extends IModifiable<?>> extends IModifiable<IModifiableSet<T>>, ISet<T> {
	
	public IMultiSubscribable<T> whenElementAtIndexModified();
	
}
