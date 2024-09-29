package ninjabrainbot.gui.options.sections;

import javax.swing.border.EmptyBorder;

import ninjabrainbot.gui.buttons.WikiButton;
import ninjabrainbot.gui.components.labels.ThemedLabel;
import ninjabrainbot.gui.components.layout.StackPanel;
import ninjabrainbot.gui.components.preferences.CheckboxPanel;
import ninjabrainbot.gui.components.preferences.HotkeyPanel;
import ninjabrainbot.gui.components.preferences.RadioButtonPanel;
import ninjabrainbot.gui.frames.OptionsFrame;
import ninjabrainbot.gui.style.SizePreference;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.io.preferences.enums.AllAdvancementsToggleType;
import ninjabrainbot.util.I18n;

public class AllAdvancementsOptionsPanel extends StackPanel {

	private final NinjabrainBotPreferences preferences;
	private final CheckboxPanel oneDotTwentyPlusAA;
	private final RadioButtonPanel switchTypeRadioButtonPanel;
	private final HotkeyPanel toggleHotkeyPanel;

	public AllAdvancementsOptionsPanel(StyleManager styleManager, NinjabrainBotPreferences preferences) {
		super(2 * OptionsFrame.PADDING);
		this.preferences = preferences;
		setOpaque(false);
		setBorder(new EmptyBorder(2 * OptionsFrame.PADDING, 2 * OptionsFrame.PADDING, 2 * OptionsFrame.PADDING, 2 * OptionsFrame.PADDING));

		add(new ThemedLabel(styleManager, "<html>" + I18n.get("settings.all_advancements.explanation") + "<ul style=\"margin-left: 10px; margin-top: 0px; margin-bottom: 0px;\">" +
										  "<li>" + I18n.get("settings.all_advancements.spawn_coords") + "</li>" +
										  "<li>" + I18n.get("settings.all_advancements.monument_coords") + "</li>" +
										  "<li>" + I18n.get("settings.all_advancements.outpost_coords") + "</li>" +
										  "</ul></html>") {
			public int getTextSize(SizePreference p) {
				return p.TEXT_SIZE_SMALL;
			}
		});
		add(new CheckboxPanel(styleManager, I18n.get("settings.enable_all_advancements_mode"), preferences.allAdvancements));

		add(oneDotTwentyPlusAA = new CheckboxPanel(styleManager, I18n.get("settings.enable_120plus_all_advancements_mode"), preferences.oneDotTwentyPlusAA)
				.withWikiButton(new WikiButton(styleManager, "https://github.com/Ninjabrain1/Ninjabrain-Bot/wiki/All-advancements-mode-with-Minecraft-version-1.20-and-later")));

		add(switchTypeRadioButtonPanel = new RadioButtonPanel(styleManager, I18n.get("settings.all_advancements.switch_type"), preferences.allAdvancementsToggleType, true));
		add(toggleHotkeyPanel = new HotkeyPanel(styleManager, I18n.get("settings.all_advancements.toggle_aa_mode_hotkey"), preferences.hotkeyToggleAllAdvancementsMode));

		updateComponentsEnabled();
		preferences.allAdvancements.whenModified().subscribeEDT(this::updateComponentsEnabled);
		preferences.oneDotTwentyPlusAA.whenModified().subscribeEDT(this::updateComponentsEnabled);
		preferences.allAdvancementsToggleType.whenModified().subscribeEDT(this::updateComponentsEnabled);
	}

	private void updateComponentsEnabled() {
		oneDotTwentyPlusAA.setEnabled(preferences.allAdvancements.get());
		switchTypeRadioButtonPanel.setEnabled(preferences.allAdvancements.get());
		toggleHotkeyPanel.setEnabled(preferences.allAdvancements.get() && preferences.allAdvancementsToggleType.get() == AllAdvancementsToggleType.Hotkey);
	}

}
