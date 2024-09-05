package ninjabrainbot.gui.mainwindow.alladvancements;

import java.util.ArrayList;
import java.util.Objects;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;

import ninjabrainbot.Main;
import ninjabrainbot.gui.components.panels.ThemedPanel;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.model.datastate.alladvancements.IAllAdvancementsDataState;
import ninjabrainbot.model.input.IButtonInputHandler;

public class AllAdvancementsPanel extends ThemedPanel {

	private final NinjabrainBotPreferences preferences;
	private static final ImageIcon strongholdIcon = new ImageIcon(Objects.requireNonNull(Main.class.getResource("/stronghold_icon.png")));
	private static final ImageIcon shulkerIcon = new ImageIcon(Objects.requireNonNull(Main.class.getResource("/spawn_icon.png")));
	private static final ImageIcon outpostIcon = new ImageIcon(Objects.requireNonNull(Main.class.getResource("/outpost_icon.png")));
	private static final ImageIcon monumentIcon = new ImageIcon(Objects.requireNonNull(Main.class.getResource("/monument_icon.png")));
	private static final ImageIcon deepDarkIcon = new ImageIcon(Objects.requireNonNull(Main.class.getResource("/deep_dark_icon.png")));
	private static final ImageIcon shulkerTransportIcon = new ImageIcon(Objects.requireNonNull(Main.class.getResource("/shulker_transport_icon.png")));
	private static final ImageIcon cityQueryIcon = new ImageIcon(Objects.requireNonNull(Main.class.getResource("/city_query_icon.png")));
	private static final ImageIcon generalLocationIcon = new ImageIcon(Objects.requireNonNull(Main.class.getResource("/general_location_icon.png")));

	private final ArrayList<StructurePanel> oneDotTwentyPlusPanels = new ArrayList<>();

	public AllAdvancementsPanel(StyleManager styleManager, IButtonInputHandler buttonInputHandler, IAllAdvancementsDataState allAdvancementsDataState, NinjabrainBotPreferences preferences) {
		super(styleManager);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(new AllAdvancementsHeader(styleManager));
		add(new StructurePanel(styleManager, buttonInputHandler, allAdvancementsDataState.strongholdInformation(), strongholdIcon, false, true, true));
		add(new StructurePanel(styleManager, buttonInputHandler, allAdvancementsDataState.spawnInformation(), shulkerIcon, true, true, true));
		add(new StructurePanel(styleManager, buttonInputHandler, allAdvancementsDataState.outpostInformation(), outpostIcon, true, true, true));
		add(new StructurePanel(styleManager, buttonInputHandler, allAdvancementsDataState.monumentInformation(), monumentIcon, true, true, true));
		oneDotTwentyPlusPanels.add(new StructurePanel(styleManager, buttonInputHandler, allAdvancementsDataState.deepDarkInformation(), deepDarkIcon, true, true, true));
		oneDotTwentyPlusPanels.add(new StructurePanel(styleManager, buttonInputHandler, allAdvancementsDataState.shulkerTransportInformation(), shulkerTransportIcon, true, true, false));
		oneDotTwentyPlusPanels.add(new StructurePanel(styleManager, buttonInputHandler, allAdvancementsDataState.cityQueryInformation(), cityQueryIcon, true, true, false));
		oneDotTwentyPlusPanels.add(new StructurePanel(styleManager, buttonInputHandler, allAdvancementsDataState.generalLocationInformation(), generalLocationIcon, true, false, false));
		for (StructurePanel panel : oneDotTwentyPlusPanels) {
			add(panel);
		}
		for (StructurePanel panel : oneDotTwentyPlusPanels) {
			panel.setEnabled(preferences.oneDotTwentyPlusAA.get());
			panel.setVisible(preferences.oneDotTwentyPlusAA.get());
		}
		this.preferences = preferences;
	}

	public void updateOneDotTwentyPlusAAEnabled() {
		for (StructurePanel panel : oneDotTwentyPlusPanels) {
			panel.setEnabled(preferences.oneDotTwentyPlusAA.get());
			panel.setVisible(preferences.oneDotTwentyPlusAA.get());
		}

		this.revalidate();
		this.repaint();
	}
}
