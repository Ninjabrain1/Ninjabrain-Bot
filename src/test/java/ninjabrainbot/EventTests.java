package ninjabrainbot;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import junit.framework.TestCase;
import ninjabrainbot.data.datalock.AlwaysUnlocked;
import ninjabrainbot.data.datalock.IModificationLock;
import ninjabrainbot.data.endereye.IThrow;
import ninjabrainbot.data.endereye.Throw;
import ninjabrainbot.event.IModifiableSet;
import ninjabrainbot.event.ModifiableSet;
import ninjabrainbot.event.ObservableField;

public class EventTests extends TestCase {

	IModifiableSet<IThrow> set;

	@Override
	protected void setUp() throws Exception {
		IModificationLock lock = new AlwaysUnlocked();
		set = new ModifiableSet<IThrow>();

		set.add(new Throw(313, 277, 90, -31, false, lock));
		set.add(new Throw(4, 2660, 30, -31, false, lock));
		set.add(new Throw(670, 273, 20, -31, false, lock));
		set.add(new Throw(1200, 243, -90, -31, false, lock));
	}

	@Test
	public void modifiableSet_setFromList_shouldntRaiseChangeEvent() {

		List<IThrow> list = set.toList();

		ObservableField<Boolean> whenModifiedWasRaised = new ObservableField<Boolean>(false);

		set.whenModified().subscribe(__ -> whenModifiedWasRaised.set(true));
		set.setFromList(list);
		assert whenModifiedWasRaised.get() == false;

		set.whenElementAtIndexModified().subscribe(__ -> whenModifiedWasRaised.set(true), 0);
		set.setFromList(list);
		assert whenModifiedWasRaised.get() == false;

		set.whenElementAtIndexModified().subscribe(__ -> whenModifiedWasRaised.set(true), 4);
		set.setFromList(list);
		Assertions.assertFalse(whenModifiedWasRaised.get());
	}

	public void modifiableSet_setFromList_shouldRaiseChangeEvent() {
		// Arrange
		IModificationLock lock = new AlwaysUnlocked();
		IModifiableSet<IThrow> set = new ModifiableSet<IThrow>();
		set.add(new Throw(313, 277, 90, -31, false, lock));
		set.add(new Throw(4, 2660, 30, -31, false, lock));
		set.add(new Throw(670, 273, 20, -31, false, lock));
		set.add(new Throw(1200, 243, -90, -31, false, lock));

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
		assert whenModifiedWasRaised.get() == true;
		assert whenModifiedWasRaised_idx0.get() == false;
		assert whenModifiedWasRaised_idx1.get() == false;
		assert whenModifiedWasRaised_idx2.get() == true;
		assert whenModifiedWasRaised_idx3.get() == true;
	}

	public void modifiableSet_add_shouldRaiseChangeEvent() {
		// Arrange
		IModificationLock lock = new AlwaysUnlocked();
		IModifiableSet<IThrow> set = new ModifiableSet<IThrow>();
		set.add(new Throw(313, 277, 90, -31, false, lock));
		set.add(new Throw(4, 2660, 30, -31, false, lock));

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
		set.add(new Throw(670, 273, 20, -31, false, lock));

		// Assert
		assert whenModifiedWasRaised.get() == true;
		assert whenModifiedWasRaised_idx0.get() == false;
		assert whenModifiedWasRaised_idx1.get() == false;
		assert whenModifiedWasRaised_idx2.get() == true;
		assert whenModifiedWasRaised_idx3.get() == false;
	}

	public void modifiableSet_remove_shouldRaiseChangeEvent() {
		// Arrange
		IModificationLock lock = new AlwaysUnlocked();
		IModifiableSet<IThrow> set = new ModifiableSet<IThrow>();
		set.add(new Throw(313, 277, 90, -31, false, lock));
		set.add(new Throw(4, 2660, 30, -31, false, lock));
		set.add(new Throw(670, 273, 20, -31, false, lock));

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
		set.remove(set.get(1));

		// Assert
		assert whenModifiedWasRaised.get() == true;
		assert whenModifiedWasRaised_idx0.get() == false;
		assert whenModifiedWasRaised_idx1.get() == true;
		assert whenModifiedWasRaised_idx2.get() == true;
		assert whenModifiedWasRaised_idx3.get() == false;
	}

}
