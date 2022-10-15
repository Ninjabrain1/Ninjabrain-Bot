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
	 * 
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
		int r = (int) (c1.getRed() * t + c0.getRed() * (1.0f - t));
		int g = (int) (c1.getGreen() * t + c0.getGreen() * (1.0f - t));
		int b = (int) (c1.getBlue() * t + c0.getBlue() * (1.0f - t));
		int a = (int) (c1.getAlpha() * t + c0.getAlpha() * (1.0f - t));
		return new Color(r, g, b, a);
	}

}
