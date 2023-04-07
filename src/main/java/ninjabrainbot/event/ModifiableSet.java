package ninjabrainbot.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class ModifiableSet<T extends IModifiable<T>> extends Modifiable<IModifiableSet<T>> implements IModifiableSet<T>, IDisposable {

	private final ArrayList<T> set;
	private final HashMap<T, Subscription> subscriptions;

	private final IndexedObservableProperty<T> whenElementAtIndexModified;

	public ModifiableSet() {
		set = new ArrayList<>();
		subscriptions = new HashMap<>();
		whenElementAtIndexModified = new IndexedObservableProperty<>();
	}

	@Override
	public IMultiSubscribable<T> whenElementAtIndexModified() {
		return whenElementAtIndexModified;
	}

	public Iterator<T> iterator() {
		return set.iterator();
	}

	public boolean add(T t) {
		if (set.add(t)) {
			subscriptions.put(t, t.whenModified().subscribe(this::onElementModified));
			whenElementAtIndexModified.notifySubscribers(t, size() - 1);
			notifySubscribers(this);
			return true;
		}
		return false;
	}

	public boolean insert(T t, int index) {
		set.add(index, t);
		subscriptions.put(t, t.whenModified().subscribe(this::onElementModified));
		for (int i = index; i < size(); i++) {
			whenElementAtIndexModified.notifySubscribers(set.get(i), i);
		}
		notifySubscribers(this);
		return true;
	}

	public void remove(T t) {
		int index = set.indexOf(t);
		if (set.remove(t)) {
			subscriptions.remove(t).dispose();
			for (int i = index; i < size() + 1; i++) {
				whenElementAtIndexModified.notifySubscribers(i < size() ? set.get(i) : null, i);
			}
			notifySubscribers(this);
		}
	}

	public void clear() {
		int n = set.size();
		if (n == 0)
			return;
		for (Subscription s : subscriptions.values()) {
			s.dispose();
		}
		set.clear();
		subscriptions.clear();
		for (int i = 0; i < n; i++) {
			whenElementAtIndexModified.notifySubscribers(null, i);
		}
		notifySubscribers(this);
	}

	public int size() {
		return set.size();
	}

	public T get(int index) {
		return set.get(index);
	}

	public void setFromList(List<T> list) {
		int n = Math.max(set.size(), list.size());
		if (n == 0)
			return;
		ArrayList<Integer> modifiedIndices = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			T t = list.get(i);
			if (i < set.size() && set.get(i).equals(t))
				continue;
			subscriptions.put(t, t.whenModified().subscribe(this::onElementModified));
			if (i < set.size()) {
				subscriptions.remove(set.get(i)).dispose();
				set.set(i, t);
			} else {
				set.add(i, t);
			}
			modifiedIndices.add(i);
		}
		while (set.size() > list.size()) {
			int removeIndex = set.size() - 1;
			T removed = set.remove(removeIndex);
			subscriptions.remove(removed).dispose();
			modifiedIndices.add(removeIndex);
		}
		for (int i : modifiedIndices) {
			whenElementAtIndexModified.notifySubscribers(i < set.size() ? set.get(i) : null, i);
		}
		if (modifiedIndices.size() != 0)
			notifySubscribers(this);
	}

	public List<T> toList() {
		return new ArrayList<>(set);
	}

	@Override
	public void dispose() {
		assert subscriberCount() == 0;
		for (Subscription s : subscriptions.values()) {
			s.dispose();
		}
	}

	private void onElementModified(T element) {
		int index = set.indexOf(element);
		whenElementAtIndexModified.notifySubscribers(element, index);
		notifySubscribers(this);
	}

}
