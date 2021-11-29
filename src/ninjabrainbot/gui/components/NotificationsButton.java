package ninjabrainbot.gui.components;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.Timer;

import ninjabrainbot.Main;
import ninjabrainbot.gui.GUI;
import ninjabrainbot.io.VersionURL;

public class NotificationsButton extends TitleBarButton {
	
	private static final long serialVersionUID = -352194555884422473L;
	
	GUI gui;
	NotificationsFrame notificationsFrame;
	
	// Pulsing
	int i;
	Timer timer;
	Color start, end;
	int duration = 1000;
	
	public NotificationsButton(GUI gui) {
		super(gui, new ImageIcon(Main.class.getResource("/resources/notifications_icon.png")));
		this.gui = gui;
		addActionListener(p -> toggleNotificationsWindow());
		setVisible(false);
		notificationsFrame = new NotificationsFrame(gui);
	}
	
	public void setURL(VersionURL url) {
		setVisible(Main.preferences.checkForUpdates.get());
		notificationsFrame.setURL(url);
	}
	
	public boolean hasURL() {
		return notificationsFrame.getURL() != null;
	}

	private Color getInterpolatedColor(float t) {
		int r = (int) (end.getRed() * t + start.getRed() * (1.0f - t));
		int g = (int) (end.getGreen() * t + start.getGreen() * (1.0f - t));
		int b = (int) (end.getBlue() * t + start.getBlue() * (1.0f - t));
		int a = (int) (end.getAlpha() * t + start.getAlpha() * (1.0f - t));
		return new Color(r, g, b, a);
	}
	
	private Color getInterpolatedColor(Color start, Color end, float t) {
		int r = (int) (end.getRed() * t + start.getRed() * (1.0f - t));
		int g = (int) (end.getGreen() * t + start.getGreen() * (1.0f - t));
		int b = (int) (end.getBlue() * t + start.getBlue() * (1.0f - t));
		int a = (int) (end.getAlpha() * t + start.getAlpha() * (1.0f - t));
		return new Color(r, g, b, a);
	}
	
	public void setColors(final Color backgroundColor, final Color hoverColor) {
		super.setColors(backgroundColor, hoverColor);
		this.start = backgroundColor;
		this.end = getInterpolatedColor(this.start, Color.YELLOW, 0.3f);
	}
	
	@Override
	public void setVisible(boolean b) {
		if (b) {
			setBackgroundColor(getInterpolatedColor(0));
			// pulsing
			i = 0;
			timer = new Timer(duration / 30, (ActionEvent event) -> {
				Color c = getInterpolatedColor(0.5f - 0.5f * (float) Math.cos(2 * Math.PI * i / 30f));
				setBackgroundColor(c);
				hoverCol = c;
				i++;
				if (i == 30)
					i = 0;
			});
			timer.start();
		} else {
			if (timer != null)
				timer.stop();
		}
		super.setVisible(b);
	}
	
	@Override
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
	}

	public NotificationsFrame getNotificationsFrame() {
		return notificationsFrame;
	}
	
	private void toggleNotificationsWindow() {
		if (notificationsFrame.isVisible()) {
			notificationsFrame.setVisible(false);
		} else {
			notificationsFrame.setVisible(true);
			Rectangle bounds = gui.frame.getBounds();
			notificationsFrame.setLocation(bounds.x + 40, bounds.y + 30);
		}
	}
	
}
