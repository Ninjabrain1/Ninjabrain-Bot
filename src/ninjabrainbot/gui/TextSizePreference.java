package ninjabrainbot.gui;

public abstract class TextSizePreference {

	public int TITLE_BAR_TEXT_SIZE;
	public int VERSION_TEXT_SIZE;
	public int THROW_TEXT_SIZE;
	public int TINY_TEXT_SIZE;
	public int THROW_HEADER_TEXT_SIZE;
	public int MAIN_TEXT_SIZE;
	public int SETTINGS_TEXT_SIZE;

	public static final TextSizePreference REGULAR = new RegularTextSize();

}

class RegularTextSize extends TextSizePreference {
	public RegularTextSize() {
		TITLE_BAR_TEXT_SIZE = 15;
		VERSION_TEXT_SIZE = 12;
		THROW_TEXT_SIZE = 12;
		TINY_TEXT_SIZE = 9;
		THROW_HEADER_TEXT_SIZE = 12;
		MAIN_TEXT_SIZE = 14;
		SETTINGS_TEXT_SIZE = 12;
	}
}
