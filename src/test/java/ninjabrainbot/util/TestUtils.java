package ninjabrainbot.util;

import ninjabrainbot.data.datalock.AlwaysUnlocked;
import ninjabrainbot.data.endereye.IThrow;
import ninjabrainbot.data.endereye.Throw;
import ninjabrainbot.data.statistics.IRay;

public class TestUtils {

	public static IRay createRay(double x, double z, double alpha) {
		return new IRay() {

			@Override
			public double z() {
				return z;
			}

			@Override
			public double x() {
				return x;
			}

			@Override
			public double alpha() {
				return alpha;
			}
		};
	}

	public static IThrow createThrow(double x, double z, double alpha) {
		return new Throw(x, z, alpha, -31, false, new AlwaysUnlocked());
	}

	public static IThrow createThrowLookDown(double x, double z, double alpha) {
		return new Throw(x, z, alpha, 31, false, new AlwaysUnlocked());
	}

	public static IThrow createThrowNether(double x, double z, double alpha) {
		return new Throw(x, z, alpha, -31, true, new AlwaysUnlocked());
	}

}
