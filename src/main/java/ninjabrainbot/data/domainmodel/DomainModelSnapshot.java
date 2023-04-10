package ninjabrainbot.data.domainmodel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DomainModelSnapshot {

	private final List<DataComponentSnapshot<?>> dataComponentSnapshots;

	public DomainModelSnapshot(List<IDataComponent<?>> dataComponentList) {
		dataComponentSnapshots = new ArrayList<>();
		for (IDataComponent<?> dataComponent : dataComponentList) {
			dataComponentSnapshots.add(new DataComponentSnapshot<>(dataComponent));
		}
	}

	public void restoreDomainModelToStateAtSnapshot() {
		dataComponentSnapshots.forEach(DataComponentSnapshot::restoreDataComponentToStateAtSnapshot);
	}

	public boolean isEqualToCurrentStateOfDomainModel() {
		return dataComponentSnapshots.stream().allMatch(DataComponentSnapshot::isEqualToCurrentStateOfDataComponent);
	}
}

class DataComponentSnapshot<T> {

	private final IDataComponent<T> dataComponent;
	private final T value;

	DataComponentSnapshot(IDataComponent<T> dataComponent) {
		this.dataComponent = dataComponent;
		this.value = dataComponent.getAsImmutable();
	}

	void restoreDataComponentToStateAtSnapshot() {
		dataComponent.set(value);
	}

	boolean isEqualToCurrentStateOfDataComponent() {
		return dataComponent.contentEquals(value);
	}

}