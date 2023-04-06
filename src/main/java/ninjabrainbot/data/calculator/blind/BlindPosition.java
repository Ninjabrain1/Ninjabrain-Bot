package ninjabrainbot.data.calculator.blind;

import ninjabrainbot.data.calculator.endereye.IThrow;

public class BlindPosition {
	public final double x, z;

	public BlindPosition(double x, double z) {
		this.x = x;
		this.z = z;
	}

	public BlindPosition(IThrow playerPos) {
		this(playerPos.xInPlayerDimension(), playerPos.zInPlayerDimension());
	}

}
