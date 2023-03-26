package ninjabrainbot.gui.style.theme;

import java.awt.Color;

import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.io.preferences.SavedPreferences;

public class StandardThemes {

	// @formatter:off
	static final String template =
			"class %sTheme extends Theme {\r\n"
					+ "\r\n"
					+ "public static final int UID = ;\r\n"
					+ "\r\n"
					+ "	public %sTheme() {\r\n"
					+ "		super(\"%s\", UID);\r\n"
					+ "	}\r\n"
					+ "\r\n"
					+ "	@Override\r\n"
					+ "	protected void loadTheme() {\r\n"
					+ "		loaded = true;\r\n"
					+ "\r\n"
					+ " %s"
					+ "	}\r\n"
					+ "}";
	// @formatter:on

	public static void main(String[] args) {
		NinjabrainBotPreferences preferences = new NinjabrainBotPreferences(new SavedPreferences());
		Theme.loadThemes(preferences);

		for (CustomTheme theme : Theme.getCustomThemes()) {
			String name = theme.name.get();
			String nameNoSpace = name.replaceAll(" ", "");
			StringBuilder colorsStringBuilder = new StringBuilder();
			colorsStringBuilder.append(String.format("		COLOR_NEUTRAL = createColor(Color.decode(\"%s\"));\r\n", theme.COLOR_NEUTRAL.hex()));
			colorsStringBuilder.append(String.format("		COLOR_STRONGEST = createColor(Color.decode(\"%s\"));\r\n", theme.COLOR_STRONGEST.hex()));
			colorsStringBuilder.append(String.format("		COLOR_EXIT_BUTTON_HOVER = createColor(Color.decode(\"%s\"));\r\n", theme.COLOR_EXIT_BUTTON_HOVER.hex()));
			colorsStringBuilder.append(String.format("		COLOR_DIVIDER = createColor(Color.decode(\"%s\"));\r\n", theme.COLOR_DIVIDER.hex()));
			colorsStringBuilder.append(String.format("		COLOR_DIVIDER_DARK = createColor(Color.decode(\"%s\"));\r\n", theme.COLOR_DIVIDER_DARK.hex()));
			colorsStringBuilder.append(String.format("		COLOR_SLIGHTLY_STRONG = createColor(Color.decode(\"%s\"));\r\n", theme.COLOR_SLIGHTLY_STRONG.hex()));
			colorsStringBuilder.append(String.format("		COLOR_SLIGHTLY_WEAK = createColor(Color.decode(\"%s\"));\r\n", theme.COLOR_SLIGHTLY_WEAK.hex()));
			colorsStringBuilder.append(String.format("		TEXT_COLOR_SLIGHTLY_WEAK = createColor(Color.decode(\"%s\"));\r\n", theme.TEXT_COLOR_SLIGHTLY_WEAK.hex()));
			colorsStringBuilder.append(String.format("		TEXT_COLOR_SLIGHTLY_STRONG = createColor(Color.decode(\"%s\"));\r\n", theme.TEXT_COLOR_SLIGHTLY_STRONG.hex()));
			colorsStringBuilder.append(String.format("		TEXT_COLOR_WEAK = createColor(Color.decode(\"%s\"));\r\n", theme.TEXT_COLOR_WEAK.hex()));
			colorsStringBuilder.append(String.format("		TEXT_COLOR_NEUTRAL = createColor(Color.decode(\"%s\"));\r\n", theme.TEXT_COLOR_NEUTRAL.hex()));
			colorsStringBuilder.append(String.format("		TEXT_COLOR_HEADER = createColor(Color.decode(\"%s\"));\r\n", theme.TEXT_COLOR_HEADER.hex()));
			colorsStringBuilder.append(String.format("		COLOR_STRONG = createColor(Color.decode(\"%s\"));\r\n", theme.COLOR_STRONG.hex()));
			colorsStringBuilder.append(String.format("		COLOR_SATURATED = createColor(Color.decode(\"%s\"));\r\n", theme.COLOR_SATURATED.hex()));
			colorsStringBuilder.append(String.format("		COLOR_POSITIVE = createColor(Color.decode(\"%s\"));\r\n", theme.COLOR_POSITIVE.hex()));
			colorsStringBuilder.append(String.format("		COLOR_NEGATIVE = createColor(Color.decode(\"%s\"));\r\n", theme.COLOR_NEGATIVE.hex()));
			colorsStringBuilder.append(String.format("		TEXT_COLOR_TITLE = createColor(Color.decode(\"%s\"));\r\n", theme.TEXT_COLOR_TITLE.hex()));
			colorsStringBuilder.append(String.format("		COLOR_GRADIENT_0 = createColor(Color.decode(\"%s\"));\r\n", theme.COLOR_GRADIENT_0.hex()));
			colorsStringBuilder.append(String.format("		COLOR_GRADIENT_50 = createColor(Color.decode(\"%s\"));\r\n", theme.COLOR_GRADIENT_50.hex()));
			colorsStringBuilder.append(String.format("		COLOR_GRADIENT_100 = createColor(Color.decode(\"%s\"));\r\n", theme.COLOR_GRADIENT_100.hex()));
			System.out.println(String.format(template, nameNoSpace, nameNoSpace, name, colorsStringBuilder.toString()));
		}
	}

}

class DarkTheme extends Theme {

	public static final int UID = 1;

	public DarkTheme() {
		super("Dark", UID);
	}

	@Override
	protected void loadTheme() {
		COLOR_NEUTRAL = createColor(Color.decode("#33383D"));
		COLOR_STRONGEST = createColor(Color.decode("#212529"));
		COLOR_EXIT_BUTTON_HOVER = createColor(Color.decode("#F04747"));
		COLOR_DIVIDER = createColor(Color.decode("#2A2E32"));
		COLOR_DIVIDER_DARK = createColor(Color.decode("#212529"));
		COLOR_SLIGHTLY_STRONG = createColor(Color.decode("#31353A"));
		COLOR_SLIGHTLY_WEAK = createColor(Color.decode("#373C42"));
		TEXT_COLOR_SLIGHTLY_WEAK = createColor(Color.WHITE);
		TEXT_COLOR_SLIGHTLY_STRONG = createColor(Color.decode("#E5E5E5"));
		TEXT_COLOR_WEAK = createColor(Color.GRAY);
		TEXT_COLOR_NEUTRAL = createColor(Color.LIGHT_GRAY);
		TEXT_COLOR_HEADER = createColor(Color.decode("#E5E5E5"));
		COLOR_STRONG = createColor(Color.decode("#2D3238"));
		COLOR_SATURATED = createColor(Color.decode("#57EBA3"));
		COLOR_POSITIVE = createColor(Color.decode("#75CC6C"));
		COLOR_NEGATIVE = createColor(Color.decode("#CC6E72"));
		TEXT_COLOR_TITLE = createColor(Color.WHITE);

		COLOR_GRADIENT_0 = createColor(Color.RED);
		COLOR_GRADIENT_50 = createColor(Color.YELLOW);
		COLOR_GRADIENT_100 = createColor(Color.decode("#00CE29"));
	}
}

class BlueTheme extends Theme {

	public static final int UID = 2;

	public BlueTheme() {
		super("Blue", UID);
	}

	@Override
	protected void loadTheme() {
		COLOR_STRONGEST = createColor(Color.decode("#1C1C27"));
		COLOR_DIVIDER = createColor(Color.decode("#212130"));
		COLOR_DIVIDER_DARK = createColor(Color.decode("#1C1C27"));
		COLOR_STRONG = createColor(Color.decode("#252538"));
		COLOR_SLIGHTLY_STRONG = createColor(Color.decode("#27273D"));
		COLOR_NEUTRAL = createColor(Color.decode("#28293D"));
		COLOR_SLIGHTLY_WEAK = createColor(Color.decode("#2B2D44"));
		COLOR_EXIT_BUTTON_HOVER = createColor(Color.decode("#F04747"));
		TEXT_COLOR_SLIGHTLY_WEAK = createColor(Color.WHITE);
		TEXT_COLOR_SLIGHTLY_STRONG = createColor(Color.decode("#E5E5E5"));
		TEXT_COLOR_WEAK = createColor(Color.GRAY);
		TEXT_COLOR_NEUTRAL = createColor(Color.LIGHT_GRAY);
		TEXT_COLOR_HEADER = createColor(Color.decode("#E5E5E5"));
		COLOR_SATURATED = createColor(Color.decode("#57EBA3"));
		COLOR_POSITIVE = createColor(Color.decode("#75CC6C"));
		COLOR_NEGATIVE = createColor(Color.decode("#CC6E72"));
		TEXT_COLOR_TITLE = createColor(Color.WHITE);

		COLOR_GRADIENT_0 = createColor(Color.RED);
		COLOR_GRADIENT_50 = createColor(Color.YELLOW);
		COLOR_GRADIENT_100 = createColor(Color.decode("#00CE29"));
	}
}

class LightTheme extends Theme {

	public static final int UID = 3;

	public LightTheme() {
		super("Light", UID);
	}

	@Override
	protected void loadTheme() {
		loaded = true;

		COLOR_NEUTRAL = createColor(Color.decode("#F5F5F5"));
		COLOR_DIVIDER = createColor(Color.decode("#D8D8D8"));
		COLOR_DIVIDER_DARK = createColor(Color.decode("#C1C1C1"));
		COLOR_EXIT_BUTTON_HOVER = createColor(Color.decode("#F04747"));
		COLOR_STRONGEST = createColor(Color.decode("#C1C1C1"));
		COLOR_SLIGHTLY_STRONG = createColor(Color.decode("#EFEFEF"));
		COLOR_SLIGHTLY_WEAK = createColor(Color.decode("#F9F9F9"));
		TEXT_COLOR_SLIGHTLY_WEAK = createColor(Color.BLACK);
		TEXT_COLOR_SLIGHTLY_STRONG = createColor(Color.decode("#191919"));
		TEXT_COLOR_WEAK = createColor(Color.decode("#888888"));
		TEXT_COLOR_NEUTRAL = createColor(Color.DARK_GRAY);
		TEXT_COLOR_HEADER = createColor(Color.decode("#191919"));
		COLOR_STRONG = createColor(Color.decode("#E5E5E5"));
		COLOR_SATURATED = createColor(Color.decode("#BAD7EF"));
		COLOR_POSITIVE = createColor(Color.decode("#1E9910"));
		COLOR_NEGATIVE = createColor(Color.decode("#991017"));
		TEXT_COLOR_TITLE = createColor(Color.decode("#373737"));

		COLOR_GRADIENT_0 = createColor(Color.RED);
		COLOR_GRADIENT_50 = createColor(Color.YELLOW);
		COLOR_GRADIENT_100 = createColor(Color.decode("#00CE29"));
	}
}

class BastionTheme extends Theme {

	public static final int UID = 4;

	public BastionTheme() {
		super("Bastion", UID);
	}

	@Override
	protected void loadTheme() {
		loaded = true;

		COLOR_NEUTRAL = createColor(Color.decode("#312F3B"));
		COLOR_STRONGEST = createColor(Color.decode("#191413"));
		COLOR_EXIT_BUTTON_HOVER = createColor(Color.decode("#F04747"));
		COLOR_DIVIDER = createColor(Color.decode("#251E1F"));
		COLOR_DIVIDER_DARK = createColor(Color.decode("#1B1E21"));
		COLOR_SLIGHTLY_STRONG = createColor(Color.decode("#2C272E"));
		COLOR_SLIGHTLY_WEAK = createColor(Color.decode("#39353E"));
		TEXT_COLOR_SLIGHTLY_WEAK = createColor(Color.decode("#FFE189"));
		TEXT_COLOR_SLIGHTLY_STRONG = createColor(Color.decode("#E5E5E5"));
		TEXT_COLOR_WEAK = createColor(Color.decode("#AE804A"));
		TEXT_COLOR_NEUTRAL = createColor(Color.decode("#B3770C"));
		TEXT_COLOR_HEADER = createColor(Color.decode("#D88F11"));
		COLOR_STRONG = createColor(Color.decode("#28242C"));
		COLOR_SATURATED = createColor(Color.decode("#52DD97"));
		COLOR_POSITIVE = createColor(Color.decode("#75CC6C"));
		COLOR_NEGATIVE = createColor(Color.decode("#CC6E72"));
		TEXT_COLOR_TITLE = createColor(Color.decode("#FFA700"));
		COLOR_GRADIENT_0 = createColor(Color.decode("#FF0000"));
		COLOR_GRADIENT_50 = createColor(Color.decode("#FFFF00"));
		COLOR_GRADIENT_100 = createColor(Color.decode("#00CE29"));
	}
}

class NetherbrickTheme extends Theme {

	public static final int UID = 5;

	public NetherbrickTheme() {
		super("Nether brick", UID);
	}

	@Override
	protected void loadTheme() {
		loaded = true;

		COLOR_NEUTRAL = createColor(Color.decode("#32181D"));
		COLOR_STRONGEST = createColor(Color.decode("#201214"));
		COLOR_EXIT_BUTTON_HOVER = createColor(Color.decode("#F04747"));
		COLOR_DIVIDER = createColor(Color.decode("#261318"));
		COLOR_DIVIDER_DARK = createColor(Color.decode("#120A0D"));
		COLOR_SLIGHTLY_STRONG = createColor(Color.decode("#301619"));
		COLOR_SLIGHTLY_WEAK = createColor(Color.decode("#381C21"));
		TEXT_COLOR_SLIGHTLY_WEAK = createColor(Color.decode("#FFE69C"));
		TEXT_COLOR_SLIGHTLY_STRONG = createColor(Color.decode("#FFE097"));
		TEXT_COLOR_WEAK = createColor(Color.decode("#A38746"));
		TEXT_COLOR_NEUTRAL = createColor(Color.decode("#FFDF80"));
		TEXT_COLOR_HEADER = createColor(Color.decode("#FFDA7A"));
		COLOR_STRONG = createColor(Color.decode("#2C1317"));
		COLOR_SATURATED = createColor(Color.decode("#56CC82"));
		COLOR_POSITIVE = createColor(Color.decode("#75CC6C"));
		COLOR_NEGATIVE = createColor(Color.decode("#CC6E72"));
		TEXT_COLOR_TITLE = createColor(Color.decode("#FFC54A"));
		COLOR_GRADIENT_0 = createColor(Color.decode("#FF0000"));
		COLOR_GRADIENT_50 = createColor(Color.decode("#FFFF00"));
		COLOR_GRADIENT_100 = createColor(Color.decode("#00CE29"));
	}
}

class BambooTheme extends Theme {

	public static final int UID = 6;

	public BambooTheme() {
		super("Bamboo", UID);
	}

	@Override
	protected void loadTheme() {
		loaded = true;

		COLOR_NEUTRAL = createColor(Color.decode("#F0FFE0"));
		COLOR_STRONGEST = createColor(Color.decode("#364D1D"));
		COLOR_EXIT_BUTTON_HOVER = createColor(Color.decode("#F04747"));
		COLOR_DIVIDER = createColor(Color.decode("#2D4514"));
		COLOR_DIVIDER_DARK = createColor(Color.decode("#20300F"));
		COLOR_SLIGHTLY_STRONG = createColor(Color.decode("#688B3A"));
		COLOR_SLIGHTLY_WEAK = createColor(Color.decode("#F9FFF2"));
		TEXT_COLOR_SLIGHTLY_WEAK = createColor(Color.decode("#314E17"));
		TEXT_COLOR_SLIGHTLY_STRONG = createColor(Color.decode("#FFFFFF"));
		TEXT_COLOR_WEAK = createColor(Color.decode("#B0C797"));
		TEXT_COLOR_NEUTRAL = createColor(Color.decode("#314E17"));
		TEXT_COLOR_HEADER = createColor(Color.decode("#FFFFFF"));
		COLOR_STRONG = createColor(Color.decode("#648838"));
		COLOR_SATURATED = createColor(Color.decode("#8EFFA3"));
		COLOR_POSITIVE = createColor(Color.decode("#1E9910"));
		COLOR_NEGATIVE = createColor(Color.decode("#991017"));
		TEXT_COLOR_TITLE = createColor(Color.decode("#FFFFFF"));
		COLOR_GRADIENT_0 = createColor(Color.decode("#FF0000"));
		COLOR_GRADIENT_50 = createColor(Color.decode("#9C9C00"));
		COLOR_GRADIENT_100 = createColor(Color.decode("#00CE29"));
	}
}

class NinjabrainTheme extends Theme {

	public static final int UID = 7;

	public NinjabrainTheme() {
		super("Ninjabrain", UID);
	}

	@Override
	protected void loadTheme() {
		loaded = true;

		COLOR_NEUTRAL = createColor(Color.decode("#FFF4F4"));
		COLOR_STRONGEST = createColor(Color.decode("#232323"));
		COLOR_EXIT_BUTTON_HOVER = createColor(Color.decode("#F04747"));
		COLOR_DIVIDER = createColor(Color.decode("#C5B6B6"));
		COLOR_DIVIDER_DARK = createColor(Color.decode("#343434"));
		COLOR_SLIGHTLY_STRONG = createColor(Color.decode("#FFD7DE"));
		COLOR_SLIGHTLY_WEAK = createColor(Color.decode("#FFFAFA"));
		TEXT_COLOR_SLIGHTLY_WEAK = createColor(Color.decode("#000000"));
		TEXT_COLOR_SLIGHTLY_STRONG = createColor(Color.decode("#191919"));
		TEXT_COLOR_WEAK = createColor(Color.decode("#888888"));
		TEXT_COLOR_NEUTRAL = createColor(Color.decode("#404040"));
		TEXT_COLOR_HEADER = createColor(Color.decode("#191919"));
		COLOR_STRONG = createColor(Color.decode("#FFD4DC"));
		COLOR_SATURATED = createColor(Color.decode("#FFFFFF"));
		COLOR_POSITIVE = createColor(Color.decode("#1E9910"));
		COLOR_NEGATIVE = createColor(Color.decode("#991017"));
		TEXT_COLOR_TITLE = createColor(Color.decode("#FFFFFF"));
		COLOR_GRADIENT_0 = createColor(Color.decode("#FF0000"));
		COLOR_GRADIENT_50 = createColor(Color.decode("#7E7E00"));
		COLOR_GRADIENT_100 = createColor(Color.decode("#00CE29"));
	}
}

class CouriwayTheme extends Theme {

	public static final int UID = 8;

	public CouriwayTheme() {
		super("Couriway", UID);
	}

	@Override
	protected void loadTheme() {
		loaded = true;

		COLOR_NEUTRAL = createColor(Color.decode("#40324E"));
		COLOR_STRONGEST = createColor(Color.decode("#322045"));
		COLOR_EXIT_BUTTON_HOVER = createColor(Color.decode("#F04747"));
		COLOR_DIVIDER = createColor(Color.decode("#191122"));
		COLOR_DIVIDER_DARK = createColor(Color.decode("#502568"));
		COLOR_SLIGHTLY_STRONG = createColor(Color.decode("#FFBB3A"));
		COLOR_SLIGHTLY_WEAK = createColor(Color.decode("#543F66"));
		TEXT_COLOR_SLIGHTLY_WEAK = createColor(Color.decode("#FFFFFF"));
		TEXT_COLOR_SLIGHTLY_STRONG = createColor(Color.decode("#E5E5E5"));
		TEXT_COLOR_WEAK = createColor(Color.decode("#A88EC1"));
		TEXT_COLOR_NEUTRAL = createColor(Color.decode("#FFFFFF"));
		TEXT_COLOR_HEADER = createColor(Color.decode("#2E1F36"));
		COLOR_STRONG = createColor(Color.decode("#FDB838"));
		COLOR_SATURATED = createColor(Color.decode("#FFFFA3"));
		COLOR_POSITIVE = createColor(Color.decode("#75CC6C"));
		COLOR_NEGATIVE = createColor(Color.decode("#CC6E72"));
		TEXT_COLOR_TITLE = createColor(Color.decode("#FFFFFF"));
		COLOR_GRADIENT_0 = createColor(Color.decode("#FF0000"));
		COLOR_GRADIENT_50 = createColor(Color.decode("#FFFF00"));
		COLOR_GRADIENT_100 = createColor(Color.decode("#00CE29"));
	}
}

class FeinbergTheme extends Theme {

	public static final int UID = 9;

	public FeinbergTheme() {
		super("Feinberg", UID);
	}

	@Override
	protected void loadTheme() {
		loaded = true;

		COLOR_NEUTRAL = createColor(Color.decode("#40FFFF"));
		COLOR_STRONGEST = createColor(Color.decode("#2F2626"));
		COLOR_EXIT_BUTTON_HOVER = createColor(Color.decode("#F04747"));
		COLOR_DIVIDER = createColor(Color.decode("#7D6E6E"));
		COLOR_DIVIDER_DARK = createColor(Color.decode("#2C0B21"));
		COLOR_SLIGHTLY_STRONG = createColor(Color.decode("#FD52C0"));
		COLOR_SLIGHTLY_WEAK = createColor(Color.decode("#DCFFFF"));
		TEXT_COLOR_SLIGHTLY_WEAK = createColor(Color.decode("#3B0023"));
		TEXT_COLOR_SLIGHTLY_STRONG = createColor(Color.decode("#000000"));
		TEXT_COLOR_WEAK = createColor(Color.decode("#B5A9B6"));
		TEXT_COLOR_NEUTRAL = createColor(Color.decode("#2F001C"));
		TEXT_COLOR_HEADER = createColor(Color.decode("#342735"));
		COLOR_STRONG = createColor(Color.decode("#F94FBE"));
		COLOR_SATURATED = createColor(Color.decode("#FFFFFF"));
		COLOR_POSITIVE = createColor(Color.decode("#75CC6C"));
		COLOR_NEGATIVE = createColor(Color.decode("#CC6E72"));
		TEXT_COLOR_TITLE = createColor(Color.decode("#FFFFFF"));
		COLOR_GRADIENT_0 = createColor(Color.decode("#FF0600"));
		COLOR_GRADIENT_50 = createColor(Color.decode("#939300"));
		COLOR_GRADIENT_100 = createColor(Color.decode("#00CE29"));
	}
}

class DarklavenderTheme extends Theme {

	public static final int UID = 10;

	public DarklavenderTheme() {
		super("Dark lavender", UID);
	}

	@Override
	protected void loadTheme() {
		loaded = true;

		COLOR_NEUTRAL = createColor(Color.decode("#474767"));
		COLOR_STRONGEST = createColor(Color.decode("#3F3E5F"));
		COLOR_EXIT_BUTTON_HOVER = createColor(Color.decode("#F04747"));
		COLOR_DIVIDER = createColor(Color.decode("#3E3862"));
		COLOR_DIVIDER_DARK = createColor(Color.decode("#34324A"));
		COLOR_SLIGHTLY_STRONG = createColor(Color.decode("#585781"));
		COLOR_SLIGHTLY_WEAK = createColor(Color.decode("#676297"));
		TEXT_COLOR_SLIGHTLY_WEAK = createColor(Color.decode("#FFFFFF"));
		TEXT_COLOR_SLIGHTLY_STRONG = createColor(Color.decode("#E5E5E5"));
		TEXT_COLOR_WEAK = createColor(Color.decode("#AAAAF2"));
		TEXT_COLOR_NEUTRAL = createColor(Color.decode("#C6C6FF"));
		TEXT_COLOR_HEADER = createColor(Color.decode("#E5E5E5"));
		COLOR_STRONG = createColor(Color.decode("#54547F"));
		COLOR_SATURATED = createColor(Color.decode("#7EFFEA"));
		COLOR_POSITIVE = createColor(Color.decode("#51E042"));
		COLOR_NEGATIVE = createColor(Color.decode("#EB4E55"));
		TEXT_COLOR_TITLE = createColor(Color.decode("#F9B4FF"));
		COLOR_GRADIENT_0 = createColor(Color.decode("#FF0000"));
		COLOR_GRADIENT_50 = createColor(Color.decode("#FFFF00"));
		COLOR_GRADIENT_100 = createColor(Color.decode("#00CE29"));
	}
}
