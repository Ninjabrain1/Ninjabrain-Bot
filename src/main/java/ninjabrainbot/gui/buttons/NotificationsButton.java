package ninjabrainbot.gui.buttons;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.util.Objects;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.Timer;

import ninjabrainbot.Main;
import ninjabrainbot.event.DisposeHandler;
import ninjabrainbot.event.IDisposable;
import ninjabrainbot.gui.frames.NotificationsFrame;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.io.updatechecker.IUpdateChecker;
import ninjabrainbot.io.updatechecker.VersionURL;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;

public class NotificationsButton extends TitleBarButton implements IDisposable {

	private final NotificationsFrame notificationsFrame;
	private final NinjabrainBotPreferences preferences;
	private final IUpdateChecker updateChecker;

	// Pulsing
	int i;
	Timer timer;
	Color start = Color.WHITE, end = Color.BLACK;
	final int duration = 1000;

	final DisposeHandler sh;

	public NotificationsButton(StyleManager styleManager, JFrame parent, NinjabrainBotPreferences preferences, IUpdateChecker updateChecker) {
		super(styleManager, new ImageIcon(Objects.requireNonNull(Main.class.getResource("/notifications_icon.png"))));
		this.preferences = preferences;
		this.updateChecker = updateChecker;
		addActionListener(p -> toggleNotificationsWindow(parent));
		setVisible(false);
		notificationsFrame = new NotificationsFrame(styleManager, preferences);
		// Subscriptions
		sh = new DisposeHandler();
		sh.add(preferences.checkForUpdates.whenModified().subscribeEDT(this::onUpdatesEnabledChanged));
		if (preferences.checkForUpdates.get()) {
			updateChecker.check(this::setURL);
		}
	}

	private void onUpdatesEnabledChanged(boolean enabled) {
		if (enabled)
			updateChecker.check(this::setURL);
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
