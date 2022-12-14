package ninjabrainbot.data.datalock;

import java.util.function.Consumer;

public class ModificationLock implements IModificationLock {

	class Lock implements ILock {

		boolean isUndoAction = false;

		@Override
		public void setUndoAction() {
			isUndoAction = true;
		}

		@Override
		public void close() {
			locked = true;
			whenModificationFinished.accept(isUndoAction);
		}

	}

	private Lock lock = new Lock();
	private boolean locked = true;

	Consumer<Boolean> whenModificationFinished;

	public ModificationLock(Consumer<Boolean> whenModificationFinished) {
		this.whenModificationFinished = whenModificationFinished;
	}

	public ILock acquireWritePermission() {
		locked = false;
		lock.isUndoAction = false;
		return lock;
	}

	@Override
	public boolean isLocked() {
		return locked;
	}

}