package ninjabrainbot.util;

public class Coords {
	
	public static double getPhi(double x, double z) {
		return -Math.atan2(x, z);
	}
	
}
