package ninjabrainbot.io;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Timer;

import ninjabrainbot.Main;
import ninjabrainbot.data.IDataState;
import ninjabrainbot.gui.frames.NinjabrainBotFrame;

public class OBSOverlay {

	private NinjabrainBotFrame ninjabrainBotFrame;
	private IDataState dataState;

	private Timer overlayClearTimer;
	private Timer overlayUpdateTimer;
	private long lastOverlayUpdate = System.currentTimeMillis();
	private final long minOverlayUpdateDelayMillis = 1000;

	public static final File OBS_OVERLAY = new File(System.getProperty("java.io.tmpdir"), "nb-overlay.png");;

	public OBSOverlay(NinjabrainBotFrame frame, IDataState dataState) {
		this.ninjabrainBotFrame = frame;
		this.dataState = dataState;
		createClearTimer();
		createUpdateTimer();
		setupSettingsSubscriptions();
		markShouldUpdate();
	}

	private void createClearTimer() {
		overlayClearTimer = new Timer((int) (Main.preferences.overlayHideDelay.get() * 1000f), p -> clear());
	}

	private void createUpdateTimer() {
		overlayUpdateTimer = new Timer(1000, p -> {
			overlayUpdateTimer.stop();
			markShouldUpdate();
		});
	}

	private void setupSettingsSubscriptions() {
		Main.preferences.overlayHideDelay.whenModified().subscribe(__ -> markShouldUpdate());
		Main.preferences.overlayAutoHide.whenModified().subscribe(__ -> markShouldUpdate());
		Main.preferences.overlayHideWhenLocked.whenModified().subscribe(__ -> markShouldUpdate());
		Main.preferences.useOverlay.whenModified().subscribe(b -> setOverlayEnabled(b));
	}

	public void markShouldUpdate() {
		long time = System.currentTimeMillis();
		long timeSinceLastUpdate = time - lastOverlayUpdate;
		if (timeSinceLastUpdate < minOverlayUpdateDelayMillis - 10) {
			drawLater((int) (10 + minOverlayUpdateDelayMillis - timeSinceLastUpdate));
			return;
		}
		drawAndWriteToFile();
		lastOverlayUpdate = time;
	}
	
	public void clear() {
		if (Main.preferences.useOverlay.get()) {
			BufferedImage img = new BufferedImage(ninjabrainBotFrame.getWidth(), ninjabrainBotFrame.getHeight(), BufferedImage.TYPE_INT_ARGB);
			write(img);
			if (Main.preferences.overlayAutoHide.get()) {
				overlayClearTimer.stop();
			}
		}
	}

	private void drawLater(int delay) {
		overlayUpdateTimer.setInitialDelay(delay);
		overlayUpdateTimer.restart();
	}

	private void drawAndWriteToFile() {
		if (Main.preferences.useOverlay.get()) {
			BufferedImage img = new BufferedImage(ninjabrainBotFrame.getWidth(), ninjabrainBotFrame.getHeight(), BufferedImage.TYPE_INT_ARGB);
			boolean hideBecauseLocked = Main.preferences.overlayHideWhenLocked.get() && dataState.locked().get();
			if (!ninjabrainBotFrame.isIdle() && !hideBecauseLocked) {
				ninjabrainBotFrame.paint(img.createGraphics());
				resetClearTimer();
			}
			write(img);
		}
	}

	private void resetClearTimer() {
		if (Main.preferences.overlayAutoHide.get()) {
			overlayClearTimer.setInitialDelay((int) (Main.preferences.overlayHideDelay.get() * 1000f));
			overlayClearTimer.restart();
		} else {
			overlayClearTimer.stop();
		}
	}

	private void write(BufferedImage img) {
		try {
			ImageIO.write(img, "png", OBS_OVERLAY);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void setOverlayEnabled(boolean b) {
		if (b) {
			markShouldUpdate();
		} else {
			OBS_OVERLAY.delete();
		}
	}

}
