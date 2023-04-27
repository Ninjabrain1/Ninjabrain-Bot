package ninjabrainbot.model.domainmodel;

public interface IWriteLock {

	void acquireWriteLock();

	void releaseWriteLock();

	Runnable applyWriteLock(Runnable runnable);

}
