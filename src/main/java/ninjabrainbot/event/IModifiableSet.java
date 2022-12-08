package ninjabrainbot.event;

import ninjabrainbot.util.ISet;

public interface IModifiableSet<T extends IModifiable<?>> extends IModifiable<IModifiableSet<T>>, ISet<T> {

	public IMultiSubscribable<T> whenElementAtIndexModified();

}
