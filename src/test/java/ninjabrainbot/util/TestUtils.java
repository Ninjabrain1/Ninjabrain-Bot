package ninjabrainbot.util;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;

import javax.swing.SwingUtilities;

import ninjabrainbot.gui.style.SizePreference;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.model.datastate.IDataState;
import ninjabrainbot.model.datastate.common.DetailedPlayerPosition;
import ninjabrainbot.model.datastate.common.IDetailedPlayerPosition;
import ninjabrainbot.model.datastate.common.IOverworldRay;
import ninjabrainbot.model.domainmodel.IDomainModel;
import org.junit.jupiter.api.Assertions;

public class TestUtils {

	public static IOverworldRay createRay(double x, double z, double alpha) {
		return new IOverworldRay() {

			@Override
			public double xInOverworld() {
				return x;
			}

			@Override
			public double zInOverworld() {
				return z;
			}

			@Override
			public double horizontalAngle() {
				return alpha;
			}
		};
	}

	public static IDetailedPlayerPosition createPlayerPosition(double x, double z, double alpha) {
		return new DetailedPlayerPosition(x, 80, z, alpha, -31, false);
	}

	public static IDetailedPlayerPosition createPlayerPositionLookDown(double x, double z, double alpha) {
		return new DetailedPlayerPosition(x, 80, z, alpha, 90, false);
	}

	public static IDetailedPlayerPosition createPlayerPositionInNether(double x, double z, double alpha) {
		return new DetailedPlayerPosition(x, 80, z, alpha, 0, true);
	}

	public static <T> void assertIterableEquals(Iterable<T> iterable1, Iterable<T> iterable2) {
		Iterator<T> iterator1 = iterable1.iterator();
		Iterator<T> iterator2 = iterable2.iterator();
		while (iterator1.hasNext()) {
			Assertions.assertTrue(iterator2.hasNext(), "Second iterable is shorter than the first.");
			Assertions.assertEquals(iterator1.next(), iterator2.next(), "Non-matching element.");
		}
		Assertions.assertFalse(iterator2.hasNext(), "First iterable is shorter than the second.");
	}

	public static StyleManager createStyleManager() {
		return new StyleManager(new TestTheme(), SizePreference.REGULAR);
	}

	public static void addDummyEnderEyeThrow(IDomainModel domainModel, IDataState dataState) {
		domainModel.acquireWriteLock();
		dataState.getThrowList().add(new TestEnderEyeThrow(0, 0, 123, 0.03));
		domainModel.releaseWriteLock();
	}

	public static void awaitSwingEvents() {
		try {
			SwingUtilities.invokeAndWait(() -> {
			});
		} catch (InterruptedException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

}
