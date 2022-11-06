package ninjabrainbot.gui.style;

import java.awt.Color;
import java.util.ArrayList;

public class CustomTheme extends Theme {

	private ArrayList<ConfigurableColor> configurableColors = new ArrayList<ConfigurableColor>();

	public CustomTheme() {
		super("Custom");
		configurableColors = new ArrayList<ConfigurableColor>();

		COLOR_STRONGEST = createColor(Color.decode("#1C1C27"), "Title bar");
		COLOR_STRONG = createColor(Color.decode("#252538"), "Header background");
		COLOR_SLIGHTLY_WEAK = createColor(Color.decode("#2B2D44"), "Result background");
		COLOR_NEUTRAL = createColor(Color.decode("#28293D"), "Throws background");
		COLOR_DIVIDER = createColor(Color.decode("#212130"), "Dividers");
		COLOR_DIVIDER_DARK = createColor(Color.decode("#1C1C27"), "Header Dividers");
		COLOR_SLIGHTLY_STRONG = createColor(Color.decode("#27273D"), COLOR_STRONG);
		COLOR_EXIT_BUTTON_HOVER = createColor(Color.decode("#F04747"), "Exit button hover");
		TEXT_COLOR_STRONG = createColor(Color.WHITE, "Text");
		TEXT_COLOR_SLIGHTLY_STRONG = createColor(Color.decode("#E5E5E5"), "Divine text");
		TEXT_COLOR_WEAK = createColor(Color.GRAY, "Version text");
		TEXT_COLOR_NEUTRAL = createColor(Color.LIGHT_GRAY, "Throws text");
		COLOR_SATURATED = createColor(Color.decode("#57EBA3"));
		COLOR_POSITIVE = createColor(Color.decode("#75CC6C"), "Subpixel +");
		COLOR_NEGATIVE = createColor(Color.decode("#CC6E72"), "Subpixel -");

		CERTAINTY_COLOR_MAP = createColorMap(new ColorMap(Color.RED, Color.YELLOW, Color.decode("#00CE29")));
		BLACK_ICONS = createBoolean(false);
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

	protected WrappedColor createColor(Color color, String name) {
		WrappedColor wc = super.createColor(color);
		configurableColors.add(new ConfigurableColor(wc, name));
		return wc;
	}

}