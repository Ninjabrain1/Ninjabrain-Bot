package ninjabrainbot.util;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import ninjabrainbot.model.datastate.common.DetailedPlayerPosition;
import ninjabrainbot.model.datastate.common.IDetailedPlayerPosition;
import ninjabrainbot.model.datastate.common.IOverworldRay;
import ninjabrainbot.gui.style.SizePreference;
import ninjabrainbot.gui.style.StyleManager;
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

	public static StyleManager createStyleManager() {
		return new StyleManager(new TestTheme(), SizePreference.REGULAR);
	}

	public static <T> void assertIterableEquals(Iterable<T> iterable1, Iterable<T> iterable2) {
		var iterator1 = iterable1.iterator();
		var iterator2 = iterable2.iterator();
		while (iterator1.hasNext()) {
			Assertions.assertTrue(iterator2.hasNext(), "Second iterable is shorter than the first.");
			Assertions.assertEquals(iterator1.next(), iterator2.next(), "Non-matching element.");
		}
		Assertions.assertFalse(iterator2.hasNext(), "First iterable is shorter than the second.");
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
