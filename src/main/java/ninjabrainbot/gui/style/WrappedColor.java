package ninjabrainbot.gui.style;

import java.awt.Color;

import ninjabrainbot.event.ISubscribable;
import ninjabrainbot.event.ObservableProperty;

public class WrappedColor {

	private Color color;

	private ObservableProperty<Color> whenColoChanged = new ObservableProperty<>();

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

}
