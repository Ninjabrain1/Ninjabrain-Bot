package ninjabrainbot.gui.mainwindow.alladvancements;

import java.awt.Dimension;

import javax.swing.border.MatteBorder;

import ninjabrainbot.gui.components.labels.ThemedLabel;
import ninjabrainbot.gui.components.panels.ThemedPanel;
import ninjabrainbot.gui.style.SizePreference;
import ninjabrainbot.gui.style.StyleManager;
import ninjabrainbot.gui.style.theme.ColumnLayout;
import ninjabrainbot.gui.style.theme.WrappedColor;
import ninjabrainbot.io.preferences.MultipleChoicePreferenceDataTypes.StrongholdDisplayType;
import ninjabrainbot.util.I18n;

public class AllAdvancementsHeader extends ThemedPanel {

	final ThemedLabel location;
	final ThemedLabel nether;
	final ThemedLabel angle;

	final StyleManager styleManager;

	private final WrappedColor borderCol;

	public AllAdvancementsHeader(StyleManager styleManager) {
		super(styleManager, true);
		this.styleManager = styleManager;
		setOpaque(true);
		ThemedLabel iconLabelSubstitute = new ThemedLabel(styleManager);
		location = new ThemedLabel(styleManager, I18n.get("location"), true, true);
		nether = new ThemedLabel(styleManager, I18n.get("nether"), true, true);
		angle = new ThemedLabel(styleManager, I18n.get("angle"), true, true);
		ThemedLabel removeStructureButtonSubstitute = new ThemedLabel(styleManager);

		ColumnLayout layout = new ColumnLayout(0);
		layout.setRelativeWidth(iconLabelSubstitute, 0.4f);
		layout.setRelativeWidth(location, 1.9f);
		layout.setRelativeWidth(nether, 1.7f);
		layout.setRelativeWidth(angle, 0.8f);
		layout.setRelativeWidth(removeStructureButtonSubstitute, 0.4f, true);
		setLayout(layout);

		add(iconLabelSubstitute);
		add(location);
		add(nether);
		add(angle);
		add(removeStructureButtonSubstitute);

		borderCol = styleManager.currentTheme.COLOR_DIVIDER_DARK;
		setBackgroundColor(styleManager.currentTheme.COLOR_HEADER);

		location.setForegroundColor(styleManager.currentTheme.TEXT_COLOR_HEADER);
		nether.setForegroundColor(styleManager.currentTheme.TEXT_COLOR_HEADER);
		angle.setForegroundColor(styleManager.currentTheme.TEXT_COLOR_HEADER);
	}

	public void updateHeaderText(StrongholdDisplayType sdt) {
		location.setText(sdt == StrongholdDisplayType.CHUNK ? I18n.get("chunk") : I18n.get("location"));
	}

	public void setAngleUpdatesEnabled(boolean b) {
		if (b) {
			add(angle);
		} else {
			remove(angle);
		}
	}

	@Override
	public void updateColors() {
		setBorder(new MatteBorder(0, 0, 2, 0, borderCol.color()));
		super.updateColors();
	}

	@Override
	public void updateSize(StyleManager styleManager) {
		super.updateSize(styleManager);
		setPreferredSize(new Dimension(styleManager.size.WIDTH, styleManager.size.TEXT_SIZE_MEDIUM + styleManager.size.PADDING_THIN * 2 + 2));
	}

	@Override
	public int getTextSize(SizePreference p) {
		return p.TEXT_SIZE_MEDIUM;
	}

}
