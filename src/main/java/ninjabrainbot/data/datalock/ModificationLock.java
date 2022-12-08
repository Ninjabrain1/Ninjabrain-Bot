package ninjabrainbot.data.datalock;

public class ModificationLock implements IModificationLock {

	class Lock implements ILock {

		@Override
		public void close() {
			locked = true;
			whenModificationFinished.run();
		}

	}

	private Lock lock = new Lock();
	private boolean locked = true;

	Runnable whenModificationFinished;

	public ModificationLock(Runnable whenModificationFinished) {
		this.whenModificationFinished = whenModificationFinished;
	}

	public ILock acquireWritePermission() {
		locked = false;
		return lock;
	}

	@Override
	public boolean isLocked() {
		return locked;
	}

}