package ninjabrainbot.data.calculator.endereye;

import ninjabrainbot.data.datalock.IModificationLock;
import ninjabrainbot.data.datalock.LockableSet;

public class ThrowSet extends LockableSet<IThrow> implements IThrowSet {

	public ThrowSet(IModificationLock modificationLock) {
		super(modificationLock);
	}

	@Override
	public int maxCapacity() {
		return 10;
	}

	@Override
	public boolean add(IThrow t) {
		if (size() < maxCapacity())
			return super.add(t);
		return false;
	}

	@Override
	public boolean insert(IThrow t, int index) {
		if (size() < maxCapacity())
			return super.insert(t, index);
		return false;
	}

}
