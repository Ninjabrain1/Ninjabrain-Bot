package ninjabrainbot.io;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Timer;

import ninjabrainbot.Main;
import ninjabrainbot.data.IDataStateHandler;
import ninjabrainbot.event.IDisposable;
import ninjabrainbot.event.IObservable;
import ninjabrainbot.event.SubscriptionHandler;
import ninjabrainbot.gui.frames.NinjabrainBotFrame;

public class OBSOverlay implements IDisposable {

	private NinjabrainBotFrame ninjabrainBotFrame;
	private IObservable<Boolean> calculatorLocked;

	private Timer overlayClearTimer;
	private Timer overlayUpdateTimer;
	private long lastOverlayUpdate = System.currentTimeMillis();
	private final long minOverlayUpdateDelayMillis = 1000;

	public static final File OBS_OVERLAY = new File(System.getProperty("java.io.tmpdir"), "nb-overlay.png");;

	SubscriptionHandler sh = new SubscriptionHandler();

	public OBSOverlay(NinjabrainBotFrame frame, IDataStateHandler dataStateHandler) {
		this.ninjabrainBotFrame = frame;
		this.calculatorLocked = dataStateHandler.getDataState().locked();
		sh.add(dataStateHandler.whenDataStateModified().subscribeEDT(__ -> markShouldUpdate()));
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
		sh.add(Main.preferences.overlayHideDelay.whenModified().subscribeEDT(__ -> markShouldUpdate()));
		sh.add(Main.preferences.overlayAutoHide.whenModified().subscribeEDT(__ -> markShouldUpdate()));
		sh.add(Main.preferences.overlayHideWhenLocked.whenModified().subscribeEDT(__ -> markShouldUpdate()));
		sh.add(Main.preferences.useOverlay.whenModified().subscribeEDT(b -> setOverlayEnabled(b)));
	}

	private void markShouldUpdate() {
		long time = System.currentTimeMillis();
		long timeSinceLastUpdate = time - lastOverlayUpdate;
		if (timeSinceLastUpdate < minOverlayUpdateDelayMillis - 10) {
			drawLater((int) (10 + minOverlayUpdateDelayMillis - timeSinceLastUpdate));
			return;
		}
		drawAndWriteToFile();
		lastOverlayUpdate = time;
	}

	private void clear() {
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
			boolean hideBecauseLocked = Main.preferences.overlayHideWhenLocked.get() && calculatorLocked.get();
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

	@Override
	public void dispose() {
		sh.dispose();
		clear();
	}

}
