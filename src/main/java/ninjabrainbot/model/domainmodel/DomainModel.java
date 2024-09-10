package ninjabrainbot.model.domainmodel;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import ninjabrainbot.Main;
import ninjabrainbot.event.DisposeHandler;
import ninjabrainbot.event.IDisposable;
import ninjabrainbot.event.ISubscribable;
import ninjabrainbot.event.ObservableProperty;
import ninjabrainbot.util.Assert;
import ninjabrainbot.util.Logger;

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

		if (fundamentalComponent.uniqueId() == null || fundamentalComponent.uniqueId().isEmpty())
			throw new IllegalArgumentException("Id cannot be empty");

		if (fundamentalComponents.stream().anyMatch(x -> x.uniqueId().equals(fundamentalComponent.uniqueId())))
			throw new IllegalArgumentException("A component with id '" + fundamentalComponent.uniqueId() + "' has already been registered.");

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
	public void deleteHistory() {
		domainModelHistory.deleteHistory();
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
	public void serialize(ObjectOutput objectOutput) throws SerializationException {
		Assert.isTrue(isFullyInitialized);
		HashMap<String, Serializable> serializedFundamentalComponents = new HashMap<>();
		for (IFundamentalComponent<?, ?> fundamentalComponent : fundamentalComponents) {
			String uniqueId = fundamentalComponent.uniqueId();
			Serializable serializable = fundamentalComponent.getAsSerializable();
			serializedFundamentalComponents.put(uniqueId, serializable);
		}
		serializedFundamentalComponents.put("", Main.VERSION);

		try {
			objectOutput.writeObject(serializedFundamentalComponents);
		} catch (IOException e) {
			throw new SerializationException("IOException when deserializing domain model.", e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void deserialize(ObjectInput objectInput) throws SerializationException {
		HashMap<String, Serializable> deserializedFundamentalComponents = null;
		try {
			deserializedFundamentalComponents = (HashMap<String, Serializable>) objectInput.readObject();
		} catch (ClassNotFoundException e) {
			throw new SerializationException("Class not found when deserializing domain model.", e);
		} catch (IOException e) {
			throw new SerializationException("IOException when deserializing domain model.", e);
		} catch (ClassCastException e) {
			throw new SerializationException("ClassCastException when deserializing domain model.", e);
		}

		String deserializedVersion = (String) deserializedFundamentalComponents.getOrDefault("", "UNKNOWN");
		if (!deserializedVersion.equals(Main.VERSION)) {
			Logger.log("Domain model deserialization failed, saved data has version " + deserializedVersion + " but application has version " + Main.VERSION);
			return;
		}
		deserializedFundamentalComponents.remove("");

		if (deserializedFundamentalComponents.size() != fundamentalComponents.size())
			throw new SerializationException("Expected there to be " + (fundamentalComponents.size()) + " deserialized objects, but got " + deserializedFundamentalComponents.size());

		try {
			acquireWriteLock();
			for (IFundamentalComponent<?, ?> fundamentalComponent : fundamentalComponents) {
				String uniqueId = fundamentalComponent.uniqueId();
				if (!deserializedFundamentalComponents.containsKey(uniqueId))
					throw new SerializationException("Key '" + uniqueId + "' is missing in list of deserialized fundamental components.");
				fundamentalComponent.setFromDeserializedObject(deserializedFundamentalComponents.get(uniqueId));
			}
		} finally {
			releaseWriteLock(true);
		}
	}

	@Override
	public void dispose() {
		disposeHandler.dispose();
	}
}
