package ninjabrainbot.gui.options.sections;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import ninjabrainbot.event.DisposeHandler;
import ninjabrainbot.gui.components.labels.ThemedLabel;
import ninjabrainbot.gui.components.labels.ThemedTextArea;
import ninjabrainbot.gui.components.layout.StackPanel;
import ninjabrainbot.gui.components.preferences.CheckboxPanel;
import ninjabrainbot.gui.components.preferences.FloatPreferencePanel;
import ninjabrainbot.gui.frames.OptionsFrame;
import ninjabrainbot.gui.style.SizePreference;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.io.overlay.NinjabrainBotOverlayImageWriter;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.util.I18n;

public class ObsOptionsPanel extends StackPanel {

	private final FloatPreferencePanel overlayResetDelay;

	public ObsOptionsPanel(StyleManager styleManager, NinjabrainBotPreferences preferences, DisposeHandler disposeHandler) {
		super(3 * OptionsFrame.PADDING);
		setOpaque(false);
		setBorder(new EmptyBorder(2 * OptionsFrame.PADDING, 2 * OptionsFrame.PADDING, 2 * OptionsFrame.PADDING, 2 * OptionsFrame.PADDING));

		ThemedLabel overlayExplanation = new ThemedLabel(styleManager, "<html>" + I18n.get("settings.overlay_explanation") + "</html>") {
			public int getTextSize(SizePreference p) {
				return p.TEXT_SIZE_SMALL;
			}
		};
		JPanel info = new StackPanel();
		info.setOpaque(false);
		info.add(overlayExplanation);
		info.add(new ThemedTextArea(styleManager, NinjabrainBotOverlayImageWriter.OBS_OVERLAY.getAbsolutePath()));
		add(info);
		add(new CheckboxPanel(styleManager, I18n.get("settings.overlay_enable"), preferences.useOverlay));
		add(new CheckboxPanel(styleManager, I18n.get("settings.overlay_hide_locked"), preferences.overlayHideWhenLocked));
		add(new CheckboxPanel(styleManager, I18n.get("settings.overlay_auto_hide"), preferences.overlayAutoHide));
		overlayResetDelay = new FloatPreferencePanel(styleManager, I18n.get("settings.overlay_auto_hide_duration"), preferences.overlayHideDelay);
		overlayResetDelay.setEnabled(preferences.overlayAutoHide.get());
		add(overlayResetDelay);

		disposeHandler.add(preferences.overlayAutoHide.whenModified().subscribe(this::setOverlayAutoHideEnabled));
	}

	private void setOverlayAutoHideEnabled(boolean b) {
		overlayResetDelay.setEnabled(b);
		overlayResetDelay.descLabel.updateColors();
	}

}
