package ninjabrainbot.io;

import java.awt.image.BufferedImage;

import javax.swing.SwingUtilities;
import javax.swing.Timer;

import ninjabrainbot.event.DisposeHandler;
import ninjabrainbot.event.IDisposable;
import ninjabrainbot.event.IObservable;
import ninjabrainbot.gui.frames.NinjabrainBotFrame;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.model.datastate.IDataState;
import ninjabrainbot.model.domainmodel.IDomainModel;

public class OBSOverlay implements IDisposable {

	private final NinjabrainBotPreferences preferences;
	private final NinjabrainBotFrame ninjabrainBotFrame;
	private final IObservable<Boolean> calculatorLocked;

	private final IImageWriter imageWriter;

	private Timer overlayClearTimer;
	private Timer overlayUpdateTimer;
	private long lastOverlayUpdate;
	private final long minOverlayUpdateDelayMillis;

	final DisposeHandler disposeHandler = new DisposeHandler();

	public OBSOverlay(NinjabrainBotFrame frame, NinjabrainBotPreferences preferences, IDataState dataState, IDomainModel domainModel, IImageWriter imageWriter, int updateDelayMillis) {
		this.ninjabrainBotFrame = frame;
		this.preferences = preferences;
		this.imageWriter = imageWriter;
		this.calculatorLocked = dataState.locked();
		minOverlayUpdateDelayMillis = updateDelayMillis;
		lastOverlayUpdate = System.currentTimeMillis() - minOverlayUpdateDelayMillis;

		disposeHandler.add(domainModel.whenModified().subscribeEDT(this::markShouldUpdate));
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
		disposeHandler.add(preferences.overlayHideDelay.whenModified().subscribeEDT(this::markShouldUpdate));
		disposeHandler.add(preferences.overlayAutoHide.whenModified().subscribeEDT(this::markShouldUpdate));
		disposeHandler.add(preferences.overlayHideWhenLocked.whenModified().subscribeEDT(this::markShouldUpdate));
		disposeHandler.add(preferences.useOverlay.whenModified().subscribeEDT(this::setOverlayEnabled));

		disposeHandler.add(preferences.strongholdDisplayType.whenModified().subscribeEDT(this::markShouldUpdate));
		disposeHandler.add(preferences.showNetherCoords.whenModified().subscribeEDT(this::markShouldUpdate));
		disposeHandler.add(preferences.showAngleErrors.whenModified().subscribeEDT(this::markShouldUpdate));
		disposeHandler.add(preferences.showAngleUpdates.whenModified().subscribeEDT(this::markShouldUpdate));
		disposeHandler.add(preferences.informationCombinedCertaintyEnabled.whenModified().subscribeEDT(this::markShouldUpdate));
		disposeHandler.add(preferences.informationDirectionHelpEnabled.whenModified().subscribeEDT(this::markShouldUpdate));
		disposeHandler.add(preferences.informationMismeasureEnabled.whenModified().subscribeEDT(this::markShouldUpdate));
		disposeHandler.add(preferences.informationPortalLinkingEnabled.whenModified().subscribeEDT(this::markShouldUpdate));
		disposeHandler.add(preferences.view.whenModified().subscribeEDT(this::markShouldUpdate));
		disposeHandler.add(preferences.theme.whenModified().subscribeEDT(this::markShouldUpdate));
		disposeHandler.add(preferences.size.whenModified().subscribeEDT(this::markShouldUpdate));
	}

	private void markShouldUpdate() {
		// Delay to let the frame render. I'll find a better solution later.
		SwingUtilities.invokeLater(() ->
				SwingUtilities.invokeLater(() ->
						SwingUtilities.invokeLater(() ->
								SwingUtilities.invokeLater(() ->
										SwingUtilities.invokeLater(
												this::markShouldUpdate2
										)
								)
						)
				)
		);
	}

	private void markShouldUpdate2() {
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
			imageWriter.write(img);
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
			imageWriter.write(img);
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

	private void setOverlayEnabled(boolean b) {
		if (b) {
			markShouldUpdate();
		} else {
			imageWriter.delete();
		}
	}

	@Override
	public void dispose() {
		disposeHandler.dispose();
		clear();
	}

}
