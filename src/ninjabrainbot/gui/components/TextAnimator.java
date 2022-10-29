package ninjabrainbot.gui.components;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.Timer;

import ninjabrainbot.gui.panels.main.ThrowPanel;
import ninjabrainbot.gui.style.ConfigurableColor;
import ninjabrainbot.gui.style.StyleManager;

public class TextAnimator implements ThemedComponent {

	int i;
	Timer timer;
	Color start, end;
	int duration;
	ThrowPanel jtp;
	
	ConfigurableColor startCol;
	ConfigurableColor endCol;

	public TextAnimator(StyleManager styleManager, int durationMillis) {
		styleManager.registerThemedComponent(this);
		this.start = Color.WHITE;
		this.end = Color.WHITE;
		this.duration = durationMillis;

		startCol = styleManager.currentTheme.TEXT_COLOR_STRONG;
		endCol = styleManager.currentTheme.TEXT_COLOR_NEUTRAL;
	}

	public void setJThrowPanel(ThrowPanel jtp) {
		i = 0;
		if (timer != null) {
			this.jtp.setForeground(end);
			timer.stop();
		}
		this.jtp = jtp;
		timer = new Timer(duration / 100, (ActionEvent event) -> {
			jtp.setForeground(getInterpolatedColor((float) i / 100f));
			i++;
			if (i == 100)
				timer.stop();
		});
		timer.start();
	}

	private Color getInterpolatedColor(float t) {
		int r = (int) (end.getRed() * t + start.getRed() * (1.0f - t));
		int g = (int) (end.getGreen() * t + start.getGreen() * (1.0f - t));
		int b = (int) (end.getBlue() * t + start.getBlue() * (1.0f - t));
		int a = (int) (end.getAlpha() * t + start.getAlpha() * (1.0f - t));
		return new Color(r, g, b, a);
	}

	public void setColors(Color start, Color end) {
		this.start = start;
		this.end = end;
	}

	@Override
	public void updateSize(StyleManager styleManager) {
	}

	@Override
	public void updateColors() {
		setColors(startCol.color(), endCol.color());
	}

}
