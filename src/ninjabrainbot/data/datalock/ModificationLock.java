package ninjabrainbot.data.datalock;

public class ModificationLock implements IModificationLock {

	class Lock implements ILock {

		@Override
		public void close() {
			System.out.println("locked");
			locked = true;
		}

	}

	private Lock lock = new Lock();
	private boolean locked = true;

	public ILock acquireWritePermission() {
		System.out.println("unlocked");
		locked = false;
		return lock;
	}

	@Override
	public boolean isLocked() {
		return locked;
	}

}