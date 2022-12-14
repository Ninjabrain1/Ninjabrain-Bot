package ninjabrainbot.data;

import java.util.ArrayList;
import java.util.List;

public class DataStateUndoHistory {

	final int maxCapacity;

	List<DataStateUndoData> undoDataList = new ArrayList<>();
	int currentIndex;

	public DataStateUndoHistory(DataStateUndoData initialUndoData, int maxCapacity) {
		this.maxCapacity = maxCapacity;
		undoDataList.add(initialUndoData);
		currentIndex = 0;
	}

	public void addNewUndoData(DataStateUndoData undoData) {
		currentIndex++;
		while (undoDataList.size() > currentIndex) {
			undoDataList.remove(undoDataList.size() - 1);
		}

		undoDataList.add(undoData);

		if (undoDataList.size() > maxCapacity) {
			undoDataList.remove(0);
			currentIndex--;
		}
	}

	public DataStateUndoData moveToPrevious() {
		if (currentIndex > 0)
			currentIndex--;
		return undoDataList.get(currentIndex);
	}

	public DataStateUndoData moveToNext() {
		if (currentIndex < undoDataList.size() - 1)
			currentIndex++;
		return undoDataList.get(currentIndex);
	}

}
