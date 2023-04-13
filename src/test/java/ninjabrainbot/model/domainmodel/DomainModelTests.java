package ninjabrainbot.model.domainmodel;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DomainModelTests {

	@Test
	public void undoRedoWithDataComponent() {
		DomainModel domainModel = new DomainModel();
		DataComponent<Integer> integer = new DataComponent<>(domainModel, 0);

		Assertions.assertEquals(0, integer.get());

		domainModel.acquireWriteLock();
		integer.set(1);
		domainModel.releaseWriteLock();
		Assertions.assertEquals(1, integer.get());

		domainModel.undoUnderWriteLock();
		Assertions.assertEquals(0, integer.get());

		domainModel.redoUnderWriteLock();
		Assertions.assertEquals(1, integer.get());

		domainModel.undoUnderWriteLock();
		Assertions.assertEquals(0, integer.get());

		domainModel.acquireWriteLock();
		integer.set(2);
		domainModel.releaseWriteLock();

		domainModel.redoUnderWriteLock();
		Assertions.assertEquals(2, integer.get());

		domainModel.undoUnderWriteLock();
		Assertions.assertEquals(0, integer.get());
	}

	@Test
	public void undoRedoWithListComponent() {
		DomainModel domainModel = new DomainModel();
		ListComponent<Integer> list = new ListComponent<>(domainModel, 10);

		Assertions.assertEquals(0, list.size());

		domainModel.acquireWriteLock();
		list.add(0);
		domainModel.releaseWriteLock();
		Assertions.assertEquals(1, list.size());
		Assertions.assertEquals(0, list.get(0));

		domainModel.acquireWriteLock();
		list.add(1);
		domainModel.releaseWriteLock();
		Assertions.assertEquals(2, list.size());
		Assertions.assertEquals(0, list.get(0));
		Assertions.assertEquals(1, list.get(1));

		domainModel.undoUnderWriteLock();
		Assertions.assertEquals(1, list.size());
		Assertions.assertEquals(0, list.get(0));

		domainModel.undoUnderWriteLock();
		Assertions.assertEquals(0, list.size());

		domainModel.redoUnderWriteLock();
		Assertions.assertEquals(1, list.size());
		Assertions.assertEquals(0, list.get(0));

		domainModel.redoUnderWriteLock();
		Assertions.assertEquals(2, list.size());
		Assertions.assertEquals(0, list.get(0));
		Assertions.assertEquals(1, list.get(1));
	}

	@Test
	public void maxNumberOfUndoSteps() {
		int maxNumberOfUndoSnapshots = 10;

		DomainModel domainModel = new DomainModel();
		DataComponent<Integer> integer = new DataComponent<>(domainModel, 0);

		for (int i = 0; i < maxNumberOfUndoSnapshots; i++) {
			domainModel.acquireWriteLock();
			integer.set(i + 1);
			domainModel.releaseWriteLock();
		}
		Assertions.assertEquals(maxNumberOfUndoSnapshots, integer.get());

		for (int i = 0; i < maxNumberOfUndoSnapshots - 1; i++) {
			domainModel.undoUnderWriteLock();
			Assertions.assertEquals(maxNumberOfUndoSnapshots - i - 1, integer.get());
		}
		for (int i = 0; i < 10; i++) {
			domainModel.undoUnderWriteLock();
			Assertions.assertEquals(1, integer.get());
		}
		for (int i = 0; i < maxNumberOfUndoSnapshots - 1; i++) {
			domainModel.redoUnderWriteLock();
			Assertions.assertEquals(i + 2, integer.get());
		}
		for (int i = 0; i < 10; i++) {
			domainModel.redoUnderWriteLock();
			Assertions.assertEquals(maxNumberOfUndoSnapshots, integer.get());
		}
	}

	@Test
	public void duplicateStatesAreNotSavedForUndo() {
		DomainModel domainModel = new DomainModel();
		DataComponent<Integer> integer = new DataComponent<>(domainModel, 0);

		domainModel.acquireWriteLock();
		integer.set(123);
		domainModel.releaseWriteLock();
		domainModel.acquireWriteLock();
		integer.set(123);
		domainModel.releaseWriteLock();
		Assertions.assertEquals(123, integer.get());

		domainModel.acquireWriteLock();
		domainModel.reset();
		domainModel.releaseWriteLock();
		Assertions.assertEquals(0, integer.get());
		domainModel.acquireWriteLock();
		domainModel.reset();
		domainModel.releaseWriteLock();
		Assertions.assertEquals(0, integer.get());

		domainModel.undoUnderWriteLock();
		Assertions.assertEquals(123, integer.get());

		domainModel.undoUnderWriteLock();
		Assertions.assertEquals(0, integer.get());

		domainModel.redoUnderWriteLock();
		Assertions.assertEquals(123, integer.get());

		domainModel.redoUnderWriteLock();
		Assertions.assertEquals(0, integer.get());
	}

	@Test
	public void noRaceConditionsDuringAttemptedConcurrentModification() {
		DomainModel domainModel = new DomainModel();
		DataComponent<Integer> integer = new DataComponent<>(domainModel, 0);

		Thread thread1 = new Thread(() -> {
			for (int i = 0; i < 10000; i++) {
				domainModel.acquireWriteLock();
				integer.set(integer.get() + 1);
				domainModel.releaseWriteLock();
			}
		});
		Thread thread2 = new Thread(() -> {
			for (int i = 0; i < 10000; i++) {
				domainModel.acquireWriteLock();
				integer.set(integer.get() + 1);
				domainModel.releaseWriteLock();
			}
		});
		thread1.start();
		thread2.start();

		int millis = 0;
		while (thread1.isAlive() || thread2.isAlive()) {
			try {
				Thread.sleep(10);
				millis += 10;
				if (millis > 1000)
					Assertions.fail("Test timed out.");
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
		Assertions.assertEquals(20000, integer.get());
	}

}
