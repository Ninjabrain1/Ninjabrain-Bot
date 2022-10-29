package ninjabrainbot.gui.style;

import ninjabrainbot.util.Wrapper;

public class CurrentTheme {
	
	public final ConfigurableColor COLOR_STRONGEST = new ConfigurableColor();
	public final ConfigurableColor COLOR_STRONGER = new ConfigurableColor();
	public final ConfigurableColor COLOR_STRONG = new ConfigurableColor();
	public final ConfigurableColor COLOR_SLIGHTLY_STRONG = new ConfigurableColor();
	public final ConfigurableColor COLOR_NEUTRAL = new ConfigurableColor();
	public final ConfigurableColor COLOR_SLIGHTLY_WEAK = new ConfigurableColor();
	public final ConfigurableColor COLOR_EXIT_BUTTON_HOVER = new ConfigurableColor();
	public final ConfigurableColor COLOR_REMOVE_BUTTON_HOVER = new ConfigurableColor();
	public final ConfigurableColor TEXT_COLOR_STRONG = new ConfigurableColor();
	public final ConfigurableColor TEXT_COLOR_SLIGHTLY_STRONG = new ConfigurableColor();
	public final ConfigurableColor TEXT_COLOR_NEUTRAL = new ConfigurableColor();
	public final ConfigurableColor TEXT_COLOR_WEAK = new ConfigurableColor();
	public final ConfigurableColor COLOR_SATURATED = new ConfigurableColor();
	public final ConfigurableColor COLOR_POSITIVE = new ConfigurableColor();
	public final ConfigurableColor COLOR_NEGATIVE = new ConfigurableColor();
	
	public final Wrapper<Boolean> BLACK_ICONS = new Wrapper<>();
	public final Wrapper<ColorMap> CERTAINTY_COLOR_MAP = new Wrapper<>();
	
	public void setTheme(Theme theme) {
		COLOR_STRONGEST.set(theme.COLOR_STRONGEST);
		COLOR_STRONGER.set(theme.COLOR_STRONGER);
		COLOR_STRONG.set(theme.COLOR_STRONG);
		COLOR_SLIGHTLY_STRONG.set(theme.COLOR_SLIGHTLY_STRONG);
		COLOR_NEUTRAL.set(theme.COLOR_NEUTRAL);
		COLOR_SLIGHTLY_WEAK.set(theme.COLOR_SLIGHTLY_WEAK);
		COLOR_EXIT_BUTTON_HOVER.set(theme.COLOR_EXIT_BUTTON_HOVER);
		COLOR_REMOVE_BUTTON_HOVER.set(theme.COLOR_REMOVE_BUTTON_HOVER);
		TEXT_COLOR_STRONG.set(theme.TEXT_COLOR_STRONG);
		TEXT_COLOR_SLIGHTLY_STRONG.set(theme.TEXT_COLOR_SLIGHTLY_STRONG);
		TEXT_COLOR_NEUTRAL.set(theme.TEXT_COLOR_NEUTRAL);
		TEXT_COLOR_WEAK.set(theme.TEXT_COLOR_WEAK);
		COLOR_SATURATED.set(theme.COLOR_SATURATED);
		COLOR_POSITIVE.set(theme.COLOR_POSITIVE);
		COLOR_NEGATIVE.set(theme.COLOR_NEGATIVE);
		
		BLACK_ICONS.set(theme.BLACK_ICONS);
		CERTAINTY_COLOR_MAP.set(theme.CERTAINTY_COLOR_MAP);
	}
	
}
