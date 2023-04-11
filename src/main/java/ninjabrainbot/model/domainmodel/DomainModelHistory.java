package ninjabrainbot.model.domainmodel;

import java.util.ArrayList;
import java.util.List;

import ninjabrainbot.util.Assert;

public class DomainModelHistory {

	private final List<IDataComponent<?>> dataComponents;
	private final int maxCapacity;

	private final List<DomainModelSnapshot> snapshots = new ArrayList<>();
	private boolean isInitialized = false;
	private int currentIndex;
	private int numberOfDataComponents;

	public DomainModelHistory(List<IDataComponent<?>> dataComponents, int maxCapacity) {
		this.dataComponents = dataComponents;
		this.maxCapacity = maxCapacity;
	}

	public void initialize() {
		Assert.isFalse(isInitialized, "The DomainModelHistory has already been initialized.");
		isInitialized = true;
		numberOfDataComponents = dataComponents.size();
		currentIndex = 0;
		snapshots.add(new DomainModelSnapshot(dataComponents));
	}

	public void saveSnapshotIfUniqueFromLastSnapshot() {
		Assert.isTrue(isInitialized, "The DomainModelHistory has not been initialized.");
		Assert.isEqual(numberOfDataComponents, dataComponents.size(), "Number of DataComponents have changed since last snapshot.");
		if (snapshots.get(currentIndex).isEqualToCurrentStateOfDomainModel())
			return;
		saveSnapshotAfterCurrentSnapshot(new DomainModelSnapshot(dataComponents));
	}

	private void saveSnapshotAfterCurrentSnapshot(DomainModelSnapshot domainModelSnapshot) {
		currentIndex++;
		while (snapshots.size() > currentIndex) {
			snapshots.remove(snapshots.size() - 1);
		}

		snapshots.add(domainModelSnapshot);

		if (snapshots.size() > maxCapacity) {
			snapshots.remove(0);
			currentIndex--;
		}
	}

	public DomainModelSnapshot moveToPreviousSnapshotAndGet() {
		currentIndex--;
		return snapshots.get(currentIndex);
	}

	public DomainModelSnapshot moveToNextSnapshotAndGet() {
		currentIndex++;
		return snapshots.get(currentIndex);
	}

	public boolean hasPreviousSnapshot() {
		return currentIndex > 0;
	}

	public boolean hasNextSnapshot() {
		return currentIndex < snapshots.size() - 1;
	}

}
