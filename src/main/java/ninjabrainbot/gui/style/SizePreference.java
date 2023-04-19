package ninjabrainbot.gui.style;

import java.util.HashMap;

import ninjabrainbot.io.preferences.enums.SizeSetting;
import ninjabrainbot.util.I18n;

public abstract class SizePreference {

	public final String name;
	public int TEXT_SIZE_TITLE_LARGE;
	public int TEXT_SIZE_TITLE_SMALL;
	public int TEXT_SIZE_MEDIUM;
	public int TEXT_SIZE_SMALL;
	public int TEXT_SIZE_TINY;
	public int PADDING;
	public int PADDING_THIN;
	public int PADDING_TITLE;
	public int WIDTH;
	public int ANGLE_COLUMN_WIDTH;
	public int WINDOW_ROUNDING;

	public static final HashMap<String, SizePreference> SIZES = new HashMap<String, SizePreference>();
	public static final SizePreference REGULAR = new RegularSize();
	public static final SizePreference LARGE = new LargeSize();
	public static final SizePreference EXTRALARGE = new ExtraLargeSize();

	public static SizePreference get(SizeSetting sizeSetting) {
		return SIZES.getOrDefault(sizeSetting.choiceName(), REGULAR);
	}

	public SizePreference(String name) {
		this.name = name;
		SIZES.put(name, this);
	}

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
		ANGLE_COLUMN_WIDTH = 100;
		WINDOW_ROUNDING = 7;
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
		ANGLE_COLUMN_WIDTH = 110;
		WINDOW_ROUNDING = 7;
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
		ANGLE_COLUMN_WIDTH = 160;
		WINDOW_ROUNDING = 7;
	}

}
