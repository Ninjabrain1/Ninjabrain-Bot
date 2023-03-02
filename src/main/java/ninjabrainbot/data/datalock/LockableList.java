package ninjabrainbot.data.datalock;

import ninjabrainbot.event.ObservableList;

public class LockableList<T> extends ObservableList<T> {

	IModificationLock modificationLock;

	public LockableList(IModificationLock modificationLock) {
		super();
		this.modificationLock = modificationLock;
	}

	@Override
	public void add(T element) {
		assertAllowedModification();
		super.add(element);
	}

	@Override
	public void clear() {
		assertAllowedModification();
		super.clear();
	}

	private void assertAllowedModification() {
		assert !modificationLock.isLocked();
	}

}
