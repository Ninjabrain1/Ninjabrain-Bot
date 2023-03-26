package ninjabrainbot.gui.style;

import java.awt.Color;

/**
 * Maps numbers in [0, 1] to colors by interpolating.
 */
public class ColorMap {

	private Color[] colors;

	public ColorMap(Color... colors) {
		this.colors = colors;
	}

	/**
	 * @param t a number in [0, 1]. If t is outside the range it will be set to the
	 *          closest number in [0, 1].
	 * @return The value of the color map at t.
	 */
	public Color get(double t) {
		int n = colors.length - 1;
		t *= n;
		// Find colors to interpolate between
		int i0 = (int) Math.floor(t);
		int i1 = (int) Math.ceil(t);
		// truncate to allowed range
		i0 = Math.max(Math.min(i0, n), 0);
		i1 = Math.max(Math.min(i1, n), 0);
		return getInterpolatedColor((float) (t - Math.floor(t)), colors[i0], colors[i1]);
	}

	private Color getInterpolatedColor(float t, Color c0, Color c1) {
		float[] hsv0 = Color.RGBtoHSB(c0.getRed(), c0.getGreen(), c0.getBlue(), null);
		float[] hsv1 = Color.RGBtoHSB(c1.getRed(), c1.getGreen(), c1.getBlue(), null);
		float h;
		if (Math.abs(hsv1[0] - hsv0[0]) < 0.5f) {
			h = hsv1[0] * t + hsv0[0] * (1.0f - t);
		} else {
			if (hsv1[0] < hsv0[0]) {
				hsv1[0]++;
			} else {
				hsv0[0]++;
			}
			h = hsv1[0] * t + hsv0[0] * (1.0f - t);
			if (h > 1)
				h--;
		}
		float s = hsv1[1] * t + hsv0[1] * (1.0f - t);
		float v = hsv1[2] * t + hsv0[2] * (1.0f - t);
		return Color.getHSBColor(h, s, v);
	}

}
