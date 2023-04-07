package ninjabrainbot.data.temp;

import java.util.List;

import ninjabrainbot.event.IReadOnlyList;

public interface IListComponent<T> extends IDataComponent<IReadOnlyList<T>> {

	boolean add(T t);

	void remove(T t);

	int maxCapacity();

}
