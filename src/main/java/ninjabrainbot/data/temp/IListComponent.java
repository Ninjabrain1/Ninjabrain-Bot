package ninjabrainbot.data.temp;

import ninjabrainbot.event.IObservableList;
import ninjabrainbot.event.IReadOnlyList;

/**
 * Represents a list of data, write permissions of ListComponent are automatically handled by the DomainModel.
 * Any modifications to a ListComponent are automatically saved by the DomainModel, for the undo action to work.
 * The generic type T should be immutable to ensure that no modifications to the data go unnoticed by the domain model.
 */
public interface IListComponent<T> extends IDataComponent<IReadOnlyList<T>>, IObservableList<T>, IReadOnlyList<T> {

	boolean add(T t);

	void remove(T t);

	int maxCapacity();

}
