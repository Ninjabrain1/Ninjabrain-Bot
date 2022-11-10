package ninjabrainbot.gui.style;

import java.awt.Color;
import java.util.ArrayList;

public class CustomTheme extends Theme {

	private ArrayList<ConfigurableColor> configurableColors = new ArrayList<ConfigurableColor>();

	public CustomTheme() {
		super("Dark");
		configurableColors = new ArrayList<ConfigurableColor>();

		COLOR_STRONGEST = createColor(Color.decode("#1C1C27"), "Title bar", "a");
		COLOR_STRONG = createColor(Color.decode("#252538"), "Header background", "b");
		COLOR_SLIGHTLY_WEAK = createColor(Color.decode("#2B2D44"), "Result background", "c");
		COLOR_NEUTRAL = createColor(Color.decode("#28293D"), "Throws background", "d");
		COLOR_DIVIDER = createColor(Color.decode("#212130"), "Dividers", "e");
		COLOR_DIVIDER_DARK = createColor(Color.decode("#1C1C27"), "Header Dividers", "f");
		COLOR_EXIT_BUTTON_HOVER = createColor(Color.decode("#F04747"), "Exit button hover", "g");
		TEXT_COLOR_STRONG = createColor(Color.WHITE, "Text", "h");
		TEXT_COLOR_SLIGHTLY_STRONG = createColor(Color.decode("#E5E5E5"), "Divine text", "i");
		TEXT_COLOR_WEAK = createColor(Color.GRAY, "Version text", "j");
		TEXT_COLOR_NEUTRAL = createColor(Color.LIGHT_GRAY, "Throws text", "k");
		COLOR_POSITIVE = createColor(Color.decode("#75CC6C"), "Subpixel +", "l");
		COLOR_NEGATIVE = createColor(Color.decode("#CC6E72"), "Subpixel -", "m");
		
		COLOR_SLIGHTLY_STRONG = createColor(Color.decode("#27273D"), COLOR_STRONG);
		COLOR_SATURATED = createColor(Color.decode("#57EBA3"), COLOR_STRONG);

		CERTAINTY_COLOR_MAP = createColorMap(new ColorMap(Color.RED, Color.YELLOW, Color.decode("#00CE29")));
		BLACK_ICONS = createBoolean(false);
	}
	
	public void setFromTheme(Theme theme) {
		COLOR_STRONGEST.set(theme.COLOR_STRONGEST);
		COLOR_STRONG.set(theme.COLOR_STRONG);
		COLOR_SLIGHTLY_WEAK.set(theme.COLOR_SLIGHTLY_WEAK);
		COLOR_NEUTRAL.set(theme.COLOR_NEUTRAL);
		COLOR_DIVIDER.set(theme.COLOR_DIVIDER);
		COLOR_DIVIDER_DARK.set(theme.COLOR_DIVIDER_DARK);
		COLOR_EXIT_BUTTON_HOVER.set(theme.COLOR_EXIT_BUTTON_HOVER);
		TEXT_COLOR_STRONG.set(theme.TEXT_COLOR_STRONG);
		TEXT_COLOR_SLIGHTLY_STRONG.set(theme.TEXT_COLOR_SLIGHTLY_STRONG);
		TEXT_COLOR_WEAK.set(theme.TEXT_COLOR_WEAK);
		TEXT_COLOR_NEUTRAL.set(theme.TEXT_COLOR_NEUTRAL);
		COLOR_POSITIVE.set(theme.COLOR_POSITIVE);
		COLOR_NEGATIVE.set(theme.COLOR_NEGATIVE);

		BLACK_ICONS.set(theme.BLACK_ICONS);
		CERTAINTY_COLOR_MAP.set(theme.CERTAINTY_COLOR_MAP);
	}

	public ArrayList<ConfigurableColor> getConfigurableColors() {
		return configurableColors;
	}

	protected WrappedColor createColor(Color color, WrappedColor parent) {
		WrappedColor wc = super.createColor(color);
		int dr = color.getRed() - parent.color().getRed();
		int dg = color.getGreen() - parent.color().getGreen();
		int db = color.getBlue() - parent.color().getBlue();
		parent.whenColorChanged().subscribe(c -> wc.set(new Color(clamp(c.getRed() + dr), clamp(c.getGreen() + dg), clamp(c.getBlue() + db))));
		return wc;
	}
	
	private static int clamp(int i) {
		if (i < 0)
			return 0;
		if (i > 255)
			return 255;
		return i;
	}

	protected WrappedColor createColor(Color color, String name, String uid) {
		assert uid.length() == 1;
		assert !configurableColors.stream().anyMatch(cc -> cc.uid == uid);
		WrappedColor wc = super.createColor(color);
		configurableColors.add(new ConfigurableColor(wc, name, uid));
		return wc;
	}

}