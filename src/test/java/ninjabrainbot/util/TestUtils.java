package ninjabrainbot.util;

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

}
