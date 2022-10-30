package ninjabrainbot.gui.style;

import java.awt.Color;
import java.util.ArrayList;

public class CustomTheme extends Theme {

	private ArrayList<ConfigurableColor> configurableColors = new ArrayList<ConfigurableColor>();
	
	public CustomTheme() {
		super("Custom");
		configurableColors = new ArrayList<ConfigurableColor>();
		
		COLOR_STRONGEST = createColor(Color.decode("#1C1C27"));
		COLOR_STRONGER = createColor(Color.decode("#212130"));
		COLOR_STRONG = createColor(Color.decode("#252538"));
		COLOR_SLIGHTLY_STRONG = createColor(Color.decode("#27273D"));
		COLOR_NEUTRAL = createColor(Color.decode("#28293D"));
		COLOR_SLIGHTLY_WEAK = createColor(Color.decode("#2B2D44"));
		COLOR_EXIT_BUTTON_HOVER = createColor(Color.decode("#F04747"));
		TEXT_COLOR_STRONG = createColor(Color.WHITE);
		TEXT_COLOR_SLIGHTLY_STRONG = createColor(Color.decode("#E5E5E5"));
		TEXT_COLOR_WEAK = createColor(Color.GRAY);
		TEXT_COLOR_NEUTRAL = createColor(Color.LIGHT_GRAY);
		COLOR_REMOVE_BUTTON_HOVER = createColor(Color.decode("#F04747"));
		COLOR_SATURATED = createColor(Color.decode("#57EBA3"));
		COLOR_POSITIVE = createColor(Color.decode("#75CC6C"));
		COLOR_NEGATIVE = createColor(Color.decode("#CC6E72"));

		CERTAINTY_COLOR_MAP = createColorMap(new ColorMap(Color.RED, Color.YELLOW, Color.decode("#00CE29")));
		BLACK_ICONS = createBoolean(false);
	}

	@Override
	protected WrappedColor createColor(Color color) {
		return createColor(color, "Unnamed color");
	}

	protected WrappedColor createColor(Color color, String name) {
		WrappedColor wc = super.createColor(color);
		configurableColors.add(new ConfigurableColor(wc, name));
		return wc;
	}

}