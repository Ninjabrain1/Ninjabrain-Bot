package ninjabrainbot.model.domainmodel;

import ninjabrainbot.event.ISubscribable;

/**
 * Keeps track of all DataComponents, to manage write lock to them and monitor changes so that undo works.
 */
public interface IDomainModel extends IWriteLock {

	void registerDataComponent(IDataComponent<?> dataComponent);

	void registerInferredComponent(IInferredComponent<?> inferredComponent);

	void checkWriteAccess();

	void reset();

	void undoUnderWriteLock();

	void redoUnderWriteLock();

	boolean isReset();

	ISubscribable<IDomainModel> whenModified();

}
