package ninjabrainbot.data.domainmodel;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DomainModelTests {

	@Test
	public void undoRedoWorksWithDataComponent() {
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
	public void undoRedoWorksWithListComponent() {
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

}
