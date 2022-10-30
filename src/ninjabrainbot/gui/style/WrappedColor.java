package ninjabrainbot.gui.style;

import java.awt.Color;

public class WrappedColor {

	private Color color;

	public void set(WrappedColor other) {
		this.color = other.color();
	}
	
	public void set(Color color) {
		this.color = color;
	}

	public Color color() {
		return color;
	}

}
