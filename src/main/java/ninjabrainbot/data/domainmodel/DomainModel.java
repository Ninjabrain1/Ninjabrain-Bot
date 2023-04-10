package ninjabrainbot.data.domainmodel;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import ninjabrainbot.util.Assert;

/**
 * Keeps track of all DataComponents, to manage write lock to them and monitors changes so that undo works.
 */
public class DomainModel implements IDomainModel {

	private final ReentrantReadWriteLock lock;
	private final ArrayList<IDataComponent<?>> dataComponents;
	private final DomainModelHistory domainModelHistory;

	private boolean isFullyInitialized = false;

	public DomainModel() {
		lock = new ReentrantReadWriteLock();
		dataComponents = new ArrayList<>();
		domainModelHistory = new DomainModelHistory(dataComponents, 10);
	}

	public void registerDataComponent(IDataComponent<?> dataComponent) {
		Assert.isFalse(isFullyInitialized, "New DataComponents cannot be registered in the DomainModel after it has been fully initialized.");
		dataComponents.add(dataComponent);
	}

	public void acquireWriteLock() {
		lock.writeLock().lock();
		if (!isFullyInitialized)
			finishInitialization();
	}

	public void releaseWriteLock() {
		releaseWriteLock(true);
	}

	private void releaseWriteLock(boolean saveSnapshotOfNewState) {
		if (saveSnapshotOfNewState)
			domainModelHistory.saveSnapshotIfUniqueFromLastSnapshot();
		lock.writeLock().unlock();
	}

	public void reset() {
		dataComponents.forEach(IDataComponent::reset);
	}

	public void undoUnderWriteLock() {
		acquireWriteLock();
		if (domainModelHistory.hasPreviousSnapshot())
			domainModelHistory.moveToPreviousSnapshotAndGet().restoreDomainModelToStateAtSnapshot();
		releaseWriteLock(false);
	}

	public void redoUnderWriteLock() {
		acquireWriteLock();
		if (domainModelHistory.hasNextSnapshot())
			domainModelHistory.moveToNextSnapshotAndGet().restoreDomainModelToStateAtSnapshot();
		releaseWriteLock(false);
	}

	public void notifyDataComponentToBeModified() {
		if (!lock.isWriteLocked())
			throw new IllegalModificationException("DataComponents cannot be changed without a write lock, create and execute an Action instead of trying to modify the DataComponent directly.");
		if (!lock.isWriteLockedByCurrentThread())
			throw new IllegalModificationException("Modification was attempted by thread " + Thread.currentThread().getName() + ", while the write lock is held by another thread.");
	}

	private void finishInitialization() {
		domainModelHistory.initialize();
		isFullyInitialized = true;
	}

}
