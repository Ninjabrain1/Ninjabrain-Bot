package ninjabrainbot.gui.components;

import java.awt.Color;
import java.awt.Dimension;

import ninjabrainbot.gui.style.StyleManager;

public class HexThemedTextField extends ThemedTextField {

	private static final long serialVersionUID = 6963733270568097925L;

	public HexThemedTextField(StyleManager styleManager) {
		super(styleManager);
		setPreferredSize(styleManager);
	}

	@Override
	protected String preProcessText(String text) {
		text = text.toUpperCase();
		if (!text.startsWith("#"))
			text = "#" + text;
		while (text.length() < 7)
			text = text + "0";
		return text;
	}

	@Override
	protected boolean verifyInput(String text) {
		return text.matches("^#(?:[0-9A-F]{6})$");
	}

	private String toHex(Color c) {
		return String.format("#%02X%02X%02X", c.getRed(), c.getGreen(), c.getBlue());
	}

	public void setColor(Color c) {
		String hex = toHex(c);
		if (validatedProcessedText.get() == null || !hex.contentEquals(validatedProcessedText.get()))
			setText(hex);
	}

	@Override
	public void updateSize(StyleManager styleManager) {
		super.updateSize(styleManager);
		setPreferredSize(styleManager);
	}

	private void setPreferredSize(StyleManager styleManager) {
		int ts = getTextSize(styleManager.size);
		setPreferredSize(new Dimension(ts * 5, ts + 2));
	}

}
