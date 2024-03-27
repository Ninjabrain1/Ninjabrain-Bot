package ninjabrainbot.gui.components.preferences;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;

import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.sun.jna.Platform;
import ninjabrainbot.gui.buttons.FlatButton;
import ninjabrainbot.gui.components.labels.ThemedLabel;
import ninjabrainbot.gui.components.panels.ThemedPanel;
import ninjabrainbot.gui.frames.OptionsFrame;
import ninjabrainbot.gui.style.SizePreference;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.gui.style.theme.WrappedColor;
import ninjabrainbot.io.KeyboardListener;
import ninjabrainbot.io.preferences.HotkeyPreference;
import ninjabrainbot.util.I18n;

public class HotkeyPanel extends ThemedPanel {

	public final ThemedLabel descLabel;
	FlatButton button;
	final HotkeyPreference preference;
	boolean editing = false;

	WrappedColor disabledCol;

	public HotkeyPanel(StyleManager styleManager, String description, HotkeyPreference preference) {
		super(styleManager);
		this.preference = preference;
		setLayout(new GridLayout(1, 2, 0, 0));
		descLabel = new ThemedLabel(styleManager, "<html>" + description + "</html>") {
			@Override
			public int getTextSize(SizePreference p) {
				return p.TEXT_SIZE_SMALL;
			}

			@Override
			public Color getForegroundColor() {
				if (button.isEnabled()) {
					return super.getForegroundColor();
				}
				return disabledCol.color();
			}
		};
		descLabel.setHorizontalAlignment(SwingConstants.LEFT);
		button = new FlatButton(styleManager, getKeyText()) {
			@Override
			public int getTextSize(SizePreference p) {
				return p.TEXT_SIZE_SMALL;
			}
		};
		button.addActionListener(p -> clicked());
		Dimension size = button.getPreferredSize();
		size.width = OptionsFrame.WINDOW_WIDTH / 4;
		button.setPreferredSize(size);
		add(descLabel);
		add(button);
		setOpaque(false);

		disabledCol = styleManager.currentTheme.TEXT_COLOR_WEAK;
	}

	private void clicked() {
		if (!editing) {
			editing = true;
			button.setText("...");
			KeyboardListener.instance.setConsumer(nativeKeyEvent -> {
				if (nativeKeyEvent == null) {
					// Canceled, dont change anything
				} else if (nativeKeyEvent.getKeyCode() == NativeKeyEvent.VC_ESCAPE) {
					preference.setCode(-1);
					preference.setModifier(-1);
				} else {
					preference.setHotkey(nativeKeyEvent);
				}
				String s = getKeyText();
				SwingUtilities.invokeLater(() -> {
					button.setText(s);
					editing = false;
				});
			});
		}
	}

	private String getKeyText() {
		if (preference.getCode() == -1)
			return I18n.get("settings.not_in_use");
		String k = Platform.isLinux() ? NativeKeyEvent.getKeyText(preference.getCode()) : KeyEvent.getKeyText(preference.getCode());
		if (k.startsWith("Unknown")) {
			k = k.substring(17);
		}
		if (preference.getModifier() == 0) {
			return k;
		} else {
			return NativeKeyEvent.getModifiersText(preference.getModifier()) + "+" + k;
		}
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		button.setEnabled(enabled);
		descLabel.updateColors();
	}

}