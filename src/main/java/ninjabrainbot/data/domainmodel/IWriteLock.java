package ninjabrainbot.data.domainmodel;

public interface IWriteLock {

	void acquireWriteLock();

	void releaseWriteLock();

}
