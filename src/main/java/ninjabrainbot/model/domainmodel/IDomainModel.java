package ninjabrainbot.model.domainmodel;

import ninjabrainbot.event.IObservable;

/**
 * Keeps track of all DataComponents, to manage write lock to them and monitor changes so that undo works.
 */
public interface IDomainModel extends IWriteLock {

	void registerDataComponent(IDataComponent<?> dataComponent);

	void notifyDataComponentToBeModified();

	void reset();

	void undoUnderWriteLock();

	void redoUnderWriteLock();

}
