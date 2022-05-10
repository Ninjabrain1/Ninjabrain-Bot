package ninjabrainbot.gui;

import ninjabrainbot.util.I18n;

import java.util.HashMap;

public abstract class SizePreference {

	public String name;
	public int TEXT_SIZE_TITLE_LARGE;
	public int TEXT_SIZE_TITLE_SMALL;
	public int TEXT_SIZE_MEDIUM;
	public int TEXT_SIZE_SMALL;
	public int TEXT_SIZE_TINY;
	public int PADDING;
	public int PADDING_THIN;
	public int PADDING_TITLE;
	public int WIDTH;
	public int WINDOW_ROUNDING;

	public static final int NUM_DETAILED_PANELS = 5;

	public static final HashMap<String, SizePreference> SIZES = new HashMap<String, SizePreference>();
	public static final SizePreference REGULAR = new RegularSize();
	public static final SizePreference LARGE = new LargeSize();
	public static final SizePreference EXTRALARGE = new ExtraLargeSize();

	public static SizePreference get(String name) {
		return SIZES.getOrDefault(name, REGULAR);
	}
	
	public SizePreference(String name) {
		this.name = name;
		SIZES.put(name, this);
	}

	public abstract void setAngleUpdatesEnabled(boolean b);
}

class RegularSize extends SizePreference {
	public RegularSize() {
		super(I18n.get("small"));
		TEXT_SIZE_TITLE_LARGE = 15;
		TEXT_SIZE_TITLE_SMALL = 12;
		TEXT_SIZE_MEDIUM = 14;
		TEXT_SIZE_SMALL = 12;
		TEXT_SIZE_TINY = 9;
		PADDING = 6;
		PADDING_THIN = 2;
		PADDING_TITLE = 6;
		WIDTH = 320;
		WINDOW_ROUNDING = 7;
	}

	@Override
	public void setAngleUpdatesEnabled(boolean b) {
		if (b) {
			WIDTH = 360;
		} else {
			WIDTH = 320;
		}
	}
}

class LargeSize extends SizePreference {
	public LargeSize() {
		super(I18n.get("medium"));
		TEXT_SIZE_TITLE_LARGE = 15;
		TEXT_SIZE_TITLE_SMALL = 12;
		TEXT_SIZE_MEDIUM = 16;
		TEXT_SIZE_SMALL = 14;
		TEXT_SIZE_TINY = 11;
		PADDING = 7;
		PADDING_THIN = 2;
		PADDING_TITLE = 6;
		WIDTH = 380;
		WINDOW_ROUNDING = 7;
	}

	@Override
	public void setAngleUpdatesEnabled(boolean b) {
		if (b) {
			WIDTH = 400;
		} else {
			WIDTH = 380;
		}
	}
}

class ExtraLargeSize extends SizePreference {
	public ExtraLargeSize() {
		super(I18n.get("large"));
		TEXT_SIZE_TITLE_LARGE = 15;
		TEXT_SIZE_TITLE_SMALL = 12;
		TEXT_SIZE_MEDIUM = 24;
		TEXT_SIZE_SMALL = 21;
		TEXT_SIZE_TINY = 18;
		PADDING = 9;
		PADDING_THIN = 3;
		PADDING_TITLE = 6;
		WIDTH = 570;
		WINDOW_ROUNDING = 7;
	}

	@Override
	public void setAngleUpdatesEnabled(boolean b) {
		if (b) {
			WIDTH = 600;
		} else {
			WIDTH = 570;
		}
	}
}
