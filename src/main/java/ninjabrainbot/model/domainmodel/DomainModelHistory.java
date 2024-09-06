package ninjabrainbot.model.domainmodel;

import java.util.ArrayList;
import java.util.List;

import ninjabrainbot.util.Assert;

public class DomainModelHistory {

	private final List<IFundamentalComponent<?, ?>> fundamentalComponents;
	private final int maxCapacity;

	private final List<DomainModelSnapshot> snapshots = new ArrayList<>();
	private boolean isInitialized = false;
	private int currentIndex;
	private int numberOfDataComponents;

	public DomainModelHistory(List<IFundamentalComponent<?, ?>> fundamentalComponents, int maxCapacity) {
		this.fundamentalComponents = fundamentalComponents;
		this.maxCapacity = maxCapacity;
	}

	public void initialize() {
		Assert.isFalse(isInitialized, "The DomainModelHistory has already been initialized.");
		isInitialized = true;
		numberOfDataComponents = fundamentalComponents.size();
		currentIndex = 0;
		snapshots.add(new DomainModelSnapshot(fundamentalComponents));
	}

	public void saveSnapshotIfUniqueFromLastSnapshot() {
		Assert.isTrue(isInitialized, "The DomainModelHistory has not been initialized.");
		Assert.isEqual(numberOfDataComponents, fundamentalComponents.size(), "Number of DataComponents have changed since last snapshot.");
		if (snapshots.get(currentIndex).isEqualToCurrentStateOfDomainModel())
			return;
		saveSnapshotAfterCurrentSnapshot(new DomainModelSnapshot(fundamentalComponents));
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
