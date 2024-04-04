package ninjabrainbot.gui.options.sections;

import ninjabrainbot.event.DisposeHandler;
import ninjabrainbot.gui.components.labels.ThemedLabel;
import ninjabrainbot.gui.components.layout.Divider;
import ninjabrainbot.gui.components.layout.StackPanel;
import ninjabrainbot.gui.components.preferences.DoublePreferencePanel;
import ninjabrainbot.gui.components.preferences.FloatPreferencePanel;
import ninjabrainbot.gui.components.preferences.RadioButtonPanel;
import ninjabrainbot.gui.frames.OptionsFrame;
import ninjabrainbot.gui.style.SizePreference;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.io.preferences.enums.SubpixelAdjustmentType;
import ninjabrainbot.util.I18n;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SubpixelOptionsPanel extends JPanel {

	private final ThemedLabel tallResExplanation;
    private final FloatPreferencePanel resolutionHeight;
	private final DoublePreferencePanel customAdjustmentAmount;

	public SubpixelOptionsPanel(StyleManager styleManager, NinjabrainBotPreferences preferences, DisposeHandler disposeHandler) {
		setOpaque(false);
		setLayout(new GridLayout(1, 1, 2 * OptionsFrame.PADDING, 0));
		setBorder(new EmptyBorder(2 * OptionsFrame.PADDING, 2 * OptionsFrame.PADDING, 2 * OptionsFrame.PADDING, 2 * OptionsFrame.PADDING));
		JPanel column1 = new StackPanel();
		column1.setOpaque(false);
		add(column1);

        column1.add(new RadioButtonPanel(styleManager, I18n.get("settings.subpixel_adjustment.adjustment_type"), preferences.subpixelAdjustmentType, true));

		// Tall Res Section
		column1.add(new Divider(styleManager));

		tallResExplanation = new ThemedLabel(styleManager, "<html>" + I18n.get("settings.tall_resolution_explanation") + "</html>") {
			public int getTextSize(SizePreference p) {
				return p.TEXT_SIZE_SMALL;
			}
		};
		column1.add(tallResExplanation);

		resolutionHeight = new FloatPreferencePanel(styleManager, I18n.get("settings.resolution_height"), preferences.resolutionHeight);
		resolutionHeight.setDecimals(0);
		column1.add(resolutionHeight);

		// Custom Adjustment Section
		column1.add(new Divider(styleManager));

		customAdjustmentAmount = new DoublePreferencePanel(styleManager, I18n.get("settings.subpixel_adjustment.custom_amount"), preferences.customAdjustment);
		customAdjustmentAmount.setDecimals(8);
		column1.add(customAdjustmentAmount);

		disposeHandler.add(preferences.subpixelAdjustmentType.whenModified().subscribeEDT(this::onAdjustmentTypeChanged));
		onAdjustmentTypeChanged(preferences.subpixelAdjustmentType.get());
	}

	private void onAdjustmentTypeChanged(SubpixelAdjustmentType type) {
		switch (type) {
			case TALL:
				tallResExplanation.setEnabled(true);
				resolutionHeight.setEnabled(true);
				customAdjustmentAmount.setEnabled(false);
				break;
			case CUSTOM:
				tallResExplanation.setEnabled(false);
				resolutionHeight.setEnabled(false);
				customAdjustmentAmount.setEnabled(true);
				break;
			default:
				tallResExplanation.setEnabled(false);
				resolutionHeight.setEnabled(false);
				customAdjustmentAmount.setEnabled(false);
		}
	}

}
