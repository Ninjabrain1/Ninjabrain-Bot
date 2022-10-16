package ninjabrainbot.data;

import ninjabrainbot.event.Modifiable;

public class DataComponent<T> extends Modifiable<T> {
	
	private IModificationLock modificationLock;
	
	public DataComponent(IModificationLock modificationLock) {
		super();
		assert modificationLock != null;
		this.modificationLock = modificationLock;
	}
	
	@Override
	protected void notifySubscribers(T value) {
		assertAllowedModification();
		super.notifySubscribers(value);
	}
	
	private void assertAllowedModification() {
		assert !modificationLock.isLocked();
	}

}
