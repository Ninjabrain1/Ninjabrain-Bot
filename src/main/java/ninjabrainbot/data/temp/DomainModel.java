package ninjabrainbot.data.temp;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Keeps track of all DataComponents, to manage write lock to them and monitor changes so that undo works.
 */
public class DomainModel implements IWriteLock {

	private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	private final ArrayList<DataComponent<?>> dataComponents = new ArrayList<>();


	public void registerDataComponent(DataComponent<?> dataComponent) {
		dataComponents.add(dataComponent);
	}

	public void acquireWriteLock() {
		lock.writeLock().lock();
	}

	public void releaseWriteLock() {
		lock.writeLock().unlock();
	}

	public void notifyDataComponentToBeModified() {
		if (!lock.isWriteLocked())
			throw new IllegalModificationException("DataComponents cannot be changed without a write lock, create and execute an Action instead of trying to modify the DataComponent directly.");
		if (!lock.isWriteLockedByCurrentThread())
			throw new IllegalModificationException("Modification was attempted by thread " + Thread.currentThread().getName() + ", while the write lock is held by another thread.");
	}

}
