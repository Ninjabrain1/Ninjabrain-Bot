package ninjabrainbot.gui.components.preferences;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;

import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseWheelEvent;
import com.sun.jna.Platform;

import java.util.ArrayList;
import java.util.List;

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

			@Override
			public void updateSize(StyleManager styleManager) {
				setFont(styleManager.fontSize(getTextSize(styleManager.size), !bold, true));
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

	private List<Integer> currentKeys = new ArrayList<>();
	private List<Integer> maxCombo = new ArrayList<>();
	private int maxModifiers = 0;

	private void clicked() {
		if (!editing) {
			editing = true;
			button.setText("...");
			currentKeys.clear();
			maxCombo.clear();
			maxModifiers = 0;

			KeyboardListener.instance.setConsumer(nativeKeyEvent -> {
				if (nativeKeyEvent == null) return;
				if (nativeKeyEvent.getKeyCode() == NativeKeyEvent.VC_ESCAPE) {
					preference.setCode(-1);
					preference.setCode2(-1);
					preference.setModifier(-1);
					finishEditing();
					return;
				}

				int code = HotkeyPreference.getPlatformSpecificKeyCode(nativeKeyEvent);
				if (!currentKeys.contains(code)) {
					currentKeys.add(code);
				}

				if (currentKeys.size() >= maxCombo.size()) {
					maxCombo = new ArrayList<>(currentKeys);
					maxModifiers = nativeKeyEvent.getModifiers();
				}

				updateRecordingText();
			}, nativeKeyReleasedEvent -> {
				if (nativeKeyReleasedEvent == null) return;
				int code = HotkeyPreference.getPlatformSpecificKeyCode(nativeKeyReleasedEvent);
				currentKeys.remove((Integer) code);

				if (currentKeys.isEmpty() && !maxCombo.isEmpty()) {
					int c1 = maxCombo.get(0);
					int c2 = maxCombo.size() > 1 ? maxCombo.get(1) : -1;
					preference.setCode(c1);
					preference.setCode2(c2);
					preference.setModifier(maxModifiers);
					finishEditing();
				}
			}, nativeMouseWheelEvent -> {
				if (nativeMouseWheelEvent == null) return;
				int scrollCode = nativeMouseWheelEvent.getWheelRotation() < 0 ? HotkeyPreference.SCROLL_UP : HotkeyPreference.SCROLL_DOWN;
				
				// Handle Scroll + Key if any keys are held
				int code2 = -1;
				if (!currentKeys.isEmpty()) {
					code2 = currentKeys.get(0);
				}
				
				preference.setHotkey(scrollCode, code2);
				finishEditing();
			});
		}
	}

	private void updateRecordingText() {
		StringBuilder sb = new StringBuilder();
		if (maxModifiers != 0) {
			sb.append(NativeKeyEvent.getModifiersText(maxModifiers)).append("+");
		}
		for (int i = 0; i < maxCombo.size(); i++) {
			if (i > 0) sb.append("+");
			sb.append(getKeyName(maxCombo.get(i)));
		}
		String text = sb.toString();
		SwingUtilities.invokeLater(() -> button.setText(text.isEmpty() ? "..." : text));
	}

	private void finishEditing() {
		KeyboardListener.instance.cancelConsumer();
		String s = getKeyText();
		SwingUtilities.invokeLater(() -> {
			button.setText(s);
			editing = false;
		});
	}

	private String getNativeKeyText(int code) {
		String text = NativeKeyEvent.getKeyText(code & 0xFFFF);
		if (code >> 16 == NativeKeyEvent.KEY_LOCATION_NUMPAD && !text.startsWith("Unknown") && !text.startsWith("Num")) {
			return "Num " + text;
		}
		return text;
	}

	private String getKeyText() {
		if (preference.getCode() == -1)
			return I18n.get("settings.not_in_use");

		String k1 = getKeyName(preference.getCode());
		String k2 = preference.getCode2() != -1 ? getKeyName(preference.getCode2()) : null;

		String text = k1;
		if (k2 != null) text = k2 + " + " + k1;

		if (preference.getModifier() == 0) {
			return text;
		} else {
			return NativeKeyEvent.getModifiersText(preference.getModifier()) + "+" + text;
		}
	}

	private String getKeyName(int code) {
		if (code == HotkeyPreference.SCROLL_UP) return "Scroll Up";
		if (code == HotkeyPreference.SCROLL_DOWN) return "Scroll Down";

		String k = Platform.isLinux() || Platform.isMac() ? getNativeKeyText(code) : KeyEvent.getKeyText(code);
		if (k.startsWith("Unknown")) {
			k = k.substring(17);

			// Try to map Windows VK keycodes that aren't mapped in KeyEvent
			if (Platform.isWindows()) {
				switch (k) {
					case "0xba": k = ";"; break;
					case "0xbb": k = "="; break;
					case "0xbc": k = ","; break;
					case "0xbd": k = "-"; break;
					case "0xbe": k = "."; break;
					case "0xbf": k = "/"; break;
					case "0xc0": k = "`"; break;
					case "0xdb": k = "["; break;
					case "0xdc": k = "\\"; break;
					case "0xdd": k = "]"; break;
					case "0xde": k = "'"; break;
				}
			}
		}
		return k;
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		button.setEnabled(enabled);
		descLabel.updateColors();
	}

}