package ninjabrainbot.gui.buttons;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.Timer;

import ninjabrainbot.Main;
import ninjabrainbot.event.IDisposable;
import ninjabrainbot.event.DisposeHandler;
import ninjabrainbot.gui.frames.NotificationsFrame;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.io.UpdateChecker;
import ninjabrainbot.io.VersionURL;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;

public class NotificationsButton extends TitleBarButton implements IDisposable {

	StyleManager styleManager;
	NotificationsFrame notificationsFrame;
	NinjabrainBotPreferences preferences;

	// Pulsing
	int i;
	Timer timer;
	Color start, end;
	int duration = 1000;

	DisposeHandler sh;

	public NotificationsButton(StyleManager styleManager, JFrame parent, NinjabrainBotPreferences preferences) {
		super(styleManager, new ImageIcon(Main.class.getResource("/notifications_icon.png")));
		this.styleManager = styleManager;
		this.preferences = preferences;
		addActionListener(p -> toggleNotificationsWindow(parent));
		setVisible(false);
		notificationsFrame = new NotificationsFrame(styleManager, preferences);
		// Subscriptions
		sh = new DisposeHandler();
		sh.add(preferences.checkForUpdates.whenModified().subscribe(b -> onUpdatesEnabledChanged(b)));
		if (preferences.checkForUpdates.get()) {
			UpdateChecker.check(url -> setURL(url));
		}
	}

	private void onUpdatesEnabledChanged(boolean enabled) {
		if (enabled)
			UpdateChecker.check(url -> setURL(url));
		setVisible(enabled && hasURL());
	}

	private void setURL(VersionURL url) {
		setVisible(preferences.checkForUpdates.get());
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
