package ninjabrainbot.util;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import ninjabrainbot.data.calculator.common.DetailedPlayerPosition;
import ninjabrainbot.data.calculator.common.IDetailedPlayerPosition;
import ninjabrainbot.data.calculator.common.IOverworldRay;
import ninjabrainbot.gui.style.SizePreference;
import ninjabrainbot.gui.style.StyleManager;

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

	public static void awaitSwingEvents() {
		try {
			SwingUtilities.invokeAndWait(() -> {
			});
		} catch (InterruptedException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

}
