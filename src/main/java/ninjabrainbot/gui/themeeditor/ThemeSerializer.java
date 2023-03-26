package ninjabrainbot.gui.themeeditor;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import ninjabrainbot.gui.style.theme.ConfigurableColor;
import ninjabrainbot.gui.style.theme.CustomTheme;

public class ThemeSerializer {

	private static int FILTER_6BIT = 0b111111;
	private static int SHIFT_SIZE = 6;

	public static String serialize(CustomTheme theme) {
		ArrayList<ConfigurableColor> configurableColors = theme.getConfigurableColors();
		StringBuilder sb = new StringBuilder();
		for (ConfigurableColor cc : configurableColors) {
			sb.append(cc.uid);
			sb.append(serializeColor(cc.color.color()));
		}
		return sb.toString();
	}

	public static CustomTheme deserialize(String s) {
		CustomTheme theme = new CustomTheme();
		ArrayList<ConfigurableColor> configurableColors = theme.getConfigurableColors();
		ParseResult pr = parseThemeString_v1_4_0(s);
		if (!pr.success)
			return null;
		for (int i = 0; i < pr.colors.size(); i++) {
			Color c = pr.colors.get(i);
			char tag = pr.tags.get(i);
			String uid = String.valueOf(tag);
			Optional<ConfigurableColor> optionalCC = configurableColors.stream().filter(cc -> cc.uid.contentEquals(uid)).findFirst();
			if (!optionalCC.isPresent())
				continue;
			ConfigurableColor cc = optionalCC.get();
			cc.color.set(c);
		}
		return theme;
	}

	private static ParseResult parseThemeString_v1_4_0(String string) {
		if (string.length() % 5 != 0 || string.length() == 0)
			return new ParseResult();
		ArrayList<Character> tags = new ArrayList<>();
		ArrayList<Color> colors = new ArrayList<>();
		for (int i = 0; i < string.length(); i += 5) {
			char tag = string.charAt(i);
			if (tags.contains(tag))
				return new ParseResult();
			tags.add(tag);

			String colorString = string.substring(i + 1, i + 5);
			try {
				colors.add(deserializeColor(colorString));
			} catch (IllegalArgumentException e) {
				return new ParseResult();
			}
		}
		return new ParseResult(tags, colors);
	}

	public static String serializeColor(Color c) {
		return serializeInt(c.getRGB(), 24);
	}

	public static Color deserializeColor(String s) throws IllegalArgumentException {
		if (s.length() != 4)
			return null;
		return new Color(deserializeInt(s));
	}

	private static String serializeInt(int i, int bits) {
		String result = "";
		while (bits > 0) {
			result = toChar(i & FILTER_6BIT) + result;
			i = i >> SHIFT_SIZE;
			bits -= SHIFT_SIZE;
		}
		return result;
	}

	private static int deserializeInt(String s) throws IllegalArgumentException {
		int result = 0;
		for (int i = 0; i < s.length(); i++) {
			result = result << SHIFT_SIZE;
			result += toInt(s.charAt(i));
		}
		return result;
	}

	private static String toChar(int i) {
		assert i >= 0 && i < 64;
		return String.valueOf((char) (i + 48));
	}

	private static int toInt(char c) throws IllegalArgumentException {
		int value = c - 48;
		if (value < 0 || value >= 64)
			throw new IllegalArgumentException();
		return value;
	}

}

class ParseResult {

	boolean success;
	List<Character> tags;
	List<Color> colors;

	public ParseResult() {
		success = false;
	}

	public ParseResult(List<Character> tags, List<Color> colors) {
		success = true;
		this.tags = tags;
		this.colors = colors;
	}

}