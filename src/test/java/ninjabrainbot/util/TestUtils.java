package ninjabrainbot.util;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import ninjabrainbot.data.calculator.endereye.IThrow;
import ninjabrainbot.data.calculator.endereye.Throw;
import ninjabrainbot.data.calculator.endereye.ThrowType;
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

	public static IThrow createThrow(double x, double z, double alpha) {
		return createThrow(x, z, alpha, 0.03);
	}

	public static IThrow createThrow(double x, double z, double alpha, double std) {
		return new Throw(x, 80, z, alpha, -31, false, ThrowType.Normal, new TestStdProfile(std));
	}

	public static IThrow createThrowLookDown(double x, double z, double alpha) {
		return createThrowLookDown(x, z, alpha, 0.03);
	}

	public static IThrow createThrowLookDown(double x, double z, double alpha, double std) {
		return new Throw(x, 80, z, alpha, 31, false, ThrowType.Normal, new TestStdProfile(std));
	}

	public static IThrow createThrowNether(double x, double z, double alpha) {
		return createThrowNether(x, z, alpha, 0.03);
	}

	public static IThrow createThrowNether(double x, double z, double alpha, double std) {
		return new Throw(x, 80, z, alpha, -31, true, ThrowType.Normal, new TestStdProfile(std));
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
