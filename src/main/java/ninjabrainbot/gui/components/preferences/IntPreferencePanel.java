package ninjabrainbot.gui.components.preferences;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.Collections;
import java.text.DecimalFormat;
import javax.swing.JSpinner;

import ninjabrainbot.gui.components.inputfields.IntTextField;
import ninjabrainbot.gui.components.labels.ThemedLabel;
import ninjabrainbot.gui.components.panels.ThemedPanel;
import ninjabrainbot.gui.style.SizePreference;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.gui.style.theme.WrappedColor;
import ninjabrainbot.io.preferences.IntPreference;

public class IntPreferencePanel extends ThemedPanel {
    public final ThemedLabel descLabel;
    IntTextField textfield;
    final IntPreference preference;
	
	WrappedColor disabledCol;

    public IntPreferencePanel(
            StyleManager styleManager,
            String description,
            IntPreference preference) {

        super(styleManager);
        this.preference = preference;

        setLayout(new BorderLayout());

        descLabel = new ThemedLabel(styleManager, "<html>" + description + "</html>") {
            @Override
            public int getTextSize(SizePreference p) {
                return p.TEXT_SIZE_SMALL;
            }

            @Override
            public Color getForegroundColor() {
                if (textfield.isEnabled()) {
                    return super.getForegroundColor();
                }
                return disabledCol.color();
            }
        };

        textfield = new IntTextField(
                styleManager,
                (int) preference.get(),
                (int) preference.min(),
                (int) preference.max()
        ) {
            @Override
            public void onChanged(double newValue) {
                preference.set((int)newValue);
            }
        };

        Dimension size = textfield.getPreferredSize();
        size.width = 80;
        textfield.setPreferredSize(size);

        add(descLabel, BorderLayout.CENTER);
        add(textfield, BorderLayout.EAST);
        setOpaque(false);

        disabledCol = styleManager.currentTheme.TEXT_COLOR_WEAK;
    }
}