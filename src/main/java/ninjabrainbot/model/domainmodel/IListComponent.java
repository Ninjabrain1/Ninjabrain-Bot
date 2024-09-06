package ninjabrainbot.model.domainmodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ninjabrainbot.event.IObservableList;
import ninjabrainbot.event.IReadOnlyList;

/**
 * Represents a list of data, write permissions of ListComponent are automatically handled by the DomainModel.
 * Any modifications to a ListComponent are automatically saved by the DomainModel, for the undo action to work.
 * The generic type T should be immutable to ensure that no modifications to the data go unnoticed by the domain model.
 */
public interface IListComponent<T extends Serializable> extends IFundamentalComponent<IReadOnlyList<T>, ArrayList<T>>, IObservableList<T>, IReadOnlyList<T> {

	/**
	 * Adds the element to the end of the list.
	 */
	boolean add(T element);

	/**
	 * Replaces the old element in the list with the new element.
	 */
	void replace(T oldElement, T newElement);

	/**
	 * Removes the element from the list.
	 */
	void remove(T element);

	/**
	 * Returns the maximum amount of elements the list can contain.
	 */
	int maxCapacity();

}
