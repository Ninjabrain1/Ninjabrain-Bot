package ninjabrainbot.gui.options.sections;

import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import ninjabrainbot.event.DisposeHandler;
import ninjabrainbot.gui.components.labels.ThemedLabel;
import ninjabrainbot.gui.components.layout.StackPanel;
import ninjabrainbot.gui.components.preferences.DoublePreferencePanel;
import ninjabrainbot.gui.components.preferences.FloatPreferencePanel;
import ninjabrainbot.gui.components.preferences.HotkeyPanel;
import ninjabrainbot.gui.components.preferences.RadioButtonPanel;
import ninjabrainbot.gui.frames.OptionsFrame;
import ninjabrainbot.gui.style.SizePreference;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.io.preferences.enums.AngleAdjustmentType;
import ninjabrainbot.util.I18n;

public class AngleAdjustmentOptionsPanel extends JPanel {

	private final ThemedLabel tallResExplanation;
	private final FloatPreferencePanel resolutionHeight;
	private final DoublePreferencePanel customAdjustmentAmount;

	public AngleAdjustmentOptionsPanel(StyleManager styleManager, NinjabrainBotPreferences preferences, DisposeHandler disposeHandler) {
		setOpaque(false);
		setLayout(new GridLayout(1, 1, 2 * OptionsFrame.PADDING, 0));
		setBorder(new EmptyBorder(2 * OptionsFrame.PADDING, 2 * OptionsFrame.PADDING, 2 * OptionsFrame.PADDING, 2 * OptionsFrame.PADDING));
		JPanel column1 = new StackPanel();
		column1.setOpaque(false);
		add(column1);

		JPanel topCol1 = new StackPanel(2 * OptionsFrame.PADDING);
		topCol1.setOpaque(false);
		JPanel topCol2 = new StackPanel(3 * OptionsFrame.PADDING);
		topCol2.setOpaque(false);
		JPanel topGrid = new JPanel();
		topGrid.setOpaque(false);
		topGrid.setLayout(new GridLayout(1, 2, 2 * OptionsFrame.PADDING, 0));
		topGrid.setBorder(new EmptyBorder(2 * OptionsFrame.PADDING, 2 * OptionsFrame.PADDING, 2 * OptionsFrame.PADDING, 2 * OptionsFrame.PADDING));

		topGrid.add(topCol1);
		topGrid.add(topCol2);

		topCol1.add(new RadioButtonPanel(styleManager, I18n.get("settings.angle_adjustment.display_type"), preferences.angleAdjustmentDisplayType, true));

		topCol1.add(new RadioButtonPanel(styleManager, I18n.get("settings.angle_adjustment.adjustment_type"), preferences.angleAdjustmentType, true));

		FloatPreferencePanel nPixel = new FloatPreferencePanel(styleManager, I18n.get("settings.n_pixels"), preferences.nPixelCount);
		nPixel.setDecimals(0);
		topCol2.add(nPixel);
		topCol2.add(new HotkeyPanel(styleManager, I18n.get("settings.up_n_pixels"), preferences.hotkeyIncrementByN));
		topCol2.add(new HotkeyPanel(styleManager, I18n.get("settings.down_n_pixels"), preferences.hotkeyDecrementByN));

		column1.add(topGrid);

		// Tall Res Section
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
		customAdjustmentAmount = new DoublePreferencePanel(styleManager, I18n.get("settings.angle_adjustment.custom_amount"), preferences.customAdjustment);
		customAdjustmentAmount.setDecimals(8);
		column1.add(customAdjustmentAmount);

		disposeHandler.add(preferences.angleAdjustmentType.whenModified().subscribeEDT(this::onAdjustmentTypeChanged));
		onAdjustmentTypeChanged(preferences.angleAdjustmentType.get());
	}

	private void onAdjustmentTypeChanged(AngleAdjustmentType type) {
		switch (type) {
			case TALL:
				tallResExplanation.setVisible(true);
				resolutionHeight.setVisible(true);
				customAdjustmentAmount.setVisible(false);
				break;
			case CUSTOM:
				tallResExplanation.setVisible(false);
				resolutionHeight.setVisible(false);
				customAdjustmentAmount.setVisible(true);
				break;
			default:
				tallResExplanation.setVisible(false);
				resolutionHeight.setVisible(false);
				customAdjustmentAmount.setVisible(false);
		}
	}

}
