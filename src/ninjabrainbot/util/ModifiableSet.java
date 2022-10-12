package ninjabrainbot.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class ModifiableSet<T extends IModifiable<?>> extends Modifiable<IModifiableSet<T>> implements IModifiableSet<T>, IDisposable {

	private ArrayList<T> set;
	private HashMap<T, Subscription> subscriptions;

	public ModifiableSet() {
		set = new ArrayList<T>();
		subscriptions = new HashMap<>();
	}

	@Override
	public Iterator<T> iterator() {
		return set.iterator();
	}

	@Override
	public boolean add(T t) {
		if (set.add(t)) {
			subscriptions.put(t, t.whenModified().subscribe(__ -> whenModified.notifySubscribers(this)));
			whenModified.notifySubscribers(this);
			return true;
		}
		return false;
	}

	@Override
	public void remove(T t) {
		if (set.remove(t)) {
			subscriptions.remove(t).cancel();
			whenModified.notifySubscribers(this);
		}
	}

	@Override
	public void clear() {
		if (set.size() == 0)
			return;
		for (Subscription s : subscriptions.values()) {
			s.cancel();
		}
		set.clear();
		subscriptions.clear();
		whenModified.notifySubscribers(this);
	}

	@Override
	public int size() {
		return set.size();
	}

	@Override
	public T get(int index) {
		return set.get(index);
	}
	
	@Override
	public void dispose() {
		assert whenModified.subscriberCount() == 0;
		for (Subscription s : subscriptions.values()) {
			s.cancel();
		}
	}

}
