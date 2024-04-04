package ninjabrainbot.gui.options.sections;

import ninjabrainbot.event.DisposeHandler;
import ninjabrainbot.gui.components.labels.ThemedLabel;
import ninjabrainbot.gui.components.layout.StackPanel;
import ninjabrainbot.gui.components.preferences.CheckboxPanel;
import ninjabrainbot.gui.components.preferences.FloatPreferencePanel;
import ninjabrainbot.gui.frames.OptionsFrame;
import ninjabrainbot.gui.style.SizePreference;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.util.I18n;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SubpixelOptionsPanel extends JPanel {

	private final FloatPreferencePanel resolutionHeight;

	public SubpixelOptionsPanel(StyleManager styleManager, NinjabrainBotPreferences preferences, DisposeHandler disposeHandler) {
		setOpaque(false);
		setLayout(new GridLayout(1, 1, 2 * OptionsFrame.PADDING, 0));
		setBorder(new EmptyBorder(2 * OptionsFrame.PADDING, 2 * OptionsFrame.PADDING, 2 * OptionsFrame.PADDING, 2 * OptionsFrame.PADDING));
		JPanel column1 = new StackPanel();
		column1.setOpaque(false);
		add(column1);

		// Tall Res Column
		column1.add(new ThemedLabel(styleManager, "<html>" + I18n.get("settings.tall_resolution_explanation") + "</html>") {
			public int getTextSize(SizePreference p) {
				return p.TEXT_SIZE_SMALL;
			}
		});
		column1.add(new CheckboxPanel(styleManager, I18n.get("settings.tall_resolution"), preferences.useTallRes));
		resolutionHeight = new FloatPreferencePanel(styleManager, I18n.get("settings.resolution_height"), preferences.resolutionHeight);
		resolutionHeight.setDecimals(0);
		resolutionHeight.setEnabled(preferences.useTallRes.get());
		column1.add(resolutionHeight);

		disposeHandler.add(preferences.useTallRes.whenModified().subscribeEDT(this::setTallResolutionEnabled));
		setTallResolutionEnabled(preferences.useTallRes.get());
	}

	private void setTallResolutionEnabled(boolean b) {
		resolutionHeight.setEnabled(b);
		resolutionHeight.descLabel.updateColors();
	}

}
