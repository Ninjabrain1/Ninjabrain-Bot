package ninjabrainbot.util;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import ninjabrainbot.data.datalock.AlwaysUnlocked;
import ninjabrainbot.data.endereye.IThrow;
import ninjabrainbot.data.endereye.Throw;
import ninjabrainbot.data.statistics.IRay;
import ninjabrainbot.gui.style.SizePreference;
import ninjabrainbot.gui.style.StyleManager;

public class TestUtils {

	public static IRay createRay(double x, double z, double alpha) {
		return new IRay() {

			@Override
			public double xInOverworld() {
				return x;
			}

			@Override
			public double zInOverworld() {
				return z;
			}

			@Override
			public double alpha() {
				return alpha;
			}
		};
	}

	public static IThrow createThrow(double x, double z, double alpha) {
		return new Throw(x, 80, z, alpha, -31, false, new AlwaysUnlocked());
	}

	public static IThrow createThrowLookDown(double x, double z, double alpha) {
		return new Throw(x, 80, z, alpha, 31, false, new AlwaysUnlocked());
	}

	public static IThrow createThrowNether(double x, double z, double alpha) {
		return new Throw(x, 80, z, alpha, -31, true, new AlwaysUnlocked());
	}

	public static StyleManager createStyleManager() {
		return new StyleManager(new TestTheme(), SizePreference.REGULAR);
	}

	public static void awaitSwingEvents(){
		try {
			SwingUtilities.invokeAndWait(() -> {});
		} catch (InterruptedException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

}
