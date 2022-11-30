package ninjabrainbot.data.datalock;

import ninjabrainbot.event.IModifiable;
import ninjabrainbot.event.ModifiableSet;

public class LockableSet<T extends IModifiable<T>> extends ModifiableSet<T> {

	IModificationLock modificationLock;

	public LockableSet(IModificationLock modificationLock) {
		super();
		this.modificationLock = modificationLock;
	}

	@Override
	public boolean add(T t) {
		assertAllowedModification();
		return super.add(t);
	}

	@Override
	public boolean insert(T t, int index) {
		assertAllowedModification();
		return super.insert(t, index);
	}

	@Override
	public void remove(T t) {
		assertAllowedModification();
		super.remove(t);
	}

	private void assertAllowedModification() {
		assert !modificationLock.isLocked();
	}

}
