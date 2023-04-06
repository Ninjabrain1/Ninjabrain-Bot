package ninjabrainbot.data.temp;

/**
 * Represents a piece of data, write permissions of DataComponents are automatically handled by the DomainModel.
 * Any modifications to a DataComponent are automatically saved by the DomainModel, for the undo action to work.
 */
public class DataComponent<T> implements IDataComponent<T> {

	private final DomainModel domainModel;
	private T value;

	public DataComponent(DomainModel domainModel) {
		this.domainModel = domainModel;
	}

	@Override
	public T get() {
		return value;
	}

	@Override
	public void set(T value) {
		domainModel.notifyDataComponentToBeModified();
		this.value = value;
	}

}
