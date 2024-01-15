package ninjabrainbot.gui.splash;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.SplashScreen;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import ninjabrainbot.util.Logger;

public class Splash {

	final SplashScreen splashScreen;
	Graphics2D g;

	private Timer timer;
	private long startTime;
	private float progressRate;
	private float maxProgress;
	private String progressString;

	private static final int LOADING_BAR_HEIGHT = 12;
	private static final int LOADING_PROGRESS_FONT_SIZE = 12;
	private static final int PADDING = 4;

	private static final Color LOADING_BAR_COLOR = new Color(249, 255, 173);
	private static final Color TEXT_COLOR = LOADING_BAR_COLOR;

	public Splash(boolean disabled) {
		splashScreen = SplashScreen.getSplashScreen();
		if (splashScreen == null) {
			System.err.println("Could not load splash screen");
			return;
		}
		if (disabled) splashScreen.close();

		g = splashScreen.createGraphics();
		g.setFont(new Font("Arial", Font.PLAIN, LOADING_PROGRESS_FONT_SIZE));
		if (g == null) {
			System.err.println("Could not create graphics for splash screen");
			return;
		}
		timer = new Timer(10, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				float dt = (System.currentTimeMillis() - startTime) / 1000f;
				float progress = Math.min(progressRate * dt, maxProgress);
				if (progressString == null)
					render(progress); // interpolate
				else
					render(progressString, progress); // interpolate
				progressString = null;
			}
		});
		timer.start();
		startTime = System.currentTimeMillis();
	}

	public void setProgress(String text, float percentage, float nextPercentage) {
		float dt = (System.currentTimeMillis() - startTime) / 1000f;
		progressRate = percentage / dt;
		maxProgress = nextPercentage;
		progressString = text;
	}

	private void render(String text, float percentage) {
		try {
			if (checkIfStopped())
				return;
			Rectangle r = splashScreen.getBounds();
			g.setComposite(AlphaComposite.Clear);
			g.fillRect(PADDING, r.height - LOADING_BAR_HEIGHT - LOADING_PROGRESS_FONT_SIZE - PADDING, 400, LOADING_PROGRESS_FONT_SIZE + PADDING);
			g.setPaintMode();
			g.setColor(TEXT_COLOR);
			g.drawString(text + "...", PADDING, r.height - LOADING_BAR_HEIGHT - PADDING - 2);
			g.setColor(LOADING_BAR_COLOR);
			g.fillRect(0, r.height - LOADING_BAR_HEIGHT, (int) (r.width * percentage), LOADING_BAR_HEIGHT);
			if (splashScreen.isVisible())
				splashScreen.update();
		} catch (IllegalStateException e) {
			Logger.log("Warning: Splash screen tried to render when application had finished loading.");
		}
	}

	private void render(float percentage) {
		try {
			checkIfStopped();
			if (!splashScreen.isVisible())
				return;
			Rectangle r = splashScreen.getBounds();
			g.setPaintMode();
			g.setColor(LOADING_BAR_COLOR);
			g.fillRect(0, r.height - LOADING_BAR_HEIGHT, (int) (r.width * percentage), LOADING_BAR_HEIGHT);
			if (splashScreen.isVisible())
				splashScreen.update();
		} catch (IllegalStateException e) {
			Logger.log("Warning: Splash screen tried to render when application had finished loading.");
		}
	}

	private boolean checkIfStopped() {
		if (!splashScreen.isVisible()) {
			timer.stop();
			return true;
		}
		return false;
	}
}