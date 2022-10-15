package ninjabrainbot.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class ModifiableSet<T extends IModifiable<T>> extends Modifiable<IModifiableSet<T>> implements IModifiableSet<T>, IDisposable {

	private ArrayList<T> set;
	private HashMap<T, Subscription> subscriptions;

	private IndexedObservableProperty<T> whenElementAtIndexModified;

	public ModifiableSet() {
		set = new ArrayList<T>();
		subscriptions = new HashMap<>();
		whenElementAtIndexModified = new IndexedObservableProperty<T>();
	}

	@Override
	public IMultiSubscribable<T> whenElementAtIndexModified() {
		return whenElementAtIndexModified;
	}

	@Override
	public Iterator<T> iterator() {
		return set.iterator();
	}

	@Override
	public boolean add(T t) {
		if (set.add(t)) {
			subscriptions.put(t, t.whenModified().subscribe(elem -> onElementModified(elem)));
			whenElementAtIndexModified.notifySubscribers(t, size() - 1);
			whenModified.notifySubscribers(this);
			return true;
		}
		return false;
	}

	@Override
	public boolean insert(T t, int index) {
		set.add(index, t);
		subscriptions.put(t, t.whenModified().subscribe(elem -> onElementModified(elem)));
		for (int i = index; i < size(); i++) {
			whenElementAtIndexModified.notifySubscribers(set.get(i), i);
		}
		whenModified.notifySubscribers(this);
		return true;
	}

	@Override
	public void remove(T t) {
		int index = set.indexOf(t);
		if (set.remove(t)) {
			subscriptions.remove(t).cancel();
			for (int i = index; i < size() + 1; i++) {
				whenElementAtIndexModified.notifySubscribers(i < size() ? set.get(i) : null, i);
			}
			whenModified.notifySubscribers(this);
		}
	}

	@Override
	public void clear() {
		int n = set.size();
		if (n == 0)
			return;
		for (Subscription s : subscriptions.values()) {
			s.cancel();
		}
		set.clear();
		subscriptions.clear();
		for (int i = 0; i < n; i++) {
			whenElementAtIndexModified.notifySubscribers(null, i);
		}
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

	private void onElementModified(T element) {
		int index = set.indexOf(element);
		whenElementAtIndexModified.notifySubscribers(element, index);
		whenModified.notifySubscribers(this);
	}

}
