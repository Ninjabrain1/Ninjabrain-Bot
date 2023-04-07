package ninjabrainbot.data.temp;

public interface IDomainModel {

	void registerDataComponent(IDataComponent<?> dataComponent);

	void notifyDataComponentToBeModified();

}
