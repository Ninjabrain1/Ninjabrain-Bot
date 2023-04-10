package ninjabrainbot.event;

import java.util.ArrayList;

public class ArrayListImplementingReadOnlyList<T> extends ArrayList<T> implements IReadOnlyList<T> {

	public ArrayListImplementingReadOnlyList() {
	}

	public ArrayListImplementingReadOnlyList(Iterable<? extends T> iterable) {
		iterable.forEach(this::add);
	}
}
