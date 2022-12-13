package ninjabrainbot.data.datalock;

import ninjabrainbot.event.ObservableField;

public class LockableField<T> extends ObservableField<T> {

	IModificationLock modificationLock;

	public LockableField(IModificationLock modificationLock) {
		super();
		this.modificationLock = modificationLock;
	}

	public LockableField(T value, IModificationLock modificationLock) {
		super(value);
		this.modificationLock = modificationLock;
	}

	@Override
	public void set(T value) {
		assertAllowedModification();
		super.set(value);
	}

	private void assertAllowedModification() {
		assert !modificationLock.isLocked();
	}

}
