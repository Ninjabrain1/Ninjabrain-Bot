package ninjabrainbot.gui.style.theme;

import javax.swing.SwingUtilities;

import ninjabrainbot.event.ISubscribable;
import ninjabrainbot.event.ObservableProperty;
import ninjabrainbot.event.Subscription;
import ninjabrainbot.util.Wrapper;

public class CurrentTheme {

	public final WrappedColor COLOR_STRONGEST = new WrappedColor();
	public final WrappedColor COLOR_DIVIDER = new WrappedColor();
	public final WrappedColor COLOR_DIVIDER_DARK = new WrappedColor();
	public final WrappedColor COLOR_HEADER = new WrappedColor();
	public final WrappedColor COLOR_SLIGHTLY_STRONG = new WrappedColor();
	public final WrappedColor COLOR_NEUTRAL = new WrappedColor();
	public final WrappedColor COLOR_SLIGHTLY_WEAK = new WrappedColor();
	public final WrappedColor COLOR_EXIT_BUTTON_HOVER = new WrappedColor();
	public final WrappedColor TEXT_COLOR_SLIGHTLY_WEAK = new WrappedColor();
	public final WrappedColor TEXT_COLOR_SLIGHTLY_STRONG = new WrappedColor();
	public final WrappedColor TEXT_COLOR_NEUTRAL = new WrappedColor();
	public final WrappedColor TEXT_COLOR_WEAK = new WrappedColor();
	public final WrappedColor TEXT_COLOR_HEADER = new WrappedColor();
	public final WrappedColor COLOR_SATURATED = new WrappedColor();
	public final WrappedColor COLOR_POSITIVE = new WrappedColor();
	public final WrappedColor COLOR_NEGATIVE = new WrappedColor();
	public final WrappedColor TEXT_COLOR_TITLE = new WrappedColor();

	public final Wrapper<ColorMap> CERTAINTY_COLOR_MAP = new Wrapper<>();

	private final ObservableProperty<CurrentTheme> whenModified = new ObservableProperty<>();

	private Subscription themeSubscription;
	private Theme theme;

	public void setTheme(Theme theme) {
		COLOR_STRONGEST.set(theme.COLOR_STRONGEST);
		COLOR_DIVIDER.set(theme.COLOR_DIVIDER);
		COLOR_DIVIDER_DARK.set(theme.COLOR_DIVIDER_DARK);
		COLOR_HEADER.set(theme.COLOR_STRONG);
		COLOR_SLIGHTLY_STRONG.set(theme.COLOR_SLIGHTLY_STRONG);
		COLOR_NEUTRAL.set(theme.COLOR_NEUTRAL);
		COLOR_SLIGHTLY_WEAK.set(theme.COLOR_SLIGHTLY_WEAK);
		COLOR_EXIT_BUTTON_HOVER.set(theme.COLOR_EXIT_BUTTON_HOVER);
		TEXT_COLOR_SLIGHTLY_WEAK.set(theme.TEXT_COLOR_SLIGHTLY_WEAK);
		TEXT_COLOR_SLIGHTLY_STRONG.set(theme.TEXT_COLOR_SLIGHTLY_STRONG);
		TEXT_COLOR_NEUTRAL.set(theme.TEXT_COLOR_NEUTRAL);
		TEXT_COLOR_WEAK.set(theme.TEXT_COLOR_WEAK);
		TEXT_COLOR_HEADER.set(theme.TEXT_COLOR_HEADER);
		TEXT_COLOR_TITLE.set(theme.TEXT_COLOR_TITLE);
		COLOR_SATURATED.set(theme.COLOR_SATURATED);
		COLOR_POSITIVE.set(theme.COLOR_POSITIVE);
		COLOR_NEGATIVE.set(theme.COLOR_NEGATIVE);

		CERTAINTY_COLOR_MAP.set(new ColorMap(theme.COLOR_GRADIENT_0.color(), theme.COLOR_GRADIENT_50.color(), theme.COLOR_GRADIENT_100.color()));

		updateSubscription(theme);
		whenModified.notifySubscribers(this);
	}

	private void updateSubscription(Theme newTheme) {
		if (theme == newTheme)
			return;
		theme = newTheme;
		if (themeSubscription != null)
			themeSubscription.dispose();
		themeSubscription = newTheme.whenModified().subscribeEDT(this::setTheme);
	}

	public boolean isTheme(Theme theme) {
		return this.theme == theme;
	}

	public ISubscribable<CurrentTheme> whenModified() {
		return whenModified;
	}

}
