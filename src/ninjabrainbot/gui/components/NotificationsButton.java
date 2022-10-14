package ninjabrainbot.gui.components;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.Timer;

import ninjabrainbot.Main;
import ninjabrainbot.gui.StyleManager;
import ninjabrainbot.gui.NotificationsFrame;
import ninjabrainbot.io.UpdateChecker;
import ninjabrainbot.io.VersionURL;
import ninjabrainbot.util.IDisposable;
import ninjabrainbot.util.SubscriptionHandler;

public class NotificationsButton extends TitleBarButton implements IDisposable {

	private static final long serialVersionUID = -352194555884422473L;

	StyleManager styleManager;
	NotificationsFrame notificationsFrame;

	// Pulsing
	int i;
	Timer timer;
	Color start, end;
	int duration = 1000;

	SubscriptionHandler sh;

	public NotificationsButton(StyleManager styleManager, JFrame parent) {
		super(styleManager, new ImageIcon(Main.class.getResource("/resources/notifications_icon.png")));
		this.styleManager = styleManager;
		addActionListener(p -> toggleNotificationsWindow(parent));
		setVisible(false);
		notificationsFrame = new NotificationsFrame(styleManager);
		// Subscriptions
		sh = new SubscriptionHandler();
		sh.add(Main.preferences.checkForUpdates.whenModified().subscribe(b -> onUpdatesEnabledChanged(b)));
		if (Main.preferences.checkForUpdates.get()) {
			UpdateChecker.check(url -> setURL(url));
		}
	}

	private void onUpdatesEnabledChanged(boolean enabled) {
		if (enabled)
			UpdateChecker.check(url -> setURL(url));
		setVisible(enabled && hasURL());
	}

	private void setURL(VersionURL url) {
		setVisible(Main.preferences.checkForUpdates.get());
		notificationsFrame.setURL(url);
	}

	private boolean hasURL() {
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

	private void toggleNotificationsWindow(JFrame parent) {
		if (notificationsFrame.isVisible()) {
			notificationsFrame.setVisible(false);
		} else {
			notificationsFrame.setVisible(true);
			Rectangle bounds = parent.getBounds();
			notificationsFrame.setLocation(bounds.x + 10, bounds.y + 30);
		}
	}

	@Override
	public void dispose() {
		sh.dispose();
	}

}
