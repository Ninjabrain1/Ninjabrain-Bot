package ninjabrainbot.gui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.SplashScreen;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class Splash {

	SplashScreen splash;
	Graphics2D g;
	
	private Timer timer;
	private long startTime;
	private float progressRate;
	private String progressString;

	private static final int LOADING_BAR_HEIGHT = 12;
	private static final int LOADING_PROGRESS_FONT_SIZE = 14;
	private static final int PADDING = 5;

	public Splash() {
		splash = SplashScreen.getSplashScreen();
		if (splash == null) {
			System.err.println("Could not load splash screen");
			return;
		}
		g = splash.createGraphics();
		g.setFont(new Font("Arial", Font.PLAIN, LOADING_PROGRESS_FONT_SIZE));
		if (g == null) {
			System.err.println("Could not create graphics for splash screen");
			return;
		}
		timer = new Timer(10, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				float dt = (System.currentTimeMillis() - startTime) / 1000f;
				if (progressString == null)
					render(progressRate * dt); // interpolate
				else
					render(progressString, progressRate * dt); // interpolate
			}
		});
		timer.start();
		startTime = System.currentTimeMillis();
	}

	public void setProgress(String text, float percentage) {
		float dt = (System.currentTimeMillis() - startTime) / 1000f;
		progressRate = percentage / dt;
		progressString = text;
	}
	
	private void render(String text, float percentage) {
		if (checkIfStopped())
			return;
		Rectangle r = splash.getBounds();
		g.setComposite(AlphaComposite.Clear);
		g.fillRect(PADDING, r.height - LOADING_BAR_HEIGHT - LOADING_PROGRESS_FONT_SIZE - PADDING, 400, LOADING_PROGRESS_FONT_SIZE + PADDING);
		g.setPaintMode();
		g.setColor(Color.WHITE);
		g.drawString(text + "...", PADDING, r.height - LOADING_BAR_HEIGHT - PADDING);
		g.setColor(Color.YELLOW);
		g.fillRect(0, r.height - LOADING_BAR_HEIGHT, (int) (r.width * percentage), LOADING_BAR_HEIGHT);
		splash.update();
	}
	
	private void render(float percentage) {
		if (checkIfStopped())
			return;
		Rectangle r = splash.getBounds();
		g.setPaintMode();
		g.setColor(Color.YELLOW);
		g.fillRect(0, r.height - LOADING_BAR_HEIGHT, (int) (r.width * percentage), LOADING_BAR_HEIGHT);
		splash.update();
	}
	
	private boolean checkIfStopped() {
		if (!splash.isVisible()) {
			timer.stop();
			return true;
		}
		return false;
	}
}