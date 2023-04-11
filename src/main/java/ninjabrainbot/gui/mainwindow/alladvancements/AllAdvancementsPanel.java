package ninjabrainbot.gui.mainwindow.alladvancements;

import java.util.Objects;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;

import ninjabrainbot.Main;
import ninjabrainbot.model.datastate.alladvancements.IAllAdvancementsDataState;
import ninjabrainbot.gui.components.panels.ThemedPanel;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.model.input.IButtonInputHandler;

public class AllAdvancementsPanel extends ThemedPanel {

	private static final ImageIcon strongholdIcon = new ImageIcon(Objects.requireNonNull(Main.class.getResource("/stronghold_icon.png")));
	private static final ImageIcon shulkerIcon = new ImageIcon(Objects.requireNonNull(Main.class.getResource("/spawn_icon.png")));
	private static final ImageIcon outpostIcon = new ImageIcon(Objects.requireNonNull(Main.class.getResource("/outpost_icon.png")));
	private static final ImageIcon monumentIcon = new ImageIcon(Objects.requireNonNull(Main.class.getResource("/monument_icon.png")));

	public AllAdvancementsPanel(StyleManager styleManager, IButtonInputHandler buttonInputHandler, IAllAdvancementsDataState allAdvancementsDataState) {
		super(styleManager);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(new AllAdvancementsHeader(styleManager));
		add(new StructurePanel(styleManager, buttonInputHandler, allAdvancementsDataState.strongholdPosition(), strongholdIcon, true));
		add(new StructurePanel(styleManager, buttonInputHandler, allAdvancementsDataState.spawnPosition(), shulkerIcon, true));
		add(new StructurePanel(styleManager, buttonInputHandler, allAdvancementsDataState.outpostPosition(), outpostIcon, true));
		add(new StructurePanel(styleManager, buttonInputHandler, allAdvancementsDataState.monumentPosition(), monumentIcon, false));
	}

}
