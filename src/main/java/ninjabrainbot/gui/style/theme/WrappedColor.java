package ninjabrainbot.gui.style.theme;

import java.awt.Color;

import ninjabrainbot.event.ISubscribable;
import ninjabrainbot.event.ObservableProperty;

public class WrappedColor {

	private Color color;

	private final ObservableProperty<Color> whenColoChanged = new ObservableProperty<>();

	public void set(WrappedColor other) {
		if (this.color == other.color())
			return;
		this.color = other.color();
		whenColoChanged.notifySubscribers(color);
	}

	public void set(Color color) {
		if (this.color == color)
			return;
		this.color = color;
		whenColoChanged.notifySubscribers(color);
	}

	public ISubscribable<Color> whenColorChanged() {
		return whenColoChanged;
	}

	public Color color() {
		return color;
	}

	public boolean isEquivalentTo(WrappedColor other) {
		return color.getRGB() == other.color.getRGB();
	}

	public String hex() {
		Color c = color();
		return String.format("#%02X%02X%02X", c.getRed(), c.getGreen(), c.getBlue());
	}

	public Color interpolate(Color other, float t) {
		int r = (int) (color().getRed() * (1f - t) + other.getRed() * t);
		int g = (int) (color().getGreen() * (1f - t) + other.getGreen() * t);
		int b = (int) (color().getBlue() * (1f - t) + other.getBlue() * t);
		return new Color(r, g, b);
	}

}
