package ninjabrainbot.model.domainmodel;

import java.io.ObjectInput;
import java.io.ObjectOutput;

import ninjabrainbot.event.ISubscribable;

/**
 * Keeps track of all DataComponents, to manage write lock to them and monitor changes so that undo works.
 */
public interface IDomainModel extends IWriteLock {

	void registerFundamentalComponent(IFundamentalComponent<?, ?> dataComponent);

	void registerInferredComponent(IInferredComponent<?> inferredComponent);

	<T> ISubscribable<T> createExternalEventFor(ISubscribable<T> subscribable);

	void checkWriteAccess();

	void reset();

	void undoUnderWriteLock();

	void redoUnderWriteLock();

	void deleteHistory();

	boolean isReset();

	boolean isExternalSubscriptionRegistrationAllowed();

	boolean isInternalSubscriptionRegistrationAllowed();

	ISubscribable<IDomainModel> whenModified();

	void serialize(ObjectOutput objectOutput) throws SerializationException;

	void deserialize(ObjectInput objectInput) throws SerializationException;

}
