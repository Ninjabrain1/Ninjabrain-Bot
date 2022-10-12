package ninjabrainbot.calculator;

import ninjabrainbot.util.ModifiableSet;

public class ThrowSet extends ModifiableSet<IThrow> implements IThrowSet {

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
