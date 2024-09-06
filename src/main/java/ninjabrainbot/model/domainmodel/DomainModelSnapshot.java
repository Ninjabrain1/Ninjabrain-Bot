package ninjabrainbot.model.domainmodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DomainModelSnapshot {

	private final List<FundamentalComponentSnapshot<?, ?>> fundamentalComponentSnapshots;

	public DomainModelSnapshot(List<IFundamentalComponent<?, ?>> dataComponentList) {
		fundamentalComponentSnapshots = new ArrayList<>();
		for (IFundamentalComponent<?, ?> fundamentalComponent : dataComponentList) {
			fundamentalComponentSnapshots.add(new FundamentalComponentSnapshot<>(fundamentalComponent));
		}
	}

	public void restoreDomainModelToStateAtSnapshot() {
		fundamentalComponentSnapshots.forEach(FundamentalComponentSnapshot::restoreDataComponentToStateAtSnapshot);
	}

	public boolean isEqualToCurrentStateOfDomainModel() {
		return fundamentalComponentSnapshots.stream().allMatch(FundamentalComponentSnapshot::isEqualToCurrentStateOfDataComponent);
	}
}

class FundamentalComponentSnapshot<T, U extends Serializable> {

	private final IFundamentalComponent<T, U> fundamentalComponent;
	private final T value;

	FundamentalComponentSnapshot(IFundamentalComponent<T, U> fundamentalComponent) {
		this.fundamentalComponent = fundamentalComponent;
		this.value = fundamentalComponent.getAsImmutable();
	}

	void restoreDataComponentToStateAtSnapshot() {
		fundamentalComponent.set(value);
	}

	boolean isEqualToCurrentStateOfDataComponent() {
		return fundamentalComponent.contentEquals(value);
	}

}