package ninjabrainbot.gui.mainwindow.alladvancements;

import java.awt.Dimension;
import java.util.Objects;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;

import ninjabrainbot.Main;
import ninjabrainbot.event.IDisposable;
import ninjabrainbot.event.Subscription;
import ninjabrainbot.model.datastate.alladvancements.IAllAdvancementsDataState;
import ninjabrainbot.gui.components.panels.ThemedPanel;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
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
		add(new StructurePanel(styleManager, buttonInputHandler, allAdvancementsDataState.strongholdPosition(), strongholdIcon, false, true, true));
		add(new StructurePanel(styleManager, buttonInputHandler, allAdvancementsDataState.spawnPosition(), shulkerIcon, true, true, true));
		add(new StructurePanel(styleManager, buttonInputHandler, allAdvancementsDataState.outpostPosition(), outpostIcon, true, true, true));
		add(new StructurePanel(styleManager, buttonInputHandler, allAdvancementsDataState.monumentPosition(), monumentIcon, true, true, true));
		oneDotTwentyPlusPanels.add(new StructurePanel(styleManager, buttonInputHandler, allAdvancementsDataState.deepDarkPosition(), deepDarkIcon, true, true, true));
		oneDotTwentyPlusPanels.add(new StructurePanel(styleManager, buttonInputHandler, allAdvancementsDataState.shulkerTransportPosition(), shulkerTransportIcon, true, true, false));
		oneDotTwentyPlusPanels.add(new StructurePanel(styleManager, buttonInputHandler, allAdvancementsDataState.cityQueryPosition(), cityQueryIcon, true, true, false));
		oneDotTwentyPlusPanels.add(new StructurePanel(styleManager, buttonInputHandler, allAdvancementsDataState.generalLocationPosition(), generalLocationIcon, true, false, false));
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
