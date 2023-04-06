package ninjabrainbot.event;

import java.util.List;

import ninjabrainbot.data.datalock.AlwaysUnlocked;
import ninjabrainbot.data.datalock.IModificationLock;
import ninjabrainbot.data.calculator.endereye.IThrow;
import ninjabrainbot.data.calculator.endereye.Throw;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ModifiableSetTests {

	IModificationLock lock;
	IModifiableSet<IThrow> set;

	@BeforeEach
	public void setUp() {
		lock = new AlwaysUnlocked();
		set = new ModifiableSet<IThrow>();

		set.add(new Throw(313, 80, 277, 90, -31, false, lock));
		set.add(new Throw(4, 80, 2660, 30, -31, false, lock));
		set.add(new Throw(670, 80, 273, 20, -31, false, lock));
		set.add(new Throw(1200, 80, 243, -90, -31, false, lock));
	}

	@Test
	public void setFromList_shouldNotRaiseChangeEvent() {
		List<IThrow> list = set.toList();

		ObservableField<Boolean> whenModifiedWasRaised = new ObservableField<Boolean>(false);

		set.whenModified().subscribe(__ -> whenModifiedWasRaised.set(true));
		set.setFromList(list);
		Assertions.assertFalse(whenModifiedWasRaised.get());

		set.whenElementAtIndexModified().subscribe(__ -> whenModifiedWasRaised.set(true), 0);
		set.setFromList(list);
		Assertions.assertFalse(whenModifiedWasRaised.get());

		set.whenElementAtIndexModified().subscribe(__ -> whenModifiedWasRaised.set(true), 4);
		set.setFromList(list);
		Assertions.assertFalse(whenModifiedWasRaised.get());
	}

	@Test
	public void setFromList_shouldRaiseChangeEvent() {
		// Arrange
		List<IThrow> list = set.toList();
		list.remove(2);

		ObservableField<Boolean> whenModifiedWasRaised = new ObservableField<Boolean>(false);
		ObservableField<Boolean> whenModifiedWasRaised_idx0 = new ObservableField<Boolean>(false);
		ObservableField<Boolean> whenModifiedWasRaised_idx1 = new ObservableField<Boolean>(false);
		ObservableField<Boolean> whenModifiedWasRaised_idx2 = new ObservableField<Boolean>(false);
		ObservableField<Boolean> whenModifiedWasRaised_idx3 = new ObservableField<Boolean>(false);

		set.whenModified().subscribe(__ -> whenModifiedWasRaised.set(true));
		set.whenElementAtIndexModified().subscribe(__ -> whenModifiedWasRaised_idx0.set(true), 0);
		set.whenElementAtIndexModified().subscribe(__ -> whenModifiedWasRaised_idx1.set(true), 1);
		set.whenElementAtIndexModified().subscribe(__ -> whenModifiedWasRaised_idx2.set(true), 2);
		set.whenElementAtIndexModified().subscribe(__ -> whenModifiedWasRaised_idx3.set(true), 3);

		// Act
		set.setFromList(list);

		// Assert
		Assertions.assertTrue(whenModifiedWasRaised.get());
		Assertions.assertFalse(whenModifiedWasRaised_idx0.get());
		Assertions.assertFalse(whenModifiedWasRaised_idx1.get());
		Assertions.assertTrue(whenModifiedWasRaised_idx2.get());
		Assertions.assertTrue(whenModifiedWasRaised_idx3.get());
	}

	@Test
	public void add_shouldRaiseChangeEvent() {
		// Arrange
		ObservableField<Boolean> whenModifiedWasRaised = new ObservableField<Boolean>(false);
		ObservableField<Boolean> whenModifiedWasRaised_idx0 = new ObservableField<Boolean>(false);
		ObservableField<Boolean> whenModifiedWasRaised_idx1 = new ObservableField<Boolean>(false);
		ObservableField<Boolean> whenModifiedWasRaised_idx2 = new ObservableField<Boolean>(false);
		ObservableField<Boolean> whenModifiedWasRaised_idx3 = new ObservableField<Boolean>(false);
		ObservableField<Boolean> whenModifiedWasRaised_idx4 = new ObservableField<Boolean>(false);
		ObservableField<Boolean> whenModifiedWasRaised_idx5 = new ObservableField<Boolean>(false);

		set.whenModified().subscribe(__ -> whenModifiedWasRaised.set(true));
		set.whenElementAtIndexModified().subscribe(__ -> whenModifiedWasRaised_idx0.set(true), 0);
		set.whenElementAtIndexModified().subscribe(__ -> whenModifiedWasRaised_idx1.set(true), 1);
		set.whenElementAtIndexModified().subscribe(__ -> whenModifiedWasRaised_idx2.set(true), 2);
		set.whenElementAtIndexModified().subscribe(__ -> whenModifiedWasRaised_idx3.set(true), 3);
		set.whenElementAtIndexModified().subscribe(__ -> whenModifiedWasRaised_idx4.set(true), 4);
		set.whenElementAtIndexModified().subscribe(__ -> whenModifiedWasRaised_idx5.set(true), 5);

		// Act
		set.add(new Throw(670, 80, 273, 20, -31, false, lock));

		// Assert
		Assertions.assertTrue(whenModifiedWasRaised.get());
		Assertions.assertFalse(whenModifiedWasRaised_idx0.get());
		Assertions.assertFalse(whenModifiedWasRaised_idx1.get());
		Assertions.assertFalse(whenModifiedWasRaised_idx2.get());
		Assertions.assertFalse(whenModifiedWasRaised_idx3.get());
		Assertions.assertTrue(whenModifiedWasRaised_idx4.get());
		Assertions.assertFalse(whenModifiedWasRaised_idx5.get());
	}

	@Test
	public void remove_shouldRaiseChangeEvent() {
		// Arrange
		ObservableField<Boolean> whenModifiedWasRaised = new ObservableField<Boolean>(false);
		ObservableField<Boolean> whenModifiedWasRaised_idx0 = new ObservableField<Boolean>(false);
		ObservableField<Boolean> whenModifiedWasRaised_idx1 = new ObservableField<Boolean>(false);
		ObservableField<Boolean> whenModifiedWasRaised_idx2 = new ObservableField<Boolean>(false);
		ObservableField<Boolean> whenModifiedWasRaised_idx3 = new ObservableField<Boolean>(false);
		ObservableField<Boolean> whenModifiedWasRaised_idx4 = new ObservableField<Boolean>(false);

		set.whenModified().subscribe(__ -> whenModifiedWasRaised.set(true));
		set.whenElementAtIndexModified().subscribe(__ -> whenModifiedWasRaised_idx0.set(true), 0);
		set.whenElementAtIndexModified().subscribe(__ -> whenModifiedWasRaised_idx1.set(true), 1);
		set.whenElementAtIndexModified().subscribe(__ -> whenModifiedWasRaised_idx2.set(true), 2);
		set.whenElementAtIndexModified().subscribe(__ -> whenModifiedWasRaised_idx3.set(true), 3);
		set.whenElementAtIndexModified().subscribe(__ -> whenModifiedWasRaised_idx4.set(true), 4);

		// Act
		set.remove(set.get(1));

		// Assert
		Assertions.assertTrue(whenModifiedWasRaised.get());
		Assertions.assertFalse(whenModifiedWasRaised_idx0.get());
		Assertions.assertTrue(whenModifiedWasRaised_idx1.get());
		Assertions.assertTrue(whenModifiedWasRaised_idx2.get());
		Assertions.assertTrue(whenModifiedWasRaised_idx3.get());
		Assertions.assertFalse(whenModifiedWasRaised_idx4.get());
	}

}
