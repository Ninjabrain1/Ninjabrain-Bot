package ninjabrainbot.model.domainmodel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import ninjabrainbot.event.DisposeHandler;
import ninjabrainbot.event.IDisposable;
import ninjabrainbot.event.ISubscribable;
import ninjabrainbot.event.ObservableProperty;
import ninjabrainbot.util.Assert;

/**
 * Keeps track of all DataComponents, to manage write lock to them and monitors changes so that undo works.
 */
public class DomainModel implements IDomainModel, IDisposable {

	private final ReentrantReadWriteLock lock;
	private final List<IFundamentalComponent<?, ?>> fundamentalComponents;
	private final List<EventLocker<?>> eventLockers;
	private final DomainModelHistory domainModelHistory;
	private final DisposeHandler disposeHandler = new DisposeHandler();
	private final ObservableProperty<IDomainModel> whenModified = new ObservableProperty<>();

	private boolean isFullyInitialized = false;

	private boolean isModifiedDuringCurrentWriteLock = false;

	public DomainModel() {
		lock = new ReentrantReadWriteLock();
		fundamentalComponents = new ArrayList<>();
		eventLockers = new ArrayList<>();
		domainModelHistory = new DomainModelHistory(fundamentalComponents, 100);
	}

	public void finishInitialization() {
		domainModelHistory.initialize();
		isFullyInitialized = true;
	}

	@Override
	public void registerFundamentalComponent(IFundamentalComponent<?, ?> fundamentalComponent) {
		Assert.isFalse(isFullyInitialized, "New IFundamentalComponents cannot be registered in the DomainModel after it has been fully initialized.");
		fundamentalComponents.add(fundamentalComponent);
		fundamentalComponent.subscribeInternal(__ -> isModifiedDuringCurrentWriteLock = true);
	}

	@Override
	public void registerInferredComponent(IInferredComponent<?> inferredComponent) {
		Assert.isFalse(isFullyInitialized, "New InferredComponents cannot be registered in the DomainModel after it has been fully initialized.");
		inferredComponent.subscribeInternal(__ -> isModifiedDuringCurrentWriteLock = true);
	}

	@Override
	public <T> ISubscribable<T> createExternalEventFor(ISubscribable<T> subscribable) {
		Assert.isFalse(isFullyInitialized, "New external events cannot be created after the DomainModel has been initialized.");
		EventLocker<T> eventLocker = new EventLocker<>(subscribable);
		eventLockers.add(eventLocker);
		return eventLocker;
	}

	@Override
	public void acquireWriteLock() {
		Assert.isTrue(isFullyInitialized, "Attempted to modify DomainModel before it has been fully initialized.");
		lock.writeLock().lock();
		eventLockers.forEach(EventLocker::lock);
	}

	@Override
	public void releaseWriteLock() {
		releaseWriteLock(true);
	}

	private void releaseWriteLock(boolean saveSnapshotOfNewState) {
		try {
			if (saveSnapshotOfNewState && isModifiedDuringCurrentWriteLock)
				domainModelHistory.saveSnapshotIfUniqueFromLastSnapshot();

			eventLockers.forEach(EventLocker::unlockAndReleaseEvents);

			if (isModifiedDuringCurrentWriteLock)
				whenModified.notifySubscribers(this);
			isModifiedDuringCurrentWriteLock = false;
		} finally {
			lock.writeLock().unlock();
		}
	}

	@Override
	public void reset() {
		fundamentalComponents.forEach(IFundamentalComponent::reset);
	}

	@Override
	public void undoUnderWriteLock() {
		acquireWriteLock();
		try {
			if (domainModelHistory.hasPreviousSnapshot())
				domainModelHistory.moveToPreviousSnapshotAndGet().restoreDomainModelToStateAtSnapshot();
		} finally {
			releaseWriteLock(false);
		}
	}

	@Override
	public void redoUnderWriteLock() {
		acquireWriteLock();
		try {
			if (domainModelHistory.hasNextSnapshot())
				domainModelHistory.moveToNextSnapshotAndGet().restoreDomainModelToStateAtSnapshot();
		} finally {
			releaseWriteLock(false);
		}
	}

	@Override
	public boolean isReset() {
		return fundamentalComponents.stream().allMatch(IFundamentalComponent::isReset);
	}

	@Override
	public boolean isExternalSubscriptionRegistrationAllowed() {
		return isFullyInitialized;
	}

	@Override
	public boolean isInternalSubscriptionRegistrationAllowed() {
		return !isFullyInitialized;
	}

	@Override
	public ISubscribable<IDomainModel> whenModified() {
		return whenModified;
	}

	@Override
	public Runnable applyWriteLock(Runnable runnable) {
		return () -> runUnderWriteLock(runnable);
	}

	private void runUnderWriteLock(Runnable runnable) {
		acquireWriteLock();
		try {
			runnable.run();
		} finally {
			releaseWriteLock(false);
		}
	}

	public void checkWriteAccess() {
		if (!lock.isWriteLocked())
			throw new IllegalModificationException("DataComponents cannot be changed without a write lock, create and execute an Action instead of trying to modify the DataComponent directly.");
		if (!lock.isWriteLockedByCurrentThread())
			throw new IllegalModificationException("Modification was attempted by thread " + Thread.currentThread().getName() + ", while the write lock is held by another thread.");
	}

	@Override
	public void dispose() {
		disposeHandler.dispose();
	}
}
