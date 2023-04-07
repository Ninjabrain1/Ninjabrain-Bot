package ninjabrainbot.data.temp;

/**
 * Represents a piece of data, write permissions of DataComponents are automatically handled by the DomainModel.
 * Any modifications to a DataComponent are automatically saved by the DomainModel, for the undo action to work.
 */
public interface IDataComponent<T> {

	T get();

	void set(T value);

}
