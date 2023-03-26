package ninjabrainbot.gui.options.sections;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.View;

import ninjabrainbot.event.SubscriptionHandler;
import ninjabrainbot.gui.components.labels.ThemedLabel;
import ninjabrainbot.gui.components.labels.ThemedTextArea;
import ninjabrainbot.gui.frames.OptionsFrame;
import ninjabrainbot.gui.components.preferences.CheckboxPanel;
import ninjabrainbot.gui.components.preferences.FloatPreferencePanel;
import ninjabrainbot.gui.style.SizePreference;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.io.OBSOverlay;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.util.I18n;

public class ObsOptionsPanel extends JPanel {

	private final FloatPreferencePanel overlayResetDelay;

	public ObsOptionsPanel(StyleManager styleManager, NinjabrainBotPreferences preferences, SubscriptionHandler subscriptionHandler) {
		setOpaque(false);
		setLayout(new GridBagLayout());
		setBorder(new EmptyBorder(OptionsFrame.PADDING, OptionsFrame.PADDING, OptionsFrame.PADDING, OptionsFrame.PADDING));

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridy = GridBagConstraints.RELATIVE;
		constraints.gridx = 0;
		constraints.insets = new Insets(0, OptionsFrame.PADDING, 0, OptionsFrame.PADDING);
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.weightx = 1;
		ThemedLabel overlayExplanation = new ThemedLabel(styleManager, "<html>" + I18n.get("settings.overlay_explanation") + "</html>") {
			private static final long serialVersionUID = 7980539999697524316L;

			public int getTextSize(SizePreference p) {
				return p.TEXT_SIZE_SMALL;
			}

			public Dimension getPreferredSize() {
				View view = (View) getClientProperty(javax.swing.plaf.basic.BasicHTML.propertyKey);
				view.setSize(OptionsFrame.WINDOW_WIDTH - 2 * OptionsFrame.PADDING, 0);
				float w = view.getPreferredSpan(View.X_AXIS);
				float h = view.getPreferredSpan(View.Y_AXIS);
				return new java.awt.Dimension((int) Math.ceil(w), (int) Math.ceil(h));
			}
		};
		add(overlayExplanation, constraints);
		add(new ThemedTextArea(styleManager, OBSOverlay.OBS_OVERLAY.getAbsolutePath()), constraints);
		constraints.insets = new Insets(0, 0, 0, 0);
		add(new CheckboxPanel(styleManager, I18n.get("settings.overlay_enable"), preferences.useOverlay), constraints);
		add(new CheckboxPanel(styleManager, I18n.get("settings.overlay_hide_locked"), preferences.overlayHideWhenLocked), constraints);
		add(new CheckboxPanel(styleManager, I18n.get("settings.overlay_auto_hide"), preferences.overlayAutoHide), constraints);
		overlayResetDelay = new FloatPreferencePanel(styleManager, I18n.get("settings.overlay_auto_hide_duration"), preferences.overlayHideDelay);
		overlayResetDelay.setEnabled(preferences.overlayAutoHide.get());
		add(overlayResetDelay, constraints);

		constraints.weighty = 1;
		add(Box.createGlue(), constraints);

		subscriptionHandler.add(preferences.overlayAutoHide.whenModified().subscribe(this::setOverlayAutoHideEnabled));
	}

	private void setOverlayAutoHideEnabled(boolean b) {
		overlayResetDelay.setEnabled(b);
		overlayResetDelay.descLabel.updateColors();
	}

}
