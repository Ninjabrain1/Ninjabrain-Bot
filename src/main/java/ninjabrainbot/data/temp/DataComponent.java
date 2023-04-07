package ninjabrainbot.data.temp;

public class DataComponent<T> implements IDataComponent<T> {

	private final DomainModel domainModel;
	private T value;

	public DataComponent(DomainModel domainModel) {
		this.domainModel = domainModel;
		domainModel.registerDataComponent(this);
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
