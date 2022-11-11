package test;

import java.awt.Color;

import ninjabrainbot.gui.settings.themeeditor.ThemeSerializer;
import ninjabrainbot.gui.style.CustomTheme;
import ninjabrainbot.gui.style.Theme;

public class ThemeSerializerTests {

	public void serialize() {
		CustomTheme theme = new CustomTheme();
		String serialized = ThemeSerializer.serialize(theme);
		assert ThemeSerializer.serialize(ThemeSerializer.deserialize(serialized)).contentEquals(serialized);
	}

	public void serializeColor() {
		// https://www.cs.cmu.edu/~pattis/15-1XX/common/handouts/ascii.html
		assert ThemeSerializer.serializeColor(Color.white).contentEquals("oooo");
		assert ThemeSerializer.serializeColor(Color.black).contentEquals("0000");
		assert ThemeSerializer.serializeColor(Color.red).contentEquals("o`00");
		assert ThemeSerializer.serializeColor(Color.green).contentEquals("0?l0");
		assert ThemeSerializer.serializeColor(Color.blue).contentEquals("003o");
	}

	public void deserializeColor() {
		// https://www.cs.cmu.edu/~pattis/15-1XX/common/handouts/ascii.html
		assert colorsAreEqual(ThemeSerializer.deserializeColor("oooo"), Color.white);
		assert colorsAreEqual(ThemeSerializer.deserializeColor("0000"), Color.black);
		assert colorsAreEqual(ThemeSerializer.deserializeColor("o`00"), Color.red);
		assert colorsAreEqual(ThemeSerializer.deserializeColor("0?l0"), Color.green);
		assert colorsAreEqual(ThemeSerializer.deserializeColor("003o"), Color.blue);
	}

	public void deserizlizeColor_shouldFail() {
		assert ThemeSerializer.deserializeColor("") == null;
		assert ThemeSerializer.deserializeColor("00000") == null;
		assert ThemeSerializer.deserializeColor("aaa") == null;
	}

	public void deserizlizeColor_shouldThrow() {
		boolean thrown = false;
		try {
			ThemeSerializer.deserializeColor("000p");
		} catch (IllegalArgumentException e) {
			thrown = true;
		}
		assert thrown == true;

		thrown = false;
		try {
			ThemeSerializer.deserializeColor("0 00");
		} catch (IllegalArgumentException e) {
			thrown = true;
		}
		assert thrown == true;

		thrown = false;
		try {
			ThemeSerializer.deserializeColor("/asd");
		} catch (IllegalArgumentException e) {
			thrown = true;
		}
		assert thrown == true;
	}

	public void deserialize_serialize_shouldBeIdenticalToOriginal() {
		String string = "a71`Wb9BDhc:be4d:2Tme8B4`f71`Wgl4M7noooohooooiiNGUjP820k`<30lMLa\\mc6ib";
		assert ThemeSerializer.serialize(ThemeSerializer.deserialize(string)).contentEquals(string);
	}

	public void serialize_deserialize_shouldBeIdenticalToOriginal_light() {
		CustomTheme a = new CustomTheme();
		a.setFromTheme(Theme.get(Theme.light_uid));
		CustomTheme b = ThemeSerializer.deserialize(ThemeSerializer.serialize(a));
		assert colorsAreEqual(a.COLOR_DIVIDER.color(), b.COLOR_DIVIDER.color());
		assert colorsAreEqual(a.COLOR_DIVIDER_DARK.color(), b.COLOR_DIVIDER_DARK.color());
		assert colorsAreEqual(a.COLOR_EXIT_BUTTON_HOVER.color(), b.COLOR_EXIT_BUTTON_HOVER.color());
		assert colorsAreEqual(a.COLOR_NEGATIVE.color(), b.COLOR_NEGATIVE.color());
		assert colorsAreEqual(a.COLOR_NEUTRAL.color(), b.COLOR_NEUTRAL.color());
		assert colorsAreEqual(a.COLOR_NEGATIVE.color(), b.COLOR_NEGATIVE.color());
		assert colorsAreEqual(a.COLOR_POSITIVE.color(), b.COLOR_POSITIVE.color());
		assert colorsAreEqual(a.COLOR_SATURATED.color(), b.COLOR_SATURATED.color());
		assert colorsAreEqual(a.COLOR_SLIGHTLY_STRONG.color(), b.COLOR_SLIGHTLY_STRONG.color());
		assert colorsAreEqual(a.COLOR_SLIGHTLY_WEAK.color(), b.COLOR_SLIGHTLY_WEAK.color());
		assert colorsAreEqual(a.COLOR_STRONG.color(), b.COLOR_STRONG.color());
		assert colorsAreEqual(a.COLOR_STRONGEST.color(), b.COLOR_STRONGEST.color());
		assert colorsAreEqual(a.TEXT_COLOR_NEUTRAL.color(), b.TEXT_COLOR_NEUTRAL.color());
		assert colorsAreEqual(a.TEXT_COLOR_SLIGHTLY_STRONG.color(), b.TEXT_COLOR_SLIGHTLY_STRONG.color());
		assert colorsAreEqual(a.TEXT_COLOR_STRONG.color(), b.TEXT_COLOR_STRONG.color());
		assert colorsAreEqual(a.TEXT_COLOR_WEAK.color(), b.TEXT_COLOR_WEAK.color());
		assert colorsAreEqual(a.ICON_COLOR.color(), b.ICON_COLOR.color());
	}

	public void serialize_deserialize_shouldBeIdenticalToOriginal_blue() {
		CustomTheme a = new CustomTheme();
		a.setFromTheme(Theme.get(Theme.blue_uid));
		CustomTheme b = ThemeSerializer.deserialize(ThemeSerializer.serialize(a));
		assert colorsAreEqual(a.COLOR_DIVIDER.color(), b.COLOR_DIVIDER.color());
		assert colorsAreEqual(a.COLOR_DIVIDER_DARK.color(), b.COLOR_DIVIDER_DARK.color());
		assert colorsAreEqual(a.COLOR_EXIT_BUTTON_HOVER.color(), b.COLOR_EXIT_BUTTON_HOVER.color());
		assert colorsAreEqual(a.COLOR_NEGATIVE.color(), b.COLOR_NEGATIVE.color());
		assert colorsAreEqual(a.COLOR_NEUTRAL.color(), b.COLOR_NEUTRAL.color());
		assert colorsAreEqual(a.COLOR_NEGATIVE.color(), b.COLOR_NEGATIVE.color());
		assert colorsAreEqual(a.COLOR_POSITIVE.color(), b.COLOR_POSITIVE.color());
		assert colorsAreEqual(a.COLOR_SATURATED.color(), b.COLOR_SATURATED.color());
		assert colorsAreEqual(a.COLOR_SLIGHTLY_STRONG.color(), b.COLOR_SLIGHTLY_STRONG.color());
		assert colorsAreEqual(a.COLOR_SLIGHTLY_WEAK.color(), b.COLOR_SLIGHTLY_WEAK.color());
		assert colorsAreEqual(a.COLOR_STRONG.color(), b.COLOR_STRONG.color());
		assert colorsAreEqual(a.COLOR_STRONGEST.color(), b.COLOR_STRONGEST.color());
		assert colorsAreEqual(a.TEXT_COLOR_NEUTRAL.color(), b.TEXT_COLOR_NEUTRAL.color());
		assert colorsAreEqual(a.TEXT_COLOR_SLIGHTLY_STRONG.color(), b.TEXT_COLOR_SLIGHTLY_STRONG.color());
		assert colorsAreEqual(a.TEXT_COLOR_STRONG.color(), b.TEXT_COLOR_STRONG.color());
		assert colorsAreEqual(a.TEXT_COLOR_WEAK.color(), b.TEXT_COLOR_WEAK.color());
		assert colorsAreEqual(a.ICON_COLOR.color(), b.ICON_COLOR.color());
	}

	private boolean colorsAreEqual(Color a, Color b) {
		return a.getRGB() == b.getRGB();
	}

}
