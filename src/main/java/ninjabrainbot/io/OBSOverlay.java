package ninjabrainbot.io;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Timer;

import ninjabrainbot.data.IDataStateHandler;
import ninjabrainbot.event.DisposeHandler;
import ninjabrainbot.event.IDisposable;
import ninjabrainbot.event.IObservable;
import ninjabrainbot.gui.frames.NinjabrainBotFrame;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;

public class OBSOverlay implements IDisposable {

	private final NinjabrainBotPreferences preferences;
	private final NinjabrainBotFrame ninjabrainBotFrame;
	private final IObservable<Boolean> calculatorLocked;

	private Timer overlayClearTimer;
	private Timer overlayUpdateTimer;
	private long lastOverlayUpdate = System.currentTimeMillis();
	private static final long minOverlayUpdateDelayMillis = 1000;

	public static final File OBS_OVERLAY = new File(System.getProperty("java.io.tmpdir"), "nb-overlay.png");

	final DisposeHandler disposeHandler = new DisposeHandler();

	public OBSOverlay(NinjabrainBotFrame frame, NinjabrainBotPreferences preferences, IDataStateHandler dataStateHandler) {
		this.ninjabrainBotFrame = frame;
		this.preferences = preferences;
		this.calculatorLocked = dataStateHandler.getDataState().locked();
		disposeHandler.add(dataStateHandler.whenDataStateModified().subscribeEDT(__ -> markShouldUpdate()));
		createClearTimer();
		createUpdateTimer();
		setupSettingsSubscriptions();
		markShouldUpdate();
	}

	private void createClearTimer() {
		overlayClearTimer = new Timer((int) (preferences.overlayHideDelay.get() * 1000f), p -> clear());
	}

	private void createUpdateTimer() {
		overlayUpdateTimer = new Timer(1000, p -> {
			overlayUpdateTimer.stop();
			markShouldUpdate();
		});
	}

	private void setupSettingsSubscriptions() {
		disposeHandler.add(preferences.overlayHideDelay.whenModified().subscribeEDT(__ -> markShouldUpdate()));
		disposeHandler.add(preferences.overlayAutoHide.whenModified().subscribeEDT(__ -> markShouldUpdate()));
		disposeHandler.add(preferences.overlayHideWhenLocked.whenModified().subscribeEDT(__ -> markShouldUpdate()));
		disposeHandler.add(preferences.useOverlay.whenModified().subscribeEDT(this::setOverlayEnabled));
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
		if (preferences.useOverlay.get()) {
			BufferedImage img = new BufferedImage(ninjabrainBotFrame.getWidth(), ninjabrainBotFrame.getHeight(), BufferedImage.TYPE_INT_ARGB);
			write(img);
			if (preferences.overlayAutoHide.get()) {
				overlayClearTimer.stop();
			}
		}
	}

	private void drawLater(int delay) {
		overlayUpdateTimer.setInitialDelay(delay);
		overlayUpdateTimer.restart();
	}

	private void drawAndWriteToFile() {
		if (preferences.useOverlay.get()) {
			BufferedImage img = new BufferedImage(ninjabrainBotFrame.getWidth(), ninjabrainBotFrame.getHeight(), BufferedImage.TYPE_INT_ARGB);
			boolean hideBecauseLocked = preferences.overlayHideWhenLocked.get() && calculatorLocked.get();
			if (!ninjabrainBotFrame.isIdle() && !hideBecauseLocked) {
				ninjabrainBotFrame.paint(img.createGraphics());
				resetClearTimer();
			}
			write(img);
		}
	}

	private void resetClearTimer() {
		if (preferences.overlayAutoHide.get()) {
			overlayClearTimer.setInitialDelay((int) (preferences.overlayHideDelay.get() * 1000f));
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
		disposeHandler.dispose();
		clear();
	}

}
